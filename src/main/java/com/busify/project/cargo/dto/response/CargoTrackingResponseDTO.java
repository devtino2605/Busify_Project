package com.busify.project.cargo.dto.response;

import com.busify.project.cargo.enums.CargoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CargoTrackingResponseDTO
 * 
 * Response DTO for cargo tracking history
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoTrackingResponseDTO implements Serializable {
    
    private Long trackingId;
    private CargoStatus status;
    private String statusDisplay;
    private String location;
    private String notes;
    private LocalDateTime createdAt;
    
    // Updated by info
    private Long updatedById;
    private String updatedByName;
    private String updatedByRole;
}
