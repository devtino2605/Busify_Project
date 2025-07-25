package com.busify.project.trip.dto.response;

import com.busify.project.trip.enums.TripStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
public class TripFilterResponseDTO {
    private Long id;

    private Integer duration;

    private String operatorName;

    private RouteInfoResponseDTO route;

    private Map<String, Object> amenities;

    private Double averageRating;

    private Instant departureTime;
    private Instant estimatedArrivalTime;

    private TripStatus status;
    private BigDecimal pricePerSeat;
}
