package com.busify.project.cargo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * CargoFeeCalculationResponseDTO
 * 
 * Response DTO for cargo fee calculation
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoFeeCalculationResponseDTO implements Serializable {

    private BigDecimal baseFee;
    private BigDecimal weightFee;
    private BigDecimal distanceFee;
    private BigDecimal cargoTypeFee;
    private BigDecimal cargoFee; // Total cargo fee before insurance
    private BigDecimal subtotal;

    private BigDecimal insuranceFee;
    private BigDecimal totalAmount;

    // Additional info
    private BigDecimal weight;
    private Double distance; // in km
    private String cargoType;
    private Double typeMultiplier;
    private BigDecimal declaredValue;
    private Boolean insuranceIncluded;

    // Breakdown explanation (detailed calculation steps)
    private FeeBreakdown breakdown;

    /**
     * Nested class for fee breakdown details
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeeBreakdown {
        private String distanceFee; // e.g., "800 km × 2,000 VND/km = 16,000 VND"
        private String weightFee; // e.g., "5.5 kg × 5,000 VND/kg = 27,500 VND"
        private String cargoFee; // e.g., "16,000 + 27,500 = 43,500 VND (rounded to 50,000)"
        private String insurance; // e.g., "500,000 × 1% = 5,000 VND"
    }
}
