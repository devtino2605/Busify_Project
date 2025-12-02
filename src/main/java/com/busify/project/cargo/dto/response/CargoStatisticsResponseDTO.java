package com.busify.project.cargo.dto.response;

import com.busify.project.cargo.enums.CargoStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * CargoStatisticsResponseDTO
 * 
 * Response DTO for cargo booking statistics (for operator dashboard)
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoStatisticsResponseDTO implements Serializable {

    // Overall statistics
    private Long totalCargoBookings;
    private BigDecimal totalRevenue;
    private BigDecimal totalCargoWeight;

    // Status breakdown
    private Map<CargoStatus, Long> cargoCountByStatus;
    private Map<CargoStatus, BigDecimal> revenueByStatus;

    // Current statistics
    private Long pendingCargo;
    private Long confirmedCargo;
    private Long inTransitCargo;
    private Long deliveredCargo;
    private Long cancelledCargo;

    // Time period (for filtering)
    private String period; // e.g., "Today", "This Week", "This Month"
    private String startDate;
    private String endDate;

    // Average metrics
    private BigDecimal averageCargoFee;
    private BigDecimal averageCargoWeight;
    private Double deliverySuccessRate; // Percentage of successfully delivered cargo

    // Top statistics
    private String mostUsedCargoType;
    private String busiestRoute;

    // ===== HELPER METHODS =====

    /**
     * Get total cargo count excluding cancelled
     */
    public Long getActiveCargo() {
        return totalCargoBookings - (cancelledCargo != null ? cancelledCargo : 0L);
    }

    /**
     * Get revenue excluding cancelled cargo
     */
    public BigDecimal getActiveRevenue() {
        BigDecimal cancelledRevenue = revenueByStatus != null 
            ? revenueByStatus.getOrDefault(CargoStatus.CANCELLED, BigDecimal.ZERO) 
            : BigDecimal.ZERO;
        return totalRevenue.subtract(cancelledRevenue);
    }

    /**
     * Calculate in-progress cargo (confirmed + picked_up + in_transit)
     */
    public Long getInProgressCargo() {
        Long confirmed = confirmedCargo != null ? confirmedCargo : 0L;
        Long inTransit = inTransitCargo != null ? inTransitCargo : 0L;
        return confirmed + inTransit;
    }
}
