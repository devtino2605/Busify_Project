package com.busify.project.trip.dto.response;

public interface TripStopResponse {
    Long getLocationId();

    String getCity();

    String getName();

    String getAddress();

    Double getLongitude();

    Double getLatitude();

    Integer getTimeOffsetFromStart();
}
