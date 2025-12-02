package com.busify.project.trip.dto.request;

import com.busify.project.trip.enums.TripStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TripSearchRequestDTO {
    private LocalDateTime departureDate;
    private LocalDateTime untilTime;
    private Integer availableSeats;
    private Long startLocation;
    private Long endLocation;
    private TripStatus status;
}