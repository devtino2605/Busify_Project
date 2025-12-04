package com.busify.project.cargo.service.impl;

import com.busify.project.cargo.dto.request.CargoCancelRequestDTO;
import com.busify.project.cargo.dto.request.CargoBookingRequestDTO;
import com.busify.project.cargo.dto.request.CargoFeeCalculationRequestDTO;
import com.busify.project.cargo.dto.request.CargoPaymentRequestDTO;
import com.busify.project.cargo.dto.request.CargoQRVerifyRequestDTO;
import com.busify.project.cargo.dto.request.CargoSearchRequestDTO;
import com.busify.project.cargo.dto.request.CargoUpdateStatusRequestDTO;
import com.busify.project.cargo.dto.response.*;
import com.busify.project.cargo.entity.CargoBooking;
import com.busify.project.cargo.entity.CargoImage;
import com.busify.project.cargo.entity.CargoTracking;
import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.cargo.enums.ImageType;
import com.busify.project.cargo.exception.CargoBookingException;
import com.busify.project.cargo.exception.CargoNotFoundException;
import com.busify.project.cargo.exception.CargoWeightExceededException;
import com.busify.project.cargo.exception.InvalidCargoStatusTransitionException;
import com.busify.project.cargo.mapper.CargoMapper;
import com.busify.project.cargo.repository.CargoBookingRepository;
import com.busify.project.cargo.repository.CargoImageRepository;
import com.busify.project.cargo.repository.CargoTrackingRepository;
import com.busify.project.cargo.service.CargoService;
import com.busify.project.common.service.CloudinaryService;
import com.busify.project.common.utils.PdfGeneratorUtil;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.location.entity.Location;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import com.busify.project.location.repository.LocationRepository;
import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.repository.PaymentRepository;
import com.busify.project.payment.strategy.PaymentStrategy;
import com.busify.project.payment.strategy.PaymentStrategyFactory;
import com.busify.project.refund.dto.request.RefundRequestDTO;
import com.busify.project.refund.service.RefundService;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.enums.TripStatus;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CargoServiceImpl
 * 
 * Implementation of CargoService with full business logic
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {

    // ===== REPOSITORIES =====
    private final CargoBookingRepository cargoBookingRepository;
    private final CargoTrackingRepository cargoTrackingRepository;
    private final CargoImageRepository cargoImageRepository;
    private final TripRepository tripRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    // ===== SERVICES & MAPPERS =====
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final RefundService refundService;
    private final CargoMapper cargoMapper;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final com.busify.project.auth.service.EmailService emailService;
    private final JwtUtils jwtUtils;
    private final com.busify.project.cargo.config.CargoFeeConfig cargoFeeConfig;

    // ===== CORE CARGO BOOKING OPERATIONS =====

    @Override
    @Transactional
    public CargoBookingResponseDTO createCargoBooking(CargoBookingRequestDTO request) {
        return createCargoBooking(request, null);
    }

    @Override
    @Transactional
    public CargoBookingResponseDTO createCargoBooking(CargoBookingRequestDTO request, List<MultipartFile> images) {
        // Get current user ID from UserService (nullable for guest users)
        Long userId = getCurrentUserId();

        log.info("Creating cargo booking for trip: {}, user: {}, with {} images",
                request.getTripId(), userId, images != null ? images.size() : 0);

        // 1. Validate trip
        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> CargoBookingException.failed("Trip not found with ID: " + request.getTripId()));

        validateTripForCargo(trip);

        // 2. Validate locations
        Location pickupLocation = locationRepository.findById(request.getPickupLocationId())
                .orElseThrow(() -> CargoBookingException.failed("Pickup location not found"));

        Location dropoffLocation = locationRepository.findById(request.getDropoffLocationId())
                .orElseThrow(() -> CargoBookingException.failed("Dropoff location not found"));

        // 3. Check cargo weight capacity
        BigDecimal maxCapacity = getMaxCargoWeightForTrip(trip);
        Double currentWeight = getCurrentCargoWeight(trip.getId());
        Double newTotalWeight = currentWeight + request.getWeight().doubleValue();

        if (newTotalWeight > maxCapacity.doubleValue()) {
            throw CargoWeightExceededException.exceeded(
                    trip.getId(),
                    request.getWeight(),
                    BigDecimal.valueOf(maxCapacity.doubleValue() - currentWeight));
        }

        // 4. Get user if provided
        User bookingUser = null;
        if (userId != null) {
            bookingUser = userRepository.findById(userId).orElse(null);
        }

        // 5. Calculate fees using simplified DTO
        CargoFeeCalculationRequestDTO feeRequest = CargoFeeCalculationRequestDTO.builder()
                .tripId(request.getTripId())
                .pickupLocationId(request.getPickupLocationId())
                .dropoffLocationId(request.getDropoffLocationId())
                .weight(request.getWeight())
                .declaredValue(request.getDeclaredValue())
                .build();
        CargoFeeCalculationResponseDTO feeCalculation = calculateCargoFee(feeRequest);

        // 6. Generate unique cargo code
        String cargoCode = generateCargoCode();

        // 7. Create cargo booking entity
        CargoBooking cargoBooking = CargoBooking.builder()
                .cargoCode(cargoCode)
                .trip(trip)
                .bookingUser(bookingUser)
                .pickupLocation(pickupLocation)
                .dropoffLocation(dropoffLocation)
                .senderName(request.getSenderName())
                .senderPhone(request.getSenderPhone())
                .senderEmail(request.getSenderEmail())
                .senderAddress(request.getSenderAddress())
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .receiverEmail(request.getReceiverEmail())
                .receiverAddress(request.getReceiverAddress())
                .cargoType(request.getCargoType())
                .description(request.getDescription())
                .weight(request.getWeight())
                .dimensions(request.getDimensions())
                .declaredValue(request.getDeclaredValue())
                .cargoFee(feeCalculation.getCargoFee())
                .insuranceFee(feeCalculation.getInsuranceFee())
                .totalAmount(feeCalculation.getTotalAmount())
                .specialInstructions(request.getSpecialInstructions())
                .status(CargoStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        cargoBooking = cargoBookingRepository.save(cargoBooking);

        // 8. Upload cargo images if provided
        if (images != null && !images.isEmpty()) {
            log.info("Uploading {} images for cargo: {}", images.size(), cargoCode);
            for (MultipartFile imageFile : images) {
                if (!imageFile.isEmpty()) {
                    try {
                        // Auto-detect image type based on filename or use PICKUP as default
                        ImageType imageType = ImageType.PACKAGE;
                        String folder = "cargo/" + cargoCode + "/" + imageType.name().toLowerCase();
                        String imageUrl = cloudinaryService.uploadFile(imageFile, folder);

                        // Save image to database
                        CargoImage cargoImage = new CargoImage(
                                cargoBooking,
                                imageUrl,
                                imageType,
                                "Image uploaded during booking creation");
                        cargoImageRepository.save(cargoImage);

                        log.info("Uploaded cargo image: {} - {}", imageType, imageUrl);
                    } catch (Exception e) {
                        log.error("Failed to upload cargo image: {}", imageFile.getOriginalFilename(), e);
                        // Continue with other images, don't fail entire booking
                    }
                }
            }
        }

        // 9. Create initial tracking record
        createTrackingRecord(cargoBooking, CargoStatus.PENDING,
                pickupLocation.getCity(), "Cargo booking created", userId);

        log.info("Cargo booking created successfully with code: {}", cargoCode);

        return cargoMapper.toCargoBookingResponse(cargoBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public CargoDetailResponseDTO getCargoBookingByCode(String cargoCode) {
        CargoBooking cargoBooking = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        return cargoMapper.toCargoDetailResponse(cargoBooking);
    }

    @Override
    @Transactional
    public CargoBookingResponseDTO updateCargoStatus(String cargoCode, CargoUpdateStatusRequestDTO request) {
        // Get current user ID from UserService
        Long updatedByUserId = getCurrentUserId();

        CargoBooking cargoBooking = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        CargoStatus newStatus = request.getStatus();
        validateStatusTransition(cargoBooking.getStatus(), newStatus);

        // Validate rejection reason BEFORE updating status
        if (newStatus == CargoStatus.REJECTED) {
            String rejectionReason = request.getNotes();
            if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
                throw CargoBookingException.failed("Rejection reason is required when rejecting cargo");
            }
            // Set rejection reason before save
            cargoBooking.setRejectionReason(rejectionReason);
        }

        // Update status and timestamps
        cargoBooking.setStatus(newStatus);
        cargoBooking.setUpdatedAt(LocalDateTime.now());
        updateStatusTimestamps(cargoBooking, newStatus);

        // Save once with all fields set
        cargoBooking = cargoBookingRepository.save(cargoBooking);

        // Create tracking record
        createTrackingRecord(cargoBooking, newStatus, request.getLocation(),
                request.getNotes(), updatedByUserId);

        // Handle REJECTED status - process refund and send email
        if (newStatus == CargoStatus.REJECTED) {
            // Process 100% refund for rejected cargo (bypass policy - always 100% refund)
            updatePaymentOnRejection(cargoBooking);

            // Send rejection email to sender
            if (cargoBooking.getSenderEmail() != null) {
                try {
                    // Eager fetch all lazy-loaded relationships BEFORE async email call
                    // to prevent LazyInitializationException in async thread
                    Hibernate.initialize(cargoBooking.getTrip());
                    Hibernate.initialize(cargoBooking.getTrip().getBus());
                    if (cargoBooking.getTrip().getBus() != null) {
                        Hibernate.initialize(cargoBooking.getTrip().getBus().getOperator());
                    }
                    Hibernate.initialize(cargoBooking.getPickupLocation());
                    Hibernate.initialize(cargoBooking.getDropoffLocation());

                    emailService.sendCargoRejectionEmail(cargoBooking, cargoBooking.getRejectionReason());
                    log.info("Cargo rejection email sent successfully to: {}", cargoBooking.getSenderEmail());
                } catch (Exception e) {
                    log.error("Failed to send cargo rejection email for {}: {}",
                            cargoCode, e.getMessage(), e);
                    // Don't fail the status update if email fails
                }
            }

            log.info("Cargo {} rejected by STAFF. Reason: {}. Refund processed.",
                    cargoCode, cargoBooking.getRejectionReason());
        }

        // Send confirmation email when cargo is CONFIRMED (after inspection)
        if (newStatus == CargoStatus.CONFIRMED && cargoBooking.getSenderEmail() != null) {
            try {
                byte[] pdfBytes = PdfGeneratorUtil.generateCargoPDF(cargoBooking);
                emailService.sendCargoBookingConfirmationEmail(cargoBooking, pdfBytes);
                log.info("Cargo confirmation email sent successfully for: {}", cargoCode);
            } catch (Exception e) {
                log.error("Failed to send cargo confirmation email for {}: {}",
                        cargoCode, e.getMessage(), e);
                // Don't fail the status update if email fails
            }
        }

        log.info("Cargo status updated successfully: {}", cargoCode);

        return cargoMapper.toCargoBookingResponse(cargoBooking);
    }

    /**
     * Generate unique cargo code with format: CRG-YYYYMMDD-XXXX
     */
    private String generateCargoCode() {
        String datePart = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
        return "CRG-" + datePart + "-" + randomPart;
    }

    @Override
    @Transactional
    public CargoBookingResponseDTO cancelCargoBooking(String cargoCode, CargoCancelRequestDTO request) {
        // Get current user ID from UserService
        Long userId = getCurrentUserId();

        log.info("Cancelling cargo booking: {}", cargoCode);

        CargoBooking cargoBooking = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        if (!cargoBooking.getStatus().canBeCancelled()) {
            throw CargoBookingException.failed(
                    String.format("Cannot cancel cargo in status: %s", cargoBooking.getStatus()));
        }

        // Check cancellation time policy
        LocalDateTime departureTime = cargoBooking.getTrip().getDepartureTime();
        long hoursUntilDeparture = java.time.Duration.between(LocalDateTime.now(), departureTime).toHours();

        if (hoursUntilDeparture < cargoFeeConfig.getCancellationHoursBeforeDeparture()) {
            throw CargoBookingException.failed(
                    String.format("Cannot cancel cargo less than %d hours before departure",
                            cargoFeeConfig.getCancellationHoursBeforeDeparture()));
        }

        // Log refund policy information
        String refundInfo;
        if (hoursUntilDeparture > 24) {
            refundInfo = "100% refund (no cancellation fee)";
        } else {
            refundInfo = "70% refund (30% cancellation fee)";
        }
        log.info("Cargo {} cancellation: {} hours until departure - Expected refund: {}",
                cargoCode, hoursUntilDeparture, refundInfo);

        // Update status
        cargoBooking.setStatus(CargoStatus.CANCELLED);
        cargoBooking.setCancelledAt(LocalDateTime.now());
        cargoBooking.setCancellationReason(request.getReason());
        cargoBooking.setUpdatedAt(LocalDateTime.now());

        cargoBooking = cargoBookingRepository.save(cargoBooking);

        // Create tracking record
        createTrackingRecord(cargoBooking, CargoStatus.CANCELLED,
                null, "Cancelled: " + request.getReason(), userId);

        // Update payment status
        updatePaymentOnCancellation(cargoBooking);

        log.info("Cargo cancelled successfully: {}", cargoCode);

        return cargoMapper.toCargoBookingResponse(cargoBooking);
    }

    // ===== CARGO SEARCH AND LISTING =====

    @Override
    @Transactional(readOnly = true)
    public Page<CargoListResponseDTO> getCargoByUserId(Long userId, Pageable pageable) {
        Page<CargoBooking> cargoPage = cargoBookingRepository.findByBookingUserId(userId, pageable);
        return cargoPage.map(cargoMapper::toCargoListResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CargoListResponseDTO> getMyCargoBookings(Pageable pageable) {
        // Get current user ID from UserService
        Long userId = getCurrentUserId();
        if (userId == null) {
            throw CargoBookingException.failed("User not authenticated");
        }
        return getCargoByUserId(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CargoListResponseDTO> searchCargo(CargoSearchRequestDTO request, Pageable pageable) {
        // Use repository's search method with all filters
        Page<CargoBooking> cargoPage = cargoBookingRepository.searchCargo(
                request.getKeyword(),
                request.getTripId(),
                request.getBookingUserId(),
                request.getStatus(),
                request.getCargoType(),
                null, // senderPhone - using keyword instead
                null, // receiverPhone - using keyword instead
                request.getCreatedFrom(),
                request.getCreatedTo(),
                pageable);

        return cargoPage.map(cargoMapper::toCargoListResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CargoListResponseDTO> getCargoByTrip(Long tripId, Pageable pageable) {
        Page<CargoBooking> cargoPage = cargoBookingRepository.findByTripId(tripId, pageable);
        return cargoPage.map(cargoMapper::toCargoListResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CargoListResponseDTO> getActiveCargoByTrip(Long tripId) {
        List<CargoBooking> activeCargos = cargoBookingRepository.findActiveCargoByTripId(tripId);
        return activeCargos.stream()
                .map(cargoMapper::toCargoListResponse)
                .collect(Collectors.toList());
    }

    // ===== FEE CALCULATION =====

    @Override
    @Transactional(readOnly = true)
    public CargoFeeCalculationResponseDTO calculateCargoFee(CargoFeeCalculationRequestDTO request) {
        // 1. Validate trip exists
        tripRepository.findById(request.getTripId())
                .orElseThrow(() -> CargoBookingException.failed("Trip not found"));

        // 2. Calculate distance between pickup and dropoff
        Double distance = calculateDistance(request.getPickupLocationId(), request.getDropoffLocationId());

        // 3. Calculate distance fee = distance × FEE_PER_KM
        BigDecimal distanceFee = cargoFeeConfig.getFeePerKm().multiply(BigDecimal.valueOf(distance));

        // 4. Calculate weight fee = weight × FEE_PER_KG
        BigDecimal weightFee = cargoFeeConfig.getFeePerKg().multiply(request.getWeight());

        // 5. Calculate cargo fee = BASE_FEE + distanceFee + weightFee
        BigDecimal cargoFee = cargoFeeConfig.getBaseFee().add(distanceFee).add(weightFee);

        // 6. Calculate insurance fee (if declared value provided)
        BigDecimal insuranceFee = BigDecimal.ZERO;
        if (request.getDeclaredValue() != null && request.getDeclaredValue().compareTo(BigDecimal.ZERO) > 0) {
            // Insurance = declared value × rate (0.5%)
            insuranceFee = request.getDeclaredValue()
                    .multiply(BigDecimal.valueOf(cargoFeeConfig.getInsuranceRate()));

            // Apply minimum insurance fee
            if (insuranceFee.compareTo(cargoFeeConfig.getMinInsuranceFee()) < 0) {
                insuranceFee = cargoFeeConfig.getMinInsuranceFee();
            }
        }

        // 7. Calculate total amount
        BigDecimal totalAmount = cargoFee.add(insuranceFee);

        // Round to nearest thousand (Vietnamese pricing convention)
        cargoFee = cargoFee.setScale(0, RoundingMode.HALF_UP);
        insuranceFee = insuranceFee.setScale(0, RoundingMode.HALF_UP);
        totalAmount = totalAmount.setScale(0, RoundingMode.HALF_UP);

        // 8. Build breakdown message for transparency
        String distanceFeeBreakdown = String.format("%.0f km × %,d VND/km = %,d VND",
                distance, cargoFeeConfig.getFeePerKm().intValue(), distanceFee.intValue());
        String weightFeeBreakdown = String.format("%.1f kg × %,d VND/kg = %,d VND",
                request.getWeight().doubleValue(), cargoFeeConfig.getFeePerKg().intValue(), weightFee.intValue());
        String cargoFeeBreakdown = String.format("%,d + %,d + %,d = %,d VND",
                cargoFeeConfig.getBaseFee().intValue(), distanceFee.intValue(), weightFee.intValue(),
                cargoFee.intValue());
        String insuranceBreakdown = request.getDeclaredValue() != null && insuranceFee.compareTo(BigDecimal.ZERO) > 0
                ? String.format("%,d × %.1f%% = %,d VND (min: %,d VND)",
                        request.getDeclaredValue().intValue(), cargoFeeConfig.getInsuranceRate() * 100,
                        insuranceFee.intValue(), cargoFeeConfig.getMinInsuranceFee().intValue())
                : "0 VND (no insurance)";

        return CargoFeeCalculationResponseDTO.builder()
                .baseFee(cargoFeeConfig.getBaseFee())
                .weightFee(weightFee.setScale(0, RoundingMode.HALF_UP))
                .cargoTypeFee(BigDecimal.ZERO)
                .distanceFee(distanceFee.setScale(0, RoundingMode.HALF_UP))
                .cargoFee(cargoFee)
                .insuranceFee(insuranceFee)
                .totalAmount(totalAmount)
                .distance(distance)
                .weight(request.getWeight())
                .cargoType(null) // Not required for simple fee calculation
                .typeMultiplier(null)
                .declaredValue(request.getDeclaredValue())
                .breakdown(CargoFeeCalculationResponseDTO.FeeBreakdown.builder()
                        .distanceFee(distanceFeeBreakdown)
                        .weightFee(weightFeeBreakdown)
                        .cargoFee(cargoFeeBreakdown)
                        .insurance(insuranceBreakdown)
                        .build())
                .build();
    }

    // ===== CARGO CAPACITY VALIDATION =====

    @Override
    @Transactional(readOnly = true)
    public Double getCurrentCargoWeight(Long tripId) {
        BigDecimal totalWeight = cargoBookingRepository.calculateTotalCargoWeight(tripId);
        return totalWeight != null ? totalWeight.doubleValue() : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getRemainingCargoCapacity(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found with ID: " + tripId));

        BigDecimal maxCapacity = getMaxCargoWeightForTrip(trip);
        Double currentWeight = getCurrentCargoWeight(tripId);
        return maxCapacity.doubleValue() - currentWeight;
    }

    /**
     * Get maximum cargo weight capacity for a specific trip based on bus model
     * 
     * @param trip The trip to get max capacity for
     * @return Max cargo weight in kg (from bus model or default 500kg)
     */
    private BigDecimal getMaxCargoWeightForTrip(Trip trip) {
        if (trip.getBus() != null && trip.getBus().getModel() != null) {
            return trip.getBus().getModel().getMaxCargoWeightOrDefault();
        }

        // Fallback to default if bus or model is not set
        log.warn("Trip {} has no bus model configured, using default cargo capacity: {} kg",
                trip.getId(), cargoFeeConfig.getDefaultMaxCargoWeight());
        return cargoFeeConfig.getDefaultMaxCargoWeight();
    }

    // ===== CARGO TRACKING =====

    @Override
    @Transactional(readOnly = true)
    public List<CargoTrackingResponseDTO> getTrackingHistory(String cargoCode) {
        CargoBooking cargoBooking = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        List<CargoTracking> trackingList = cargoTrackingRepository
                .findByCargoBookingCargoBookingIdOrderByCreatedAtDesc(cargoBooking.getCargoBookingId());

        return trackingList.stream()
                .map(cargoMapper::toCargoTrackingResponse)
                .collect(Collectors.toList());
    }

    // ===== CARGO IMAGES =====

    @Override
    @Transactional
    public String uploadCargoImage(String cargoCode, MultipartFile imageFile, ImageType imageType, String description) {
        CargoBooking cargoBooking = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        try {
            // Upload to Cloudinary with folder structure: cargo/{cargoCode}/{imageType}
            String folder = "cargo/" + cargoCode + "/" + imageType.name().toLowerCase();
            String imageUrl = cloudinaryService.uploadFile(imageFile, folder);

            // Save to database
            CargoImage cargoImage = new CargoImage(cargoBooking, imageUrl, imageType, description);
            cargoImageRepository.save(cargoImage);

            log.info("Cargo image uploaded successfully: {} for cargo: {}", imageType, cargoCode);

            return imageUrl;
        } catch (Exception e) {
            log.error("Failed to upload cargo image for cargo: {}", cargoCode, e);
            throw CargoBookingException.failed("Failed to upload cargo image: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CargoDetailResponseDTO.ImageInfo> getCargoImages(String cargoCode) {
        CargoBooking cargoBooking = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        List<CargoImage> images = cargoImageRepository
                .findByCargoBookingCargoBookingId(cargoBooking.getCargoBookingId());

        return images.stream()
                .map(img -> CargoDetailResponseDTO.ImageInfo.builder()
                        .imageId(img.getImageId())
                        .imageUrl(img.getImageUrl())
                        .imageType(img.getImageType().name())
                        .description(img.getDescription())
                        .uploadedAt(img.getUploadedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCargoImage(String cargoCode, Long imageId) {
        // Validate cargo exists
        cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        // Get image
        CargoImage cargoImage = cargoImageRepository.findById(imageId)
                .orElseThrow(() -> CargoBookingException.failed("Image not found with ID: " + imageId));

        try {
            // Delete from Cloudinary
            String publicId = cloudinaryService.extractPublicId(cargoImage.getImageUrl());
            cloudinaryService.deleteFile(publicId);

            // Delete from database
            cargoImageRepository.deleteById(imageId);

            log.info("Cargo image deleted: {} for cargo: {}", imageId, cargoCode);
        } catch (Exception e) {
            log.error("Failed to delete cargo image: {}", imageId, e);
            throw CargoBookingException.failed("Failed to delete cargo image: " + e.getMessage(), e);
        }
    }

    // ===== STATISTICS & REPORTING =====

    @Override
    @Transactional(readOnly = true)
    public CargoStatisticsResponseDTO getCargoStatistics(Long operatorId) {
        log.info("Getting cargo statistics for operator: {}", operatorId);

        // Get status counts based on operator filter
        List<Object[]> statusCounts;
        if (operatorId != null) {
            // Get statistics for specific operator
            statusCounts = cargoBookingRepository.countCargoByStatusForOperator(operatorId);
        } else {
            // Get statistics for all operators (admin view)
            statusCounts = cargoBookingRepository.countCargoByStatus();
        }

        Map<CargoStatus, Long> countMap = new HashMap<>();
        for (Object[] row : statusCounts) {
            CargoStatus status = (CargoStatus) row[0];
            Long count = (Long) row[1];
            countMap.put(status, count);
        }

        return CargoStatisticsResponseDTO.builder()
                .cargoCountByStatus(countMap)
                .pendingCargo(countMap.getOrDefault(CargoStatus.PENDING, 0L))
                .confirmedCargo(countMap.getOrDefault(CargoStatus.CONFIRMED, 0L))
                .inTransitCargo(countMap.getOrDefault(CargoStatus.IN_TRANSIT, 0L))
                .deliveredCargo(countMap.getOrDefault(CargoStatus.DELIVERED, 0L))
                .cancelledCargo(countMap.getOrDefault(CargoStatus.CANCELLED, 0L))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getCargoRevenueByTrip(Long tripId) {
        BigDecimal revenue = cargoBookingRepository.calculateCargoRevenue(tripId);
        return revenue != null ? revenue.doubleValue() : 0.0;
    }

    // ===== ADMIN OPERATIONS =====

    @Override
    @Transactional
    public int autoCancelCargoByTrip(Long tripId, String cancellationReason) {
        log.info("Auto-cancelling cargo bookings for trip: {} with reason: {}", tripId, cancellationReason);

        List<CargoBooking> cargoList = cargoBookingRepository.findActiveCargoByTripId(tripId);

        int cancelledCount = 0;
        int refundedCount = 0;

        for (CargoBooking cargo : cargoList) {
            if (cargo.getStatus().canBeCancelled()) {
                // Update cargo status
                cargo.setStatus(CargoStatus.CANCELLED);
                cargo.setCancelledAt(LocalDateTime.now());
                // Use the actual trip cancellation reason instead of generic message
                cargo.setCancellationReason(cancellationReason);
                cargo.setUpdatedAt(LocalDateTime.now());
                cargoBookingRepository.save(cargo);

                // Add tracking record with actual reason
                createTrackingRecord(cargo, CargoStatus.CANCELLED,
                        null, "Chuyến xe bị hủy: " + cancellationReason, null);

                // Process 100% refund (bypass policy because trip cancellation is not
                // customer's fault)
                processFullRefundForTripCancellation(cargo, cancellationReason);
                refundedCount++;

                cancelledCount++;
            }
        }

        log.info("Auto-cancelled {} cargo bookings for trip: {} ({} full refunds processed)",
                cancelledCount, tripId, refundedCount);
        return cancelledCount;
    }

    /**
     * Process 100% refund when cargo is cancelled due to TRIP CANCELLATION
     * 
     * TRIP CANCELLATION REFUND POLICY:
     * - Always 100% refund regardless of departure time
     * - Bypass normal time-based refund policy
     * - Reason: Trip was cancelled by operator, not customer's fault
     * 
     * @param cargoBooking           The cargo booking being cancelled
     * @param tripCancellationReason The reason why the trip was cancelled
     */
    private void processFullRefundForTripCancellation(CargoBooking cargoBooking, String tripCancellationReason) {
        try {
            Payment payment = paymentRepository.findByCargoBookingId(cargoBooking.getCargoBookingId())
                    .orElse(null);

            // Kiểm tra có payment và đã completed chưa
            if (payment != null && payment.getStatus() == PaymentStatus.completed) {
                log.info("Processing 100% refund for cargo: {} due to trip cancellation", cargoBooking.getCargoCode());

                // Tạo refund request with bypassPolicy = true (100% refund - no cancellation
                // fee)
                RefundRequestDTO refundRequest = new RefundRequestDTO();
                refundRequest.setPaymentId(payment.getPaymentId());
                refundRequest.setRefundReason(tripCancellationReason); // Use actual trip cancellation reason
                refundRequest.setNotes("Hoàn tiền 100% do chuyến xe bị hủy: " + cargoBooking.getCargoCode());
                refundRequest.setBypassPolicy(true); // Bypass time-based policy - always 100% refund

                // Tạo refund (100% refund regardless of time)
                refundService.createRefund(refundRequest);

                log.info("100% refund request created successfully for cargo: {} due to trip cancellation",
                        cargoBooking.getCargoCode());
            } else {
                log.info("No refund needed for cargo: {} (no payment or payment not completed)",
                        cargoBooking.getCargoCode());
            }
        } catch (Exception e) {
            log.error("Error processing refund for cargo: {}", cargoBooking.getCargoCode(), e);
            // Không throw exception để không ảnh hưởng đến việc cancel cargo
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCargoCustomerEmailsByTripId(Long tripId) {
        log.info("Getting cargo customer emails for trip: {}", tripId);

        List<CargoBooking> activeCargoList = cargoBookingRepository.findActiveCargoByTripId(tripId);

        return activeCargoList.stream()
                .map(CargoBooking::getSenderEmail)
                .filter(email -> email != null && !email.isBlank())
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public Map<String, Integer> autoProcessCargoWhenTripDeparted(Long tripId) {
        log.info("Auto-processing cargo bookings when trip {} departed", tripId);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found: " + tripId));

        // Eager load relationships needed for email
        Hibernate.initialize(trip.getBus());
        if (trip.getBus() != null) {
            Hibernate.initialize(trip.getBus().getOperator());
        }
        Hibernate.initialize(trip.getRoute());
        if (trip.getRoute() != null) {
            Hibernate.initialize(trip.getRoute().getStartLocation());
            Hibernate.initialize(trip.getRoute().getEndLocation());
        }

        List<CargoBooking> allCargo = cargoBookingRepository.findByTripId(tripId);

        int inTransitCount = 0;
        int cancelledCount = 0;

        for (CargoBooking cargo : allCargo) {
            CargoStatus currentStatus = cargo.getStatus();

            // 1. PICKED_UP → IN_TRANSIT
            if (currentStatus == CargoStatus.PICKED_UP) {
                cargo.setStatus(CargoStatus.IN_TRANSIT);
                cargo.setUpdatedAt(LocalDateTime.now());
                cargoBookingRepository.save(cargo);

                createTrackingRecord(cargo, CargoStatus.IN_TRANSIT,
                        null, "Xe đã khởi hành, hàng đang vận chuyển", null);

                inTransitCount++;
                log.info("Auto-updated cargo {} from PICKED_UP to IN_TRANSIT", cargo.getCargoCode());
            }
            // 2. PENDING or CONFIRMED (not picked up) → CANCELLED
            else if (currentStatus == CargoStatus.PENDING || currentStatus == CargoStatus.CONFIRMED) {
                cargo.setStatus(CargoStatus.CANCELLED);
                cargo.setCancelledAt(LocalDateTime.now());
                cargo.setCancellationReason("Không lấy hàng trước khi xe khởi hành");
                cargo.setUpdatedAt(LocalDateTime.now());
                cargoBookingRepository.save(cargo);

                createTrackingRecord(cargo, CargoStatus.CANCELLED,
                        null, "Tự động hủy do không lấy hàng trước khi xe khởi hành", null);

                // Process refund if payment exists
                updatePaymentOnCancellation(cargo);

                // Send cancellation email asynchronously
                sendCargoCancellationEmail(cargo, trip);

                cancelledCount++;
                log.info("Auto-cancelled cargo {} (status: {}) - not picked up before departure",
                        cargo.getCargoCode(), currentStatus);
            }
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("inTransit", inTransitCount);
        result.put("cancelled", cancelledCount);

        log.info("Auto-processing completed for trip {}: {} cargo → IN_TRANSIT, {} cargo → CANCELLED",
                tripId, inTransitCount, cancelledCount);

        return result;
    }

    @Override
    @Transactional
    public Map<String, Integer> autoProcessCargoWhenTripArrived(Long tripId) {
        log.info("Auto-processing cargo bookings when trip {} arrived at destination", tripId);

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found: " + tripId));

        List<CargoBooking> allCargo = cargoBookingRepository.findByTripId(tripId);

        int arrivedCount = 0;

        for (CargoBooking cargo : allCargo) {
            CargoStatus currentStatus = cargo.getStatus();

            // IN_TRANSIT → ARRIVED (khi xe đến nơi)
            if (currentStatus == CargoStatus.IN_TRANSIT) {
                cargo.setStatus(CargoStatus.ARRIVED);
                cargo.setUpdatedAt(LocalDateTime.now());
                cargoBookingRepository.save(cargo);

                // Get destination location from route
                String destinationCity = trip.getRoute() != null && trip.getRoute().getEndLocation() != null
                        ? trip.getRoute().getEndLocation().getCity()
                        : null;

                createTrackingRecord(cargo, CargoStatus.ARRIVED,
                        destinationCity, "Xe đã đến nơi, hàng sẵn sàng để giao", null);

                arrivedCount++;
                log.info("Auto-updated cargo {} from IN_TRANSIT to ARRIVED", cargo.getCargoCode());

                // Send arrival notification email to receiver (optional)
                sendCargoArrivalEmail(cargo, trip);
            }
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("arrived", arrivedCount);

        log.info("Auto-processing completed for trip {}: {} cargo → ARRIVED",
                tripId, arrivedCount);

        return result;
    }

    /**
     * Send cargo arrival notification email to receiver
     * Notifies receiver that cargo has arrived and ready for pickup/delivery
     * Includes QR code for verification at pickup
     */
    private void sendCargoArrivalEmail(CargoBooking cargo, Trip trip) {
        try {
            if (cargo.getReceiverEmail() != null) {
                // Eager fetch all lazy-loaded relationships
                Hibernate.initialize(cargo.getTrip());
                Hibernate.initialize(cargo.getPickupLocation());
                Hibernate.initialize(cargo.getDropoffLocation());

                // Generate pickup token (JWT) valid for 7 days
                String pickupToken = generateCargoPickupToken(cargo);

                // Save token to database
                cargo.setPickupToken(pickupToken);
                cargo.setTokenExpiresAt(LocalDateTime.now().plusDays(7));
                cargoBookingRepository.save(cargo);

                // Send email with QR code
                emailService.sendCargoArrivalEmailWithQR(cargo, trip, pickupToken);

                log.info("Sent cargo arrival email with QR code for: {}", cargo.getCargoCode());
            }
        } catch (Exception e) {
            log.error("Failed to send cargo arrival email for {}: {}",
                    cargo.getCargoCode(), e.getMessage());
            // Don't fail the status update if email fails
        }
    }

    /**
     * Generate JWT token for cargo pickup verification
     * Token contains cargo code and receiver info, valid for 7 days
     * 
     * @param cargo CargoBooking entity
     * @return JWT token string
     */
    private String generateCargoPickupToken(CargoBooking cargo) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("cargoCode", cargo.getCargoCode());
            claims.put("receiverPhone", cargo.getReceiverPhone());
            claims.put("receiverName", cargo.getReceiverName());
            claims.put("cargoBookingId", cargo.getCargoBookingId());

            LocalDateTime expiryDate = LocalDateTime.now().plusDays(7);
            Date expirationDate = java.sql.Timestamp.valueOf(expiryDate);

            String token = jwtUtils.generateCustomToken(
                    "cargo-pickup-" + cargo.getCargoCode(),
                    claims,
                    expirationDate);

            log.debug("Generated pickup token for cargo: {}", cargo.getCargoCode());
            return token;

        } catch (Exception e) {
            log.error("Error generating pickup token for cargo: {}", cargo.getCargoCode(), e);
            throw CargoBookingException.failed("Failed to generate pickup token", e);
        }
    }

    /**
     * Confirm cargo pickup by QR code verification
     * Validates JWT token from QR and updates cargo status to DELIVERED
     * 
     * @param request CargoQRVerifyRequestDTO with QR token and staff ID
     * @return CargoQRVerifyResponseDTO with verification result
     */
    @Override
    @Transactional
    public CargoQRVerifyResponseDTO confirmCargoPickupByQR(CargoQRVerifyRequestDTO request) {
        // Get current staff user from authentication
        Long staffId = getCurrentUserId();
        if (staffId == null) {
            throw CargoBookingException.failed("Staff not authenticated");
        }

        try {
            // 1. Decode and validate JWT token using JwtUtils
            Claims claims = jwtUtils.validateCustomToken(request.getQrToken());

            String cargoCode = claims.get("cargoCode", String.class);
            log.info("QR token validated for cargo: {} by staff: {}", cargoCode, staffId);

            // 2. Find cargo booking
            CargoBooking cargo = cargoBookingRepository.findByCargoCode(cargoCode)
                    .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

            // 3. Validate cargo status
            if (cargo.getStatus() != CargoStatus.ARRIVED) {
                throw CargoBookingException.failed(
                        String.format("Hàng chưa đến nơi. Trạng thái hiện tại: %s",
                                cargo.getStatus().getDisplayName()));
            }

            // 4. Validate token hasn't expired in DB
            if (cargo.getTokenExpiresAt() != null && LocalDateTime.now().isAfter(cargo.getTokenExpiresAt())) {
                throw CargoBookingException.failed("Mã QR đã hết hạn (quá 7 ngày)");
            }

            // 5. Update cargo status to DELIVERED
            cargo.setStatus(CargoStatus.DELIVERED);
            cargo.setDeliveredAt(LocalDateTime.now());
            cargo.setConfirmedByStaffId(staffId);
            cargo.setUpdatedAt(LocalDateTime.now());
            cargoBookingRepository.save(cargo);

            // 7. Create tracking record
            createTrackingRecord(cargo, CargoStatus.DELIVERED,
                    cargo.getDropoffLocation().getCity(),
                    "Hàng đã được giao cho người nhận - Xác thực bằng QR code",
                    staffId);

            log.info("Cargo {} delivered successfully via QR verification by staff {}",
                    cargoCode, staffId);

            // 6. Build response
            return CargoQRVerifyResponseDTO.builder()
                    .success(true)
                    .message("Đã giao hàng thành công")
                    .cargoCode(cargo.getCargoCode())
                    .senderName(cargo.getSenderName())
                    .senderPhone(cargo.getSenderPhone())
                    .receiverName(cargo.getReceiverName())
                    .receiverPhone(cargo.getReceiverPhone())
                    .cargoType(cargo.getCargoType().name())
                    .weight(cargo.getWeight().doubleValue())
                    .description(cargo.getDescription())
                    .confirmedByStaffId(staffId)
                    .deliveredAt(cargo.getDeliveredAt())
                    .tripId(cargo.getTrip().getId())
                    .routeName(cargo.getTrip().getRoute().getStartLocation().getCity() + " - " +
                            cargo.getTrip().getRoute().getEndLocation().getCity())
                    .build();

        } catch (ExpiredJwtException e) {
            log.error("QR token expired: {}", e.getMessage());
            throw CargoBookingException.failed("Mã QR đã hết hạn");
        }
    }

    /**
     * Send cargo cancellation email asynchronously
     * Uses existing sendCargoRejectionEmail method with cancellation reason
     */
    private void sendCargoCancellationEmail(CargoBooking cargo, Trip trip) {
        try {
            String cancellationMessage = String.format(
                    "Hàng hóa đã bị hủy tự động do không được lấy trước khi xe khởi hành.\n" +
                            "Tuyến đường: %s → %s\n" +
                            "Thời gian khởi hành: %s\n" +
                            "Lý do: %s",
                    trip.getRoute().getStartLocation().getCity(),
                    trip.getRoute().getEndLocation().getCity(),
                    trip.getDepartureTime(),
                    cargo.getCancellationReason());

            // Use existing rejection email method (works for cancellation too)
            emailService.sendCargoRejectionEmail(cargo, cancellationMessage);

            log.info("Sent cargo cancellation email for: {}", cargo.getCargoCode());
        } catch (Exception e) {
            log.error("Failed to send cargo cancellation email for {}: {}",
                    cargo.getCargoCode(), e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateInternalNotes(String cargoCode, String internalNotes) {
        // Get current user ID from UserService (for audit trail if needed)
        Long updatedByUserId = getCurrentUserId();

        CargoBooking cargoBooking = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        cargoBooking.setInternalNotes(internalNotes);
        cargoBooking.setUpdatedAt(LocalDateTime.now());
        cargoBookingRepository.save(cargoBooking);

        log.info("Internal notes updated for cargo: {} by user: {}", cargoCode, updatedByUserId);
    }

    // ===== PRIVATE HELPER METHODS =====

    private void validateTripForCargo(Trip trip) {
        if (trip.getStatus() == TripStatus.cancelled) {
            throw CargoBookingException.failed("Cannot book cargo for cancelled trip");
        }

        if (trip.getStatus() == TripStatus.departed || trip.getStatus() == TripStatus.arrived) {
            throw CargoBookingException.failed("Cannot book cargo for trip that has already departed");
        }

        if (trip.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw CargoBookingException.failed("Cannot book cargo for past trips");
        }
    }

    private void validateStatusTransition(CargoStatus currentStatus, CargoStatus newStatus) {
        // Allow staying in same status (no-op update)
        if (currentStatus == newStatus) {
            return;
        }

        // Define valid status transitions
        // Note: Includes rollback options for staff to fix mistakes
        Map<CargoStatus, List<CargoStatus>> validTransitions = Map.of(
                CargoStatus.PENDING, List.of(
                        CargoStatus.CONFIRMED,
                        CargoStatus.CANCELLED,
                        CargoStatus.REJECTED), // Staff can reject during inspection

                CargoStatus.CONFIRMED, List.of(
                        CargoStatus.PENDING, // Rollback if confirmed by mistake
                        CargoStatus.PICKED_UP,
                        CargoStatus.CANCELLED,
                        CargoStatus.REJECTED), // Staff can still reject if issue found after confirmation

                CargoStatus.PICKED_UP, List.of(
                        CargoStatus.CONFIRMED, // Rollback if picked up marked by mistake
                        CargoStatus.IN_TRANSIT,
                        CargoStatus.RETURNED), // Return to sender directly if issue at pickup

                CargoStatus.IN_TRANSIT, List.of(
                        CargoStatus.PICKED_UP, // Rollback if marked in transit by mistake
                        CargoStatus.ARRIVED,
                        CargoStatus.RETURNED), // Return if issue during transit

                CargoStatus.ARRIVED, List.of(
                        CargoStatus.IN_TRANSIT, // Rollback if marked arrived by mistake
                        CargoStatus.DELIVERED,
                        CargoStatus.RETURNED), // Return if receiver refuses delivery

                // Terminal states - no forward transitions
                CargoStatus.DELIVERED, List.of(
                        CargoStatus.ARRIVED), // Allow rollback to ARRIVED if delivered marked by mistake

                CargoStatus.CANCELLED, List.of(), // No transitions from CANCELLED

                CargoStatus.REJECTED, List.of(), // No transitions from REJECTED (terminal state)

                CargoStatus.RETURNED, List.of() // No transitions from RETURNED
        );

        List<CargoStatus> allowedStatuses = validTransitions.get(currentStatus);
        if (allowedStatuses == null || !allowedStatuses.contains(newStatus)) {
            throw InvalidCargoStatusTransitionException.transition(currentStatus, newStatus);
        }
    }

    private void updateStatusTimestamps(CargoBooking cargoBooking, CargoStatus newStatus) {
        LocalDateTime now = LocalDateTime.now();
        switch (newStatus) {
            case CONFIRMED:
                cargoBooking.setConfirmedAt(now);
                break;
            case PICKED_UP:
                cargoBooking.setPickedUpAt(now);
                break;
            case DELIVERED:
                cargoBooking.setDeliveredAt(now);
                break;
            case CANCELLED:
                cargoBooking.setCancelledAt(now);
                break;
            case REJECTED:
                cargoBooking.setRejectedAt(now);
                break;
            default:
                // No specific timestamp for other statuses
                break;
        }
    }

    private CargoTracking createTrackingRecord(CargoBooking cargoBooking, CargoStatus status,
            String location, String notes, Long updatedByUserId) {
        User updatedBy = null;
        if (updatedByUserId != null) {
            updatedBy = userRepository.findById(updatedByUserId).orElse(null);
        }

        CargoTracking tracking = CargoTracking.builder()
                .cargoBooking(cargoBooking)
                .status(status)
                .location(location)
                .notes(notes)
                .createdAt(LocalDateTime.now())
                .updatedBy(updatedBy)
                .build();

        return cargoTrackingRepository.save(tracking);
    }

    /**
     * Update payment status when cargo is cancelled and process automatic refund
     * 
     * Refund policy (handled by RefundPolicyUtil.calculateRefund):
     * - Cancel > 24h before departure: 100% refund
     * - Cancel <= 24h before departure: 70% refund (30% cancellation fee)
     * - After departure: 0% refund
     * 
     * This method only creates the refund request. The actual refund processing
     * (fee calculation, payment gateway transaction) is handled by RefundService.
     * 
     * @param cargoBooking The cargo booking being cancelled
     */
    private void updatePaymentOnCancellation(CargoBooking cargoBooking) {
        try {
            Payment payment = paymentRepository.findByCargoBookingId(cargoBooking.getCargoBookingId())
                    .orElse(null);

            // Kiểm tra có payment và đã completed chưa
            if (payment != null && payment.getStatus() == PaymentStatus.completed) {
                log.info("Processing automatic refund for cargo: {}", cargoBooking.getCargoCode());

                // Tạo refund request (apply time-based policy)
                RefundRequestDTO refundRequest = new RefundRequestDTO(
                        payment.getPaymentId(),
                        "Cargo booking cancelled",
                        "Auto-refund for cargo: " + cargoBooking.getCargoCode());

                // Tạo refund (RefundService sẽ tự động tính toán fee theo thời gian và process)
                refundService.createRefund(refundRequest);

                log.info("Refund request created successfully for cargo: {}", cargoBooking.getCargoCode());
            } else {
                log.info("No refund needed for cargo: {} (no payment or payment not completed)",
                        cargoBooking.getCargoCode());
            }
        } catch (Exception e) {
            log.error("Error processing refund for cargo: {}", cargoBooking.getCargoCode(), e);
            // Không throw exception để không ảnh hưởng đến việc cancel cargo
        }
    }

    /**
     * Process refund when cargo is REJECTED by staff
     * 
     * REJECTION REFUND POLICY:
     * - Always 100% refund regardless of departure time
     * - Bypass normal time-based refund policy
     * - Reason: Staff rejected the cargo (prohibited items, wrong description,
     * etc.)
     * 
     * @param cargoBooking The cargo booking being rejected
     */
    private void updatePaymentOnRejection(CargoBooking cargoBooking) {
        try {
            Payment payment = paymentRepository.findByCargoBookingId(cargoBooking.getCargoBookingId())
                    .orElse(null);

            // Kiểm tra có payment và đã completed chưa
            if (payment != null && payment.getStatus() == PaymentStatus.completed) {
                log.info("Processing 100% refund for REJECTED cargo: {}", cargoBooking.getCargoCode());

                // Tạo refund request with bypassPolicy = true (100% refund)
                RefundRequestDTO refundRequest = new RefundRequestDTO();
                refundRequest.setPaymentId(payment.getPaymentId());
                refundRequest.setRefundReason("Cargo rejected by staff");
                refundRequest.setNotes("Auto-refund for rejected cargo: " + cargoBooking.getCargoCode() +
                        ". Reason: " + cargoBooking.getRejectionReason());
                refundRequest.setBypassPolicy(true); // Bypass time-based policy - always 100% refund

                // Tạo refund (100% refund regardless of time)
                refundService.createRefund(refundRequest);

                log.info("100% refund request created successfully for REJECTED cargo: {}",
                        cargoBooking.getCargoCode());
            } else {
                log.info("No refund needed for REJECTED cargo: {} (no payment or payment not completed)",
                        cargoBooking.getCargoCode());
            }
        } catch (Exception e) {
            log.error("Error processing refund for REJECTED cargo: {}", cargoBooking.getCargoCode(), e);
            // Không throw exception để không ảnh hưởng đến việc reject cargo
        }
    }

    /**
     * Calculate distance between two locations using Haversine formula
     * 
     * @param pickupLocationId  Pickup location ID
     * @param dropoffLocationId Dropoff location ID
     * @return Distance in kilometers
     */
    private Double calculateDistance(Long pickupLocationId, Long dropoffLocationId) {
        // If same location, return 0
        if (pickupLocationId.equals(dropoffLocationId)) {
            return 0.0;
        }

        Location pickupLocation = locationRepository.findById(pickupLocationId)
                .orElseThrow(() -> CargoBookingException.failed("Pickup location not found"));
        Location dropoffLocation = locationRepository.findById(dropoffLocationId)
                .orElseThrow(() -> CargoBookingException.failed("Dropoff location not found"));

        // Check if coordinates are available
        if (pickupLocation.getLatitude() == null || pickupLocation.getLongitude() == null
                || dropoffLocation.getLatitude() == null || dropoffLocation.getLongitude() == null) {
            // Fallback: estimate based on city distance (rough estimate)
            log.warn("Location coordinates missing for distance calculation. Using city-based estimate.");
            return estimateDistanceByCity(pickupLocation.getCity(), dropoffLocation.getCity());
        }

        // Calculate distance using Haversine formula
        return calculateHaversineDistance(
                pickupLocation.getLatitude(), pickupLocation.getLongitude(),
                dropoffLocation.getLatitude(), dropoffLocation.getLongitude());
    }

    /**
     * Calculate distance using Haversine formula
     * Formula: a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
     * c = 2 ⋅ atan2( √a, √(1−a) )
     * d = R ⋅ c
     * where φ is latitude, λ is longitude, R is earth's radius (6371 km)
     */
    private Double calculateHaversineDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int EARTH_RADIUS_KM = 6371;

        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        // Haversine formula
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                        * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        double distance = EARTH_RADIUS_KM * c;

        // Round to 2 decimal places
        return Math.round(distance * 100.0) / 100.0;
    }

    /**
     * Estimate distance based on city names (fallback when coordinates unavailable)
     * This is a rough estimate - should be replaced with actual data
     */
    private Double estimateDistanceByCity(String city1, String city2) {
        // Same city - assume 10km average
        if (city1.equalsIgnoreCase(city2)) {
            return 10.0;
        }

        // Common routes in Vietnam (rough estimates)
        Map<String, Map<String, Double>> cityDistances = Map.of(
                "Hồ Chí Minh", Map.of(
                        "Đà Lạt", 300.0,
                        "Nha Trang", 450.0,
                        "Đà Nẵng", 950.0,
                        "Hà Nội", 1750.0,
                        "Vũng Tàu", 125.0),
                "Hà Nội", Map.of(
                        "Hải Phòng", 120.0,
                        "Đà Nẵng", 800.0,
                        "Hồ Chí Minh", 1750.0),
                "Đà Nẵng", Map.of(
                        "Hội An", 30.0,
                        "Huế", 100.0,
                        "Nha Trang", 550.0,
                        "Hồ Chí Minh", 950.0,
                        "Hà Nội", 800.0));

        // Try to find distance
        if (cityDistances.containsKey(city1) && cityDistances.get(city1).containsKey(city2)) {
            return cityDistances.get(city1).get(city2);
        }
        if (cityDistances.containsKey(city2) && cityDistances.get(city2).containsKey(city1)) {
            return cityDistances.get(city2).get(city1);
        }

        // Default fallback - assume 200km for unknown routes
        log.warn("Unknown city pair: {} - {}. Using default estimate: 200km", city1, city2);
        return 200.0;
    }

    // ===== PAYMENT OPERATIONS WITH STRATEGY PATTERN =====

    @Override
    @Transactional
    public PaymentResponseDTO createCargoPayment(CargoPaymentRequestDTO request) {
        log.info("Creating cargo payment for cargo booking ID: {}, method: {}",
                request.getCargoBookingId(), request.getPaymentMethod());

        // 1. Find cargo booking
        CargoBooking cargoBooking = cargoBookingRepository.findById(request.getCargoBookingId())
                .orElseThrow(() -> CargoNotFoundException.byId(request.getCargoBookingId()));

        // 2. Check if payment already exists
        Payment existingPayment = paymentRepository.findByCargoBookingId(cargoBooking.getCargoBookingId())
                .orElse(null);

        Payment payment;
        if (existingPayment != null) {
            payment = existingPayment;
            log.info("Found existing payment for cargo booking {}: Payment ID {}",
                    cargoBooking.getCargoBookingId(), payment.getPaymentId());

            // If payment is completed, don't allow re-payment
            if (payment.getStatus() == PaymentStatus.completed) {
                throw CargoBookingException.failed("Cargo booking already paid");
            }

            // If payment is cancelled or failed, reset for retry
            if (payment.getStatus() == PaymentStatus.cancelled
                    || payment.getStatus() == PaymentStatus.failed) {
                resetPaymentForRetry(payment, request.getPaymentMethod());
            }
        } else {
            // Create new payment
            payment = createNewPaymentForCargo(cargoBooking, request.getPaymentMethod());
            log.info("Created new payment for cargo booking {}: Payment ID {}",
                    cargoBooking.getCargoBookingId(), payment.getPaymentId());
        }

        // 3. Get payment strategy
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(request.getPaymentMethod());

        try {
            // 4. Create payment URL through strategy
            // Note: PaymentRequestDTO is designed for booking, so we create a minimal DTO
            PaymentRequestDTO paymentRequestDTO = PaymentRequestDTO.builder()
                    .bookingId(null) // Not used for cargo
                    .paymentMethod(request.getPaymentMethod())
                    .build();

            String paymentUrl = strategy.createPaymentUrl(payment, paymentRequestDTO);

            // Refresh payment entity to get updated status from strategy
            // (needed for SIMULATION and other auto-complete payment methods)
            payment = paymentRepository.findById(payment.getPaymentId())
                    .orElseThrow(() -> new RuntimeException("Payment not found after creation"));

            log.info("Created payment URL for cargo payment ID: {}, status: {}",
                    payment.getPaymentId(), payment.getStatus());

            return PaymentResponseDTO.builder()
                    .paymentId(payment.getPaymentId())
                    .status(payment.getStatus()) // Get actual status from DB
                    .paymentUrl(paymentUrl)
                    .cargoBookingId(cargoBooking.getCargoBookingId())
                    .bookingId(null)
                    .build();

        } catch (Exception e) {
            log.error("Error creating cargo payment: ", e);
            throw CargoBookingException.failed("Failed to create cargo payment: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public PaymentResponseDTO executeCargoPayment(String cargoCode, String paymentId, String payerId) {
        log.info("Executing cargo payment for cargo code: {}, payment ID: {}", cargoCode, paymentId);

        // 1. Find cargo booking by code
        CargoBooking cargoBooking = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        // 2. Find payment
        Payment payment = paymentRepository.findByCargoBookingId(cargoBooking.getCargoBookingId())
                .orElseThrow(() -> CargoBookingException.failed("Payment not found for cargo: " + cargoCode));

        // 3. Get payment strategy
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(payment.getPaymentMethod());

        try {
            // 4. Execute payment through strategy
            PaymentResponseDTO response = strategy.executePayment(payment, paymentId, payerId);

            // 5. Update cargo booking status to CONFIRMED if payment successful
            if (response.getStatus() == PaymentStatus.completed) {
                cargoBooking.setStatus(CargoStatus.CONFIRMED);
                cargoBooking.setConfirmedAt(LocalDateTime.now());
                cargoBookingRepository.save(cargoBooking);

                // Add tracking record
                createTrackingRecord(cargoBooking, CargoStatus.CONFIRMED,
                        cargoBooking.getPickupLocation().getCity(),
                        "Payment completed successfully",
                        cargoBooking.getBookingUser() != null ? cargoBooking.getBookingUser().getId() : null);

                log.info("Cargo payment completed successfully for cargo code: {}", cargoCode);
            }

            return response;

        } catch (Exception e) {
            log.error("Error executing cargo payment: ", e);
            throw CargoBookingException.failed("Failed to execute cargo payment: " + e.getMessage(), e);
        }
    }

    /**
     * Create new payment for cargo booking
     */
    private Payment createNewPaymentForCargo(CargoBooking cargoBooking, PaymentMethod paymentMethod) {
        Payment payment = new Payment();
        payment.setBooking(null); // Explicitly set null - this is cargo payment, not ticket booking
        payment.setCargoBooking(cargoBooking);
        payment.setPaymentMethod(paymentMethod);
        payment.setAmount(cargoBooking.getTotalAmount());
        payment.setTransactionCode(generateTransactionCodeForCargo());
        payment.setStatus(PaymentStatus.pending);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    /**
     * Generate unique transaction code for cargo payment
     */
    private String generateTransactionCodeForCargo() {
        return "TXN-CARGO-" + System.currentTimeMillis() + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Reset payment for retry
     */
    private void resetPaymentForRetry(Payment payment, PaymentMethod paymentMethod) {
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(PaymentStatus.pending);
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setPaidAt(null);
        payment.setPaymentGatewayId(null);

        paymentRepository.save(payment);
        log.info("Reset payment for retry: Payment ID {}", payment.getPaymentId());
    }

    // ===== USER HELPER =====

    /**
     * Get current authenticated user ID from UserService
     * Throws exception if user is not authenticated
     * 
     * @return User ID of currently logged in user
     * @throws CargoBookingException if user is not authenticated
     */
    private Long getCurrentUserId() {
        try {
            User currentUser = userService.getUserCurrentlyLoggedIn();
            if (currentUser == null) {
                throw CargoBookingException.failed("User must be authenticated to perform this action");
            }
            return currentUser.getId();
        } catch (CargoBookingException e) {
            // Re-throw our custom exception
            throw e;
        } catch (Exception e) {
            // User not authenticated or error occurred
            log.error("Authentication error: {}", e.getMessage());
            throw CargoBookingException.failed("User must be authenticated to perform this action");
        }
    }

    /**
     * Export cargo booking to PDF
     * 
     * @param cargoCode Unique cargo code
     * @return PDF as byte array
     * @throws CargoNotFoundException if cargo not found
     */
    @Override
    public byte[] exportCargoToPdf(String cargoCode) {
        CargoBooking cargo = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        try {
            return PdfGeneratorUtil.generateCargoPDF(cargo);
        } catch (IOException e) {
            log.error("Error generating PDF for cargo {}: {}", cargoCode, e.getMessage(), e);
            throw new RuntimeException("Could not generate PDF for cargo " + cargoCode, e);
        }
    }

    /**
     * Validate cargo booking with trip
     * Used by driver/staff to verify cargo belongs to trip before pickup/delivery
     * 
     * @param tripId    Trip ID to validate against
     * @param cargoCode Unique cargo code
     * @return Cargo validation response with detailed information
     * @throws CargoNotFoundException   if cargo not found
     * @throws IllegalArgumentException if cargo does not belong to trip
     */
    @Override
    @Transactional(readOnly = true)
    public CargoValidationResponseDTO validateCargoTrip(Long tripId, String cargoCode) {
        log.info("Validating cargo {} for trip {}", cargoCode, tripId);

        // 1. Find cargo by code
        CargoBooking cargo = cargoBookingRepository.findByCargoCode(cargoCode)
                .orElseThrow(() -> CargoNotFoundException.byCargoCode(cargoCode));

        // 2. Validate cargo belongs to trip
        if (!cargo.getTrip().getId().equals(tripId)) {
            String errorMsg = String.format(
                    "Cargo %s does not belong to trip %d (belongs to trip %d)",
                    cargoCode, tripId, cargo.getTrip().getId());
            log.warn(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        // 3. Get payment status
        Payment payment = paymentRepository.findByCargoBookingId(cargo.getCargoBookingId())
                .orElse(null);
        boolean isPaid = payment != null && payment.getStatus() == PaymentStatus.completed;

        // 4. Determine validation status
        boolean isValid = true;
        String validationMessage = "Cargo hợp lệ";

        // Check if cargo status is valid for pickup/delivery
        if (cargo.getStatus() == CargoStatus.CANCELLED) {
            isValid = false;
            validationMessage = "Cargo đã bị hủy";
        } else if (cargo.getStatus() == CargoStatus.REJECTED) {
            isValid = false;
            validationMessage = "Cargo đã bị từ chối";
        } else if (!isPaid) {
            isValid = false;
            validationMessage = "Cargo chưa thanh toán";
        } else if (cargo.getStatus() == CargoStatus.DELIVERED) {
            isValid = false;
            validationMessage = "Cargo đã được giao";
        }

        // 5. Build response DTO
        return CargoValidationResponseDTO.builder()
                // Cargo Information
                .cargoCode(cargo.getCargoCode())
                .status(cargo.getStatus())
                .statusDisplay(cargo.getStatus().getDisplayName())

                // Trip Information
                .tripId(cargo.getTrip().getId())
                .departureTime(cargo.getTrip().getDepartureTime())

                // Sender Information
                .senderName(cargo.getSenderName())
                .senderPhone(cargo.getSenderPhone())

                // Receiver Information
                .receiverName(cargo.getReceiverName())
                .receiverPhone(cargo.getReceiverPhone())

                // Cargo Details
                .cargoTypeDisplay(cargo.getCargoType().getDisplayName())
                .weight(cargo.getWeight())
                .description(cargo.getDescription())

                // Location Information
                .pickupLocationName(cargo.getPickupLocation().getCity())
                .dropoffLocationName(cargo.getDropoffLocation().getCity())

                // Payment Information
                .totalAmount(cargo.getTotalAmount())
                .isPaid(isPaid)

                // Validation Status
                .isValid(isValid)
                .validationMessage(validationMessage)
                .validatedAt(LocalDateTime.now())
                .images(cargoImageRepository.findByCargoBookingCargoBookingId(cargo.getCargoBookingId())
                        .stream()
                        .map(CargoImage::getImageUrl)
                        .collect(Collectors.toList()))

                .build();
    }
}
