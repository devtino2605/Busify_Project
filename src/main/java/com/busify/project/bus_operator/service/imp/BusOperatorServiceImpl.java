package com.busify.project.bus_operator.service.imp;

import com.busify.project.booking.dto.response.BookingStatusCountDTO;
import com.busify.project.bus.dto.response.BusSummaryResponseDTO;
import com.busify.project.bus.repository.BusRepository;
import com.busify.project.bus_operator.dto.request.BusOperatorFilterRequest;
import com.busify.project.bus_operator.dto.request.CreateBusOperatorRequest;
import com.busify.project.bus_operator.dto.request.UpdateBusOperatorRequest;
import com.busify.project.bus_operator.dto.response.*;
import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.enums.OperatorStatus;
import com.busify.project.bus_operator.mapper.BusOperatorMapper;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.bus_operator.service.BusOperatorService;
import com.busify.project.common.service.CloudinaryService;
import com.busify.project.review.repository.ReviewRepository;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.mapper.UserMapper;
import com.busify.project.common.utils.JwtUtils;

import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusOperatorServiceImpl implements BusOperatorService {

        private final BusOperatorRepository busOperatorRepository;
        private final ReviewRepository reviewRepository;
        private final BusRepository busRepository;
        private final UserRepository userRepository;
        private final CloudinaryService cloudinaryService;
        private final RoleRepository roleRepository;
        private final JwtUtils utils;

        @Override
        public List<BusOperatorFilterTripResponse> getAllBusOperators() {
                return busOperatorRepository.findAll()
                                .stream()
                                .map(BusOperatorMapper::toDTO)
                                .collect(Collectors.toList());
        }

        @Override
        public List<BusOperatorRatingResponse> getAllBusOperatorsByRating(Integer limit) {
                Pageable pageable = PageRequest.of(0, limit);
                return busOperatorRepository.findAllOperatorsWithRatings(pageable);
        }

        public BusOperatorDetailsResponse getOperatorById(Long id) {
                final BusOperator busOperator = busOperatorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Bus operator not found with id: " + id));
                final Double rating = reviewRepository.findAverageRatingByOperatorId(id);
                final Long totalReviews = reviewRepository.countByBusOperatorId(id);
                return new BusOperatorDetailsResponse(
                                busOperator.getId(),
                                busOperator.getName(),
                                busOperator.getEmail(),
                                busOperator.getHotline(),
                                busOperator.getDescription(),
                                "/logo.png",
                                busOperator.getAddress(),
                                rating != null ? rating : 0.0,
                                totalReviews != null ? totalReviews : 0L);
        }

        public List<BusOperatorResponse> getAllActiveOperators() {
                List<BusOperator> activeOperators = busOperatorRepository.findByStatus(OperatorStatus.active);
                return activeOperators.stream()
                                .map(operator -> new BusOperatorResponse(
                                                operator.getId(),
                                                operator.getName(),
                                                operator.getHotline(),
                                                operator.getAddress(),
                                                operator.getEmail(), operator.getDescription(),
                                                operator.getStatus()))
                                .toList();
        }

        @Override
        public BusOperatorManagementPageResponse getBusOperatorsForManagement(BusOperatorFilterRequest filterRequest) {
                // Create sort object
                Sort sort = Sort.by(Sort.Direction.fromString(filterRequest.getSortDirection()),
                                getSortField(filterRequest.getSortBy()));

                // Create pageable
                Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getSize(), sort);

                // Get paginated operators with filters
                Page<BusOperator> operatorsPage = busOperatorRepository.findBusOperatorsForManagement(
                                filterRequest.getSearch(),
                                filterRequest.getStatus(),
                                filterRequest.getOwnerName(),
                                pageable);

                // Get operator IDs for bus fetching
                List<Long> operatorIds = operatorsPage.getContent()
                                .stream()
                                .map(BusOperator::getId)
                                .toList();

                // Fetch all buses in one query if there are operators
                Map<Long, List<BusSummaryResponseDTO>> busMap = Map.of();
                if (!operatorIds.isEmpty()) {
                        List<BusSummaryResponseDTO> allBuses = busRepository.findBusesByOperatorIds(operatorIds);
                        busMap = allBuses.stream()
                                        .collect(Collectors.groupingBy(BusSummaryResponseDTO::getOperatorId));
                }

                // Convert to DTOs
                final Map<Long, List<BusSummaryResponseDTO>> finalBusMap = busMap;
                List<BusOperatorForManagement> content = operatorsPage.getContent()
                                .stream()
                                .map(operator -> BusOperatorForManagement.builder()
                                                .operatorId(operator.getId())
                                                .operatorName(operator.getName())
                                                .email(operator.getEmail())
                                                .hotline(operator.getHotline())
                                                .address(operator.getAddress())
                                                .licensePath(operator.getLicensePath())
                                                .description(operator.getDescription())
                                                .status(operator.getStatus())
                                                .owner(UserMapper.toDTO(operator.getOwner()))
                                                .busesOwned(finalBusMap.getOrDefault(operator.getId(), List.of()))
                                                .dateOfResignation(operator.getCreatedAt())
                                                .build())
                                .toList();

                // Create page response
                Page<BusOperatorForManagement> resultPage = new PageImpl<>(
                                content, pageable, operatorsPage.getTotalElements());

                return BusOperatorManagementPageResponse.fromPage(resultPage);
        }

        private String getSortField(String sortBy) {
                return switch (sortBy) {
                        case "operatorName" -> "name";
                        case "email" -> "email";
                        case "hotline" -> "hotline";
                        case "status" -> "status";
                        case "dateOfResignation" -> "createdAt";
                        default -> "name";
                };
        }

        @Override
        public BusOperatorForManagement createBusOperator(CreateBusOperatorRequest request) {
                // Find owner profile
                Profile owner = (Profile) userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException(
                                                "Owner not found with email: " + request.getEmail()));

                // Upload license file to Cloudinary if provided
                String licensePath = null;
                if (request.getLicenseFile() != null && !request.getLicenseFile().isEmpty()) {
                        try {
                                licensePath = cloudinaryService.uploadFile(request.getLicenseFile(), "busify/licenses");
                        } catch (Exception e) {
                                throw new RuntimeException("Failed to upload license file: " + e.getMessage());
                        }
                }

                // Create new bus operator
                BusOperator busOperator = new BusOperator();
                busOperator.setName(request.getName());
                busOperator.setEmail(request.getEmail());
                busOperator.setHotline(request.getHotline());
                busOperator.setAddress(request.getAddress());
                busOperator.setDescription(request.getDescription());
                busOperator.setLicensePath(licensePath);
                busOperator.setOwner(owner);
                busOperator.setStatus(OperatorStatus.active); // Default status
                busOperator.setDeleted(false);

                // Save bus operator
                BusOperator savedOperator = busOperatorRepository.save(busOperator);

                Role defaultRole = roleRepository.findByName("BUS_OPERATOR")
                                .orElseThrow(() -> new RuntimeException("Default BUS_OPERATOR role not found"));

                owner.setRole(defaultRole);
                userRepository.save(owner);
                // Return management DTO
                return BusOperatorForManagement.builder()
                                .operatorId(savedOperator.getId())
                                .operatorName(savedOperator.getName())
                                .email(savedOperator.getEmail())
                                .hotline(savedOperator.getHotline())
                                .address(savedOperator.getAddress())
                                .licensePath(savedOperator.getLicensePath())
                                .status(savedOperator.getStatus())
                                .owner(UserMapper.toDTO(savedOperator.getOwner()))
                                .busesOwned(List.of()) // New operator has no buses initially
                                .dateOfResignation(savedOperator.getCreatedAt())
                                .build();
        }

        @Override
        public BusOperatorForManagement updateBusOperator(Long id, UpdateBusOperatorRequest request) {
                // Find existing bus operator
                BusOperator busOperator = busOperatorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Bus operator not found with id: " + id));

                // Handle license file upload if provided
                if (request.getLicenseFile() != null && !request.getLicenseFile().isEmpty()) {
                        try {
                                // Delete old license file if exists
                                if (busOperator.getLicensePath() != null && !busOperator.getLicensePath().isEmpty()) {
                                        String oldPublicId = cloudinaryService
                                                        .extractPublicId(busOperator.getLicensePath());
                                        if (oldPublicId != null) {
                                                cloudinaryService.deleteFile(oldPublicId);
                                        }
                                }
                                // Upload new license file
                                String newLicensePath = cloudinaryService.uploadFile(request.getLicenseFile(),
                                                "licenses");
                                busOperator.setLicensePath(newLicensePath);
                        } catch (Exception e) {
                                throw new RuntimeException("Failed to upload license file: " + e.getMessage());
                        }
                }

                if (request.getEmail() != null) {
                        Profile newOwner = (Profile) userRepository.findByEmail(request.getEmail())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Owner not found with email: " + request.getEmail()));
                        busOperator.setOwner(newOwner);
                }

                // Save updated operator
                BusOperator updatedOperator = busOperatorRepository.save(busOperator);

                // Get buses for this operator
                List<BusSummaryResponseDTO> buses = busRepository
                                .findBusesByOperatorIds(List.of(updatedOperator.getId()));

                // Return management DTO
                return BusOperatorForManagement.builder()
                                .operatorId(updatedOperator.getId())
                                .operatorName(updatedOperator.getName())
                                .email(updatedOperator.getEmail())
                                .hotline(updatedOperator.getHotline())
                                .address(updatedOperator.getAddress())
                                .licensePath(updatedOperator.getLicensePath())
                                .description(updatedOperator.getDescription())
                                .status(updatedOperator.getStatus())
                                .owner(UserMapper.toDTO(updatedOperator.getOwner()))
                                .busesOwned(buses)
                                .dateOfResignation(updatedOperator.getCreatedAt())
                                .build();
        }

        @Override
        public void deleteBusOperator(Long id) {
                // Find existing bus operator
                BusOperator busOperator = busOperatorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Bus operator not found with id: " + id));

                // Soft delete - set isDeleted flag and inactive status
                busOperator.setDeleted(true);

                busOperatorRepository.save(busOperator);
        }

        @Override
        public BusOperatorForManagement getBusOperatorForManagementById(Long id) {
                // Find bus operator
                BusOperator busOperator = busOperatorRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Bus operator not found with id: " + id));

                // Get buses for this operator
                List<BusSummaryResponseDTO> buses = busRepository.findBusesByOperatorIds(List.of(id));

                // Return management DTO
                return BusOperatorForManagement.builder()
                                .operatorId(busOperator.getId())
                                .operatorName(busOperator.getName())
                                .email(busOperator.getEmail())
                                .hotline(busOperator.getHotline())
                                .address(busOperator.getAddress())
                                .licensePath(busOperator.getLicensePath())
                                .status(busOperator.getStatus())
                                .owner(UserMapper.toDTO(busOperator.getOwner()))
                                .busesOwned(buses)
                                .dateOfResignation(busOperator.getCreatedAt())
                                .build();
        }

        @Override
        public MonthlyBusOperatorReportDTO getMonthlyReportByOperatorId(Long operatorId, int month, int year) {
                MonthlyBusOperatorReportDTO report = busOperatorRepository.findMonthlyReportByOperatorId(operatorId,
                                month, year);
                if (report == null) {
                        // Tạo báo cáo rỗng nếu không có dữ liệu
                        BusOperator operator = busOperatorRepository.findById(operatorId)
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Bus operator not found with id: " + operatorId));

                        report = new MonthlyBusOperatorReportDTOImpl(
                                        operatorId,
                                        operator.getName(),
                                        operator.getEmail(),
                                        Long.valueOf(month),
                                        Long.valueOf(year),
                                        0L,
                                        java.math.BigDecimal.ZERO,
                                        0L,
                                        0L,
                                        new java.sql.Date(System.currentTimeMillis()),
                                        0);
                }
                return report;
        }

        public BusOperatorResponse getOperatorDetailByUser() {
                String email = utils.getCurrentUserLogin().isPresent() ? utils.getCurrentUserLogin().get() : null;
                final Long userId = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found with email: " + email))
                                .getId();
                System.out.println("User email: " + email);
                System.out.println("User ID: " + userId);

                final BusOperator busOperator = busOperatorRepository.findBusOperatorByUserId(userId);
                return new BusOperatorResponse(
                                busOperator.getId(),
                                busOperator.getName(),
                                busOperator.getHotline(),
                                busOperator.getAddress(),
                                busOperator.getEmail(),
                                busOperator.getDescription(),
                                busOperator.getStatus());
        }

        public WeeklyBusOperatorReportDTO getWeeklyReportByOperatorId(Long operatorId) {
                return busOperatorRepository.findWeeklyReportByOperatorId(operatorId);
        }

        @Override
        public AdminMonthlyReportsResponse getAllMonthlyReports(int month, int year) {
                List<MonthlyBusOperatorReportDTO> operatorReports = busOperatorRepository.findAllMonthlyReports(month,
                                year);

                // Tính tổng doanh thu của hệ thống
                BigDecimal totalSystemRevenue = operatorReports.stream()
                                .map(MonthlyBusOperatorReportDTO::getTotalRevenue)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                Long totalTrips = operatorReports.stream()
                                .mapToLong(MonthlyBusOperatorReportDTO::getTotalTrips)
                                .sum();

                Long totalPassengers = operatorReports.stream()
                                .mapToLong(MonthlyBusOperatorReportDTO::getTotalPassengers)
                                .sum();

                return new AdminMonthlyReportsResponse(
                                month, year, totalSystemRevenue,
                                (long) operatorReports.size(), totalTrips, totalPassengers,
                                operatorReports);
        }

        @Override
        public void sendMonthlyReportsToAdmin(int month, int year) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'sendMonthlyReportsToAdmin'");
        }

        @Override
        public MonthlyBusOperatorReportDTO getCurrentMonthReport(Long operatorId) {
                LocalDate now = LocalDate.now();
                return getMonthlyReportByOperatorId(operatorId, now.getMonthValue(), now.getYear());
        }

        @Override
        public List<MonthlyTotalRevenueDTO> getMonthlyTotalRevenueByYear(int year) {
                return busOperatorRepository.findMonthlyTotalRevenueByYear(year);
        }

}