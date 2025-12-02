package com.busify.project.cargo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * CargoFeeConfig
 * 
 * Configuration properties for cargo fee calculation
 * Allows flexible configuration via application.properties
 * 
 * Pricing strategy based on real-world bus cargo services in Vietnam
 * Reference: Phương Trang (FUTA), Thành Bưởi, Mai Linh
 * 
 * Formula: Total = BASE_FEE + (distance × FEE_PER_KM) + (weight × FEE_PER_KG)
 * 
 * Example: HCM → Da Nang (950km), 5kg
 * - Base fee: 20,000 VND
 * - Distance: 950 × 50 = 47,500 VND
 * - Weight: 5 × 8,000 = 40,000 VND
 * - Total: ~107,500 VND (competitive with market: 50-120k VND)
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-12-02
 */
@Configuration
@ConfigurationProperties(prefix = "cargo.fee")
@Data
public class CargoFeeConfig {

    /**
     * Base fee for all cargo shipments (VND)
     * Default: 20,000 VND
     */
    private BigDecimal baseFee = new BigDecimal("20000");

    /**
     * Fee per kilometer (VND/km)
     * Default: 50 VND per km (reasonable for long distance)
     */
    private BigDecimal feePerKm = new BigDecimal("50");

    /**
     * Fee per kilogram (VND/kg)
     * Default: 8,000 VND per kg (competitive with J&T: 8,000-10,000)
     */
    private BigDecimal feePerKg = new BigDecimal("8000");

    /**
     * Insurance rate as percentage of declared value
     * Default: 0.5% (0.005)
     */
    private Double insuranceRate = 0.005;

    /**
     * Minimum insurance fee (VND)
     * Default: 5,000 VND
     */
    private BigDecimal minInsuranceFee = new BigDecimal("5000");

    /**
     * Minimum hours before departure to allow cargo cancellation
     * 
     * REFUND POLICY (handled by RefundPolicyUtil):
     * - Cancel > 24h before departure: 100% refund (no cancellation fee)
     * - Cancel <= 24h before departure: 70% refund (30% cancellation fee)
     * - After departure: 0% refund (100% cancellation fee)
     * 
     * This constant prevents cancellation too close to departure time.
     * For refund calculation, see RefundPolicyUtil.calculateRefund()
     */
    private Integer cancellationHoursBeforeDeparture = 24;

    /**
     * Default max cargo weight if bus model doesn't specify (kg)
     * This is a fallback value for backward compatibility
     * Default: 500.0 kg
     */
    private BigDecimal defaultMaxCargoWeight = new BigDecimal("500.0");
}
