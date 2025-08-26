package com.busify.project.bus_operator.dto.response;

import java.math.BigDecimal;

public interface MonthlyBusOperatorReportDTO {
    Long getOperatorId();

    String getOperatorName();

    String getOperatorEmail();

    Long getMonth();

    Long getYear();

    Long getTotalTrips();

    BigDecimal getTotalRevenue();

    Long getTotalPassengers();

    Long getTotalBuses();

    java.sql.Date getReportGeneratedDate();

    Integer getSentToAdmin();
}