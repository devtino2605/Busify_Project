package com.busify.project.cargo.dto.response;

import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.cargo.enums.CargoType;
import com.busify.project.payment.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CargoListResponseDTO
 * 
 * Response DTO for cargo list (used in search/filter results)
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoListResponseDTO implements Serializable {

    private Long cargoBookingId;
    private String cargoCode;

    // Trip info
    private Long tripId;
    private String routeName;
    private LocalDateTime departureTime;

    // Basic info
    private String senderName;
    private String senderPhone;
    private String receiverName;
    private String receiverPhone;

    // Cargo info
    private CargoType cargoType;
    private String cargoTypeDisplay;
    private BigDecimal weight;

    // Locations
    private String pickupLocationName;
    private String dropoffLocationName;

    // Status
    private CargoStatus status;
    private String statusDisplay;

    // Fees
    private BigDecimal totalAmount;

    // Bus info
    private BusInfo bus;

    // Payment info
    private PaymentInfo payment;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;

    private List<String> images;

    /**
     * Nested class for bus information
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusInfo {
        private String model;
        private String licensePlate;
    }

    /**
     * Nested class for payment information
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentInfo {
        private BigDecimal amount;
        private PaymentMethod method;
        private LocalDateTime timestamp;
    }
}
