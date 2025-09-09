package com.busify.project.bus_operator.service.imp;

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
import com.busify.project.user.mapper.UserMapper;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.user.entity.User;

import com.busify.project.user.repository.UserRepository;
import com.busify.project.bus_operator.exception.BusOperatorCreationException;
import com.busify.project.bus_operator.exception.BusOperatorUpdateException;
import com.busify.project.bus_operator.exception.BusOperatorDeleteException;
import com.busify.project.bus_operator.exception.BusOperatorNotFoundException;
import com.busify.project.bus_operator.exception.BusOperatorLicenseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class BusOperatorServiceImpl implements BusOperatorService {

        private final BusOperatorRepository busOperatorRepository;
        private final ReviewRepository reviewRepository;
        private final BusRepository busRepository;
        private final UserRepository userRepository;
        private final CloudinaryService cloudinaryService;
        private final RoleRepository roleRepository;
        private final JwtUtils utils;
        private final AuditLogService auditLogService;

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
                                .orElseThrow(() -> BusOperatorNotFoundException.withId(id));
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
                                .orElseThrow(() -> BusOperatorCreationException.ownerNotFound(request.getEmail()));

                // Upload license file to Cloudinary if provided
                String licensePath = null;
                if (request.getLicenseFile() != null && !request.getLicenseFile().isEmpty()) {
                        try {
                                licensePath = cloudinaryService.uploadFile(request.getLicenseFile(), "busify/licenses");
                        } catch (Exception e) {
                                throw BusOperatorLicenseException.uploadFailed(e.getMessage());
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
                                .orElseThrow(() -> BusOperatorCreationException.defaultRoleNotFound());

                owner.setRole(defaultRole);
                userRepository.save(owner);
                
                // Audit log for bus operator creation
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("CREATE");
                auditLog.setTargetEntity("BUS_OPERATOR");
                auditLog.setTargetId(savedOperator.getId());
                auditLog.setDetails(String.format("{\"name\":\"%s\",\"email\":\"%s\",\"status\":\"%s\",\"ownerEmail\":\"%s\"}", 
                        savedOperator.getName(), savedOperator.getEmail(), savedOperator.getStatus(), owner.getEmail()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
                
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
                                .orElseThrow(() -> BusOperatorUpdateException.operatorNotFound(id));

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
                                throw BusOperatorLicenseException.uploadFailed(e.getMessage());
                        }
                }

                if (request.getEmail() != null) {
                        Profile newOwner = (Profile) userRepository.findByEmail(request.getEmail())
                                        .orElseThrow(() -> BusOperatorUpdateException
                                                        .ownerNotFound(request.getEmail()));
                        busOperator.setOwner(newOwner);
                }

                // Save updated operator
                BusOperator updatedOperator = busOperatorRepository.save(busOperator);

                // Audit log for bus operator update
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("UPDATE");
                auditLog.setTargetEntity("BUS_OPERATOR");
                auditLog.setTargetId(id);
                auditLog.setDetails(String.format("{\"name\":\"%s\",\"email\":\"%s\",\"status\":\"%s\"}", 
                        updatedOperator.getName(), updatedOperator.getEmail(), updatedOperator.getStatus()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);

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
                                .orElseThrow(() -> BusOperatorDeleteException.operatorNotFound(id));

                // Soft delete - set isDeleted flag and inactive status
                busOperator.setDeleted(true);

                // Audit log for bus operator deletion (before save)
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("DELETE");
                auditLog.setTargetEntity("BUS_OPERATOR");
                auditLog.setTargetId(id);
                auditLog.setDetails(String.format("{\"name\":\"%s\",\"email\":\"%s\",\"action\":\"soft_delete\"}", 
                        busOperator.getName(), busOperator.getEmail()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);

                busOperatorRepository.save(busOperator);
        }

        @Override
        public BusOperatorForManagement getBusOperatorForManagementById(Long id) {
                // Find bus operator
                BusOperator busOperator = busOperatorRepository.findById(id)
                                .orElseThrow(() -> BusOperatorNotFoundException.withId(id));

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
                                        .orElseThrow(() -> BusOperatorNotFoundException.withId(operatorId));

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
                                .orElseThrow(() -> BusOperatorCreationException.ownerNotFound(email))
                                .getId();
                System.out.println("User email: " + email);
//                System.out.println("User ID: " + userId);

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
        public MonthlyBusOperatorReportDTO getCurrentMonthReport(Long operatorId) {
                LocalDate now = LocalDate.now();
                return getMonthlyReportByOperatorId(operatorId, now.getMonthValue(), now.getYear());
        }

        @Override
        public List<MonthlyTotalRevenueDTO> getMonthlyTotalRevenueByYear(int year) {
                return busOperatorRepository.findMonthlyTotalRevenueByYear(year);
        }

        @Override
        public void markReportAsSent(int month, int year) {
                try {
                        // Log việc đánh dấu báo cáo đã được gửi
                        log.info("📝 Đánh dấu báo cáo tháng {}/{} đã được gửi", month, year);

                } catch (Exception e) {
                        log.error("❌ Lỗi khi đánh dấu báo cáo đã gửi: {}", e.getMessage(), e);
                        // Không throw exception vì đây không phải critical operation
                }
        }
        
        /**
         * Helper method to get current user for audit logging
         */
        private User getCurrentUser() {
                try {
                        String currentUserEmail = utils.getCurrentUserLogin().orElse(null);
                        if (currentUserEmail != null) {
                                return userRepository.findByEmail(currentUserEmail).orElse(null);
                        }
                        return null;
                } catch (Exception e) {
                        // Return null if unable to get current user (e.g., system operations)
                        return null;
                }
        }

}