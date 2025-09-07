package com.busify.project.bus.service.impl;

import com.busify.project.bus.dto.request.BusMGMTRequestDTO;
import com.busify.project.bus.dto.response.BusDeleteResponseDTO;
import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.dto.response.BusMGMTResponseDTO;
import com.busify.project.bus.entity.Bus;
import com.busify.project.bus.entity.BusImage;
import com.busify.project.bus.enums.BusStatus;
import com.busify.project.bus.mapper.BusMGMTMapper;
import com.busify.project.bus.repository.BusRepository;
import com.busify.project.bus.repository.BusImageRepository;
import com.busify.project.bus_model.entity.BusModel;
import com.busify.project.bus_model.repository.BusModelRepository;
import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.common.service.CloudinaryService;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.seat_layout.entity.SeatLayout;
import com.busify.project.seat_layout.repository.SeatLayoutRepository;
import com.busify.project.bus.service.BusMGMTService;
import com.busify.project.bus.exception.BusCreationException;
import com.busify.project.bus.exception.BusUpdateException;
import com.busify.project.bus.exception.BusNotFoundException;
import com.busify.project.bus.exception.BusDeleteException;
import com.busify.project.bus.exception.BusSeatLayoutException;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusMGMTServiceImpl implements BusMGMTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusMGMTServiceImpl.class);

    private final BusRepository busRepository;
    private final BusImageRepository busImageRepository;        // <-- inject repository
    private final BusOperatorRepository busOperatorRepository;
    private final SeatLayoutRepository seatLayoutRepository;
    private final BusModelRepository busModelRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtil;
    private final CloudinaryService cloudinaryService;
    private final ObjectMapper objectMapper;

    /**
     * Tạo Bus mới và upload ảnh (nếu có).
     * Nếu có lỗi upload thì ném exception -> transaction rollback.
     */
    @Override
    @Transactional
    public BusMGMTResponseDTO addBus(BusMGMTRequestDTO requestDTO) {
        Bus bus = new Bus();

        if (busRepository.existsByLicensePlate(requestDTO.getLicensePlate())) {
            throw new ValidationException("Biển số xe đã tồn tại trong hệ thống");
        }
        bus.setLicensePlate(requestDTO.getLicensePlate());

        // Lấy user hiện tại
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        BusOperator operator = busOperatorRepository.findById(operatorId)
                .orElseThrow(() -> BusCreationException.invalidModel(operatorId));
        bus.setOperator(operator);

        // BusModel
        BusModel model = busModelRepository.findById(requestDTO.getModelId())
                .orElseThrow(() -> BusCreationException.invalidModel(requestDTO.getModelId()));
        bus.setModel(model);

        // SeatLayout
        SeatLayout seatLayout = seatLayoutRepository.findById(requestDTO.getSeatLayoutId())
                .orElseThrow(() -> BusCreationException.invalidSeatLayout(requestDTO.getSeatLayoutId().longValue()));
        bus.setSeatLayout(seatLayout);

        // Tính total seats
        try {
            JSONObject layoutData = new JSONObject(seatLayout.getLayoutData());
            int cols = (int) layoutData.get("cols");
            int rows = (int) layoutData.get("rows");
            int floors = (int) layoutData.get("floors");
            bus.setTotalSeats(cols * rows * floors);
        } catch (Exception e) {
            throw BusCreationException.invalidSeatLayoutData("Unable to parse seat layout data: " + e.getMessage());
        }

        // Parse amenities (requestDTO.getAmenities() expected là JSON string)
        Map<String, Object> amenitiesMap = new HashMap<>();
        try {
            if (requestDTO.getAmenities() != null && !requestDTO.getAmenities().isBlank()) {
                amenitiesMap = objectMapper.readValue(requestDTO.getAmenities(), Map.class);
            }
        } catch (Exception e) {
            LOGGER.error("Lỗi parse amenities payload: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi parse amenities: " + e.getMessage());
        }

        bus.setAmenities(amenitiesMap);
        bus.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : BusStatus.active);

        // Lưu bus trước, sau đó upload ảnh và lưu BusImage
        Bus savedBus = busRepository.save(bus);

        // Upload images nếu có
        List<MultipartFile> images = requestDTO.getImages();
        if (images != null && !images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                String imageUrl = null;
                String publicId = null;

                // Thử upload với 1 retry để giảm lỗi timeout tạm thời
                int attempts = 0;
                int maxAttempts = 2;
                Exception lastEx = null;
                while (attempts < maxAttempts) {
                    attempts++;
                    try {
                        imageUrl = cloudinaryService.uploadFile(file, "busify/buses/images");
                        publicId = cloudinaryService.extractPublicId(imageUrl);
                        break; // thành công -> thoát vòng
                    } catch (Exception ex) {
                        lastEx = ex;
                        LOGGER.warn("Upload ảnh thất bại attempt {}/{}: {}", attempts, maxAttempts, ex.getMessage());
                        // Nếu còn attempt, thử lại
                    }
                }

                if (imageUrl == null) {
                    // Nếu muốn không rollback mà chỉ log, bạn có thể tiếp tục; hiện tại ném exception để rollback
                    LOGGER.error("Upload ảnh thất bại toàn bộ attempts: {}", lastEx == null ? "unknown" : lastEx.getMessage());
                    throw new RuntimeException("Không thể upload ảnh bus: " + (lastEx != null ? lastEx.getMessage() : ""));
                }

                // Lưu BusImage vào DB
                try {
                    BusImage busImage = new BusImage();
                    busImage.setBus(savedBus);
                    busImage.setImageUrl(imageUrl);
                    busImage.setPublicId(publicId);
                    busImage.setPrimary(i == 0);
                    busImageRepository.save(busImage);
                } catch (Exception e) {
                    LOGGER.error("Không thể lưu BusImage vào DB: {}", e.getMessage(), e);
                    throw new RuntimeException("Không thể lưu ảnh bus vào database: " + e.getMessage());
                }
            }
        }

        return BusMGMTMapper.toBusDetailResponseDTO(savedBus);
    }

    /**
     * Update bus + upload ảnh mới (nếu có).
     */
    @Override
    @Transactional
    public BusMGMTResponseDTO  updateBus(Long id, BusMGMTRequestDTO requestDTO) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> BusUpdateException.busNotFound(id));

        // Auth + operator validation
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        if (requestDTO.getLicensePlate() != null) {
            if (busRepository.existsByLicensePlateAndIdNot(requestDTO.getLicensePlate(), id)) {
                throw new ValidationException("Biển số xe đã tồn tại trong hệ thống");
            }
            bus.setLicensePlate(requestDTO.getLicensePlate());
        }

        if (requestDTO.getModelId() != null) {
            BusModel model = busModelRepository.findById(requestDTO.getModelId())
                    .orElseThrow(() -> BusUpdateException.invalidModel(requestDTO.getModelId()));
            bus.setModel(model);
        }

        if (requestDTO.getOperatorId() != null) {
            BusOperator operator = busOperatorRepository.findById(operatorId)
                    .orElseThrow(() -> BusUpdateException.unauthorizedOperator(id, operatorId));
            bus.setOperator(operator);
        }

        if (requestDTO.getSeatLayoutId() != null) {
            SeatLayout seatLayout = seatLayoutRepository.findById(requestDTO.getSeatLayoutId())
                    .orElseThrow(() -> BusUpdateException.invalidSeatLayout(requestDTO.getSeatLayoutId().longValue()));
            bus.setSeatLayout(seatLayout);

            try {
                JSONObject layoutData = new JSONObject(seatLayout.getLayoutData());
                int cols = (int) layoutData.get("cols");
                int rows = (int) layoutData.get("rows");
                int floors = (int) layoutData.get("floors");
                bus.setTotalSeats(cols * rows * floors);
            } catch (Exception e) {
                throw BusSeatLayoutException.invalidLayoutData("Unable to parse seat layout data: " + e.getMessage());
            }
        }

        // Parse amenities nếu gửi lên
        try {
            if (requestDTO.getAmenities() != null && !requestDTO.getAmenities().isBlank()) {
                Map<String, Object> amenitiesMap = objectMapper.readValue(requestDTO.getAmenities(), Map.class);
                bus.setAmenities(amenitiesMap);
            }
        } catch (Exception e) {
            LOGGER.error("Lỗi parse amenities: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi parse amenities: " + e.getMessage());
        }

        if (requestDTO.getStatus() != null) {
            bus.setStatus(requestDTO.getStatus());
        }

        // Xử lý ảnh bị xoá
        if (requestDTO.getDeletedImageIds() != null && !requestDTO.getDeletedImageIds().isEmpty()) {
            requestDTO.getDeletedImageIds().forEach(imgId -> {
                BusImage img = busImageRepository.findById(imgId)
                        .orElseThrow(() -> new RuntimeException("Ảnh không tồn tại: " + imgId));
                try {
                    String publicId = img.getPublicId();
                    if (publicId == null || publicId.isBlank()) {
                        publicId = cloudinaryService.extractPublicId(img.getImageUrl());
                    }
                    if (publicId != null) {
                        cloudinaryService.deleteFile(publicId);
                        LOGGER.info("Đã xóa ảnh trên Cloudinary: {}", publicId);
                    }
                } catch (Exception e) {
                    LOGGER.warn("Không xóa được ảnh trên Cloudinary (imgId={}): {}", imgId, e.getMessage());
                }
                // ✅ Xóa cả trong collection Bus + DB
                bus.getImages().remove(img);
                busImageRepository.deleteById(imgId);
            });
        }

        // ✅ Thêm ảnh mới (nếu có)
        List<MultipartFile> newImages = requestDTO.getImages();
        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile file : newImages) {
                try {
                    String imageUrl = cloudinaryService.uploadFile(file, "busify/buses/images");
                    String publicId = cloudinaryService.extractPublicId(imageUrl);

                    BusImage busImage = new BusImage();
                    busImage.setBus(bus);
                    busImage.setImageUrl(imageUrl);
                    busImage.setPublicId(publicId);
                    busImage.setPrimary(false); // tuỳ logic: có thể đặt ảnh đầu tiên là primary
                    busImageRepository.save(busImage);
                } catch (Exception e) {
                    throw new RuntimeException("Không thể upload ảnh bus: " + e.getMessage(), e);
                }
            }
        }

        Bus updatedBus = busRepository.save(bus);
        return BusMGMTMapper.toBusDetailResponseDTO(updatedBus);
    }

    @Override
    public BusDeleteResponseDTO deleteBus(Long id, boolean isDelete) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new BusNotFoundException(id));

        boolean existsInTrips = tripRepository.existsByBusId(bus.getId());
        if (existsInTrips) {
            throw BusDeleteException.hasActiveTrips(id, 1);
        }

        if (isDelete) {
            // Xóa ảnh liên quan (nếu cần) trước khi xóa bus
            List<BusImage> images = busImageRepository.findByBusId(bus.getId());
            if (images != null && !images.isEmpty()) {
                images.forEach(img -> {
                    try {
                        String publicId = cloudinaryService.extractPublicId(img.getImageUrl());
                        if (publicId != null) {
                            cloudinaryService.deleteFile(publicId);
                        }
                    } catch (Exception e) {
                        LOGGER.warn("Không xóa được ảnh trên Cloudinary: {}", e.getMessage());
                    }
                    busImageRepository.delete(img);
                });
            }
            busRepository.delete(bus);
        }

        return new BusDeleteResponseDTO(
                bus.getId(),
                bus.getLicensePlate(),
                bus.getModel().getName(),
                bus.getOperator().getName(),
                bus.getSeatLayout().getName());
    }

    @Override
    public ApiResponse<?> getAllBuses(String keyword, BusStatus status, List<String> amenities, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Long operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));

        String amenitiesJson;
        try {
            amenitiesJson = (amenities == null || amenities.isEmpty())
                    ? "[]"
                    : new ObjectMapper().writeValueAsString(amenities);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi chuyển amenities sang JSON", e);
        }

        Page<Bus> busPage = busRepository.searchAndFilterBuses(
                keyword,
                status != null ? status.name() : null,
                amenities,
                amenitiesJson,
                operatorId,
                pageable);

        List<BusMGMTResponseDTO > content = busPage.stream()
                .map(BusMGMTMapper::toBusDetailResponseDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("result", content);
        response.put("pageNumber", busPage.getNumber() + 1);
        response.put("pageSize", busPage.getSize());
        response.put("totalRecords", busPage.getTotalElements());
        response.put("totalPages", busPage.getTotalPages());
        response.put("hasNext", busPage.hasNext());
        response.put("hasPrevious", busPage.hasPrevious());

        return ApiResponse.success("Lấy danh sách xe khách thành công", response);
    }
}
