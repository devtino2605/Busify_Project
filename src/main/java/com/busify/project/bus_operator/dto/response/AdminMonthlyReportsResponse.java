package com.busify.project.bus_operator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMonthlyReportsResponse {
    private int month;
    private int year;
    private BigDecimal totalSystemRevenue;
    private Long totalOperators;
    private Long totalTrips;
    private Long totalPassengers;
    private List<MonthlyBusOperatorReportDTO> operatorReports;

    public AdminMonthlyReportsResponse(BigDecimal totalSystemRevenue,
            Long totalOperators, Long totalTrips, Long totalPassengers,
            List<MonthlyBusOperatorReportDTO> operatorReports) {
        this.totalSystemRevenue = totalSystemRevenue;
        this.totalOperators = totalOperators;
        this.totalTrips = totalTrips;
        this.totalPassengers = totalPassengers;
        this.operatorReports = operatorReports;
    }
}
