package com.busify.project.cargo.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.busify.project.cargo.enums.CargoStatus;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CargoValidationResponseDTO {

    // Cargo Information
    private String cargoCode;
    private CargoStatus status;
    private String statusDisplay;

    // Trip Information
    private Long tripId;
    private LocalDateTime departureTime;

    // Sender Information
    private String senderName;
    private String senderPhone;

    // Receiver Information
    private String receiverName;
    private String receiverPhone;

    // Cargo Details
    private String cargoTypeDisplay;
    private BigDecimal weight;
    private String description;

    // Location Information
    private String pickupLocationName;
    private String dropoffLocationName;

    // Payment Information
    private BigDecimal totalAmount;
    private Boolean isPaid;

    // Validation Status
    private Boolean isValid;
    private String validationMessage;
    private LocalDateTime validatedAt;

    private List<String> images;
}
