package com.busify.project.trip.dto.request;

import com.busify.project.trip.enums.TripStatus;
import lombok.Data;
import java.time.Instant;

@Data
public class TripSearchRequestDTO {
    private Instant departureDate;
    private Instant untilTime;
    private Integer availableSeats;
    private Long startLocation;
    private Long endLocation;
    private TripStatus status;
}