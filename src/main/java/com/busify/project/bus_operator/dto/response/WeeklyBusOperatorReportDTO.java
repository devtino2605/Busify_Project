package com.busify.project.bus_operator.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyBusOperatorReportDTO {
    private Long operatorId;
    private Long totalTrips;
    private BigDecimal totalRevenue;
    private Long totalPassengers;
    private Long totalBuses;
}
