package com.busify.project.cargo.dto.response;

import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.cargo.enums.CargoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CargoBookingResponseDTO
 * 
 * Response DTO for cargo booking (basic info)
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoBookingResponseDTO implements Serializable {

    private Long cargoBookingId;
    private String cargoCode;
    private Long tripId;
    private String tripRoute; // e.g., "Sài Gòn → Đà Lạt"

    // Sender info
    private String senderName;
    private String senderPhone;

    // Receiver info
    private String receiverName;
    private String receiverPhone;

    // Cargo info
    private CargoType cargoType;
    private String cargoTypeDisplay;
    private BigDecimal weight;
    private String dimensions;

    // Locations
    private Long pickupLocationId;
    private String pickupLocationName;
    private Long dropoffLocationId;
    private String dropoffLocationName;

    // Fees
    private BigDecimal cargoFee;
    private BigDecimal insuranceFee;
    private BigDecimal totalAmount;

    // Status
    private CargoStatus status;
    private String statusDisplay;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime estimatedPickupTime;
    private LocalDateTime estimatedDeliveryTime;
}
