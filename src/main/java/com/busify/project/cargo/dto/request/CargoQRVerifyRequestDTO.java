package com.busify.project.cargo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for verifying cargo pickup via QR code
 * Used by staff at destination to confirm cargo delivery
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoQRVerifyRequestDTO {

    /**
     * JWT token from QR code (scanned by staff)
     */
    @NotBlank(message = "QR token is required")
    private String qrToken;
}
