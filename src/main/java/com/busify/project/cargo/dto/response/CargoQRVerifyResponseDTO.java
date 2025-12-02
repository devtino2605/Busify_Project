package com.busify.project.cargo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO after QR code verification
 * Returns cargo delivery confirmation details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoQRVerifyResponseDTO {

    /**
     * Success status
     */
    private Boolean success;

    /**
     * Message describing the result
     */
    private String message;

    /**
     * Cargo code
     */
    private String cargoCode;

    /**
     * Sender information
     */
    private String senderName;
    private String senderPhone;

    /**
     * Receiver information (verified from QR)
     */
    private String receiverName;
    private String receiverPhone;

    /**
     * Cargo details
     */
    private String cargoType;
    private Double weight;
    private String description;

    /**
     * Staff who confirmed the pickup
     */
    private Long confirmedByStaffId;

    /**
     * Delivery timestamp
     */
    private LocalDateTime deliveredAt;

    /**
     * Trip information
     */
    private Long tripId;
    private String routeName;
}
