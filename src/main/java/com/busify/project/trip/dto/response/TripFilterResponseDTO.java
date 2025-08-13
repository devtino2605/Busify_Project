package com.busify.project.trip.dto.response;

import com.busify.project.trip.enums.TripStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
public class TripFilterResponseDTO {
    private Long trip_id;

//    private Integer duration;

    private String operator_name;

    private RouteInfoResponseDTO route;

    private Map<String, Object> amenities;

    private Double average_rating;

    private Instant departure_time;
    private Instant arrival_time;

    private TripStatus status;
    private BigDecimal price_per_seat;

    private Integer available_seats;
}
