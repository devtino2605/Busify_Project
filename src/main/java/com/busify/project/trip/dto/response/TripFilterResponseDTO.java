package com.busify.project.trip.dto.response;

import com.busify.project.trip.enums.TripStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class TripFilterResponseDTO {
    private Long trip_id;

    private String operator_name;
    private String operator_avatar;

    private RouteInfoResponseDTO route;

    private Map<String, Object> amenities;

    private Double average_rating;

    private LocalDateTime departure_time;
    private LocalDateTime arrival_time;

    private TripStatus status;
    private BigDecimal price_per_seat;

    private Integer available_seats;
    private Integer total_seats;
}
