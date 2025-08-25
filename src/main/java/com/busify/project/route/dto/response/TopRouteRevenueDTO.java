package com.busify.project.route.dto.response;

import java.math.BigDecimal;

public interface TopRouteRevenueDTO {
    Long getRouteId();

    String getRouteName();

    String getStartLocation();

    String getEndLocation();

    Long getTotalTrips();

    Long getTotalBookings();

    BigDecimal getTotalRevenue();

    BigDecimal getAverageRevenuePerTrip();
}
