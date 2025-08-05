package com.busify.project.trip.dto.response;

import java.time.Instant;

public interface TripDetailResponse {
    Long getId();

    String getStartCity();
    String getStartName();
    String getStartAddress();
    Double getStartLongitude();
    Double getStartLatitude();

    String getEndCity();
    String getEndName();
    String getEndAddress();
    Double getEndLongitude();
    Double getEndLatitude();

    Integer getEstimatedDurationMinutes();

    Long getBusId();
    String getBusName();
    String getBusAmenities();
    Integer getBusSeats();
    String getBusLicensePlate();

    Long getOperatorId();
    String getOperatorName();

    Instant getDepartureTime();

    Double getPricePerSeat();
    
    Long getRouteId();
}
