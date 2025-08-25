package com.busify.project.bus_operator.dto.response;

import java.math.BigDecimal;

public interface MonthlyTotalRevenueDTO {
    Integer getMonth();

    Integer getYear();

    BigDecimal getTotalRevenue();

    Long getTotalTrips();

    Long getTotalPassengers();

    Long getTotalActiveOperators();
}
