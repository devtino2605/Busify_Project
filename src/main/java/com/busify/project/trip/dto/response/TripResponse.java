package com.busify.project.trip.dto.response;

import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.trip.enums.TripStatus;

import java.time.LocalDateTime;

public class TripResponse {
    private Long tripId;
    private String operatorName;
    private RouteResponse route;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private double averageRating;
    private TripStatus status;
}
