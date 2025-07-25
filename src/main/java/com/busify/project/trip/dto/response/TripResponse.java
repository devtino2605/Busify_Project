package com.busify.project.trip.dto.response;

import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.trip.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripResponse {
    private Long tripId;
    private String operatorName;
    private RouteResponse route;
    private Instant departureTime;
    private Instant arrivalTime;
    private Integer availableSeats;
    private double averageRating;
    private TripStatus status;
}
