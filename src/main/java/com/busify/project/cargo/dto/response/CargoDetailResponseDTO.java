package com.busify.project.cargo.dto.response;

import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.cargo.enums.CargoType;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CargoDetailResponseDTO
 * 
 * Response DTO for detailed cargo booking information
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoDetailResponseDTO implements Serializable {

    private Long cargoBookingId;
    private String cargoCode;

    // Trip info
    private TripInfo trip;

    // Sender info
    private ContactInfo sender;

    // Receiver info
    private ContactInfo receiver;

    // Cargo info
    private CargoInfo cargo;

    // Location info
    private LocationInfo pickup;
    private LocationInfo dropoff;

    // Payment info
    private PaymentInfo payment;

    // Status and tracking
    private CargoStatus status;
    private String statusDisplay;
    private List<CargoTrackingResponseDTO> trackingHistory;

    // Images
    private List<ImageInfo> images;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime pickedUpAt;
    private LocalDateTime deliveredAt;

    // Notes
    private String specialInstructions;

    // ===== NESTED CLASSES =====

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripInfo {
        private Long tripId;
        private String routeName;
        private LocalDateTime departureTime;
        private LocalDateTime arrivalTime;
        private String busOperatorName;
        private String driverName;
        private String driverPhone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactInfo {
        private String name;
        private String phone;
        private String email;
        private String address;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CargoInfo {
        private CargoType type;
        private String typeDisplay;
        private String description;
        private BigDecimal weight;
        private String dimensions;
        private BigDecimal declaredValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationInfo {
        private Long locationId;
        private String locationName;
        private String locationCity;
        private String specificAddress;
        private String notes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentInfo {
        private Long paymentId;
        private PaymentMethod paymentMethod;
        private String paymentMethodDisplay;
        private BigDecimal cargoFee;
        private BigDecimal insuranceFee;
        private BigDecimal totalAmount;
        private PaymentStatus status;
        private String statusDisplay;
        private String transactionId;
        private LocalDateTime paidAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageInfo {
        private Long imageId;
        private String imageUrl;
        private String imageType;
        private String description;
        private LocalDateTime uploadedAt;
    }
}
