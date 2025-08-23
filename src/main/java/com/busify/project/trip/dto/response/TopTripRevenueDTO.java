package com.busify.project.trip.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public interface TopTripRevenueDTO {
    Long getTripId();

    String getRouteName();

    String getStartLocation();

    String getEndLocation();

    Instant getDepartureTime();

    String getBusName();

    String getBusOperatorName();

    Long getTotalBookings();

    BigDecimal getTotalRevenue();

    BigDecimal getPricePerSeat();
}
