package com.busify.project.trip.dto.response;

import java.time.Instant;

import com.busify.project.bus.enums.BusStatus;

public interface NextTripsOfOperatorResponseDTO {
    Long getTripId();

    int getAvailableSeats();

    int getTotalSeats();

    Instant getDepartureTime();

    Instant getArrivalTime(); // arrival_time

    Integer getEstimatedDurationMinutes();

    Integer getBusSeats(); // total_seats

    String getStartCity(); // route.start_location.city

    String getStartAddress(); // route.start_location.address

    Double getStartLongitude(); // route.start_location.longitude

    Double getStartLatitude(); // route.start_location.latitude

    // --- Địa điểm đến ---
    String getEndCity(); // route.end_location.city

    String getEndAddress(); // route.end_location.address

    Double getEndLongitude(); // route.end_location.longitude

    Double getEndLatitude(); // route.end_location.latitude

    String getRouteName(); // route.name

    Long getRouteId();

    Long getBusId();

    String getBusLicensePlate();

    BusStatus getBusStatus();
}
