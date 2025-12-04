package com.busify.project.trip.dto.response;

import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.trip.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripResponse {
    private Long trip_id;
    private String operator_name;
    private RouteResponse route;
    private LocalDateTime departure_time;
    private LocalDateTime arrival_time;
    private Integer available_seats;
    private double average_rating;
    private TripStatus status;
    private BigDecimal price_per_seat;
}
