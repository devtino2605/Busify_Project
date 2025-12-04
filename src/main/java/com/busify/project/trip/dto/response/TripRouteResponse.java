package com.busify.project.trip.dto.response;

import com.busify.project.route.dto.RouteLocationDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;

public interface TripRouteResponse {
    @JsonProperty("trip_id")
    Long getTripId();

    @JsonProperty("operator_name")
    String getOperatorName();

    @JsonProperty("operator_avatar")
    String getOperatorAvatar();

    @JsonProperty("route")
    @Value("#{new com.busify.project.route.dto.RouteLocationDTO(target.startLocation, target.endLocation, target.startCity, target.endCity)}")
    RouteLocationDTO getRoute();

    @JsonProperty("departure_time")
    LocalDateTime getDepartureTime();

    @JsonProperty("arrival_time")
    LocalDateTime getArrivalEstimateTime();

    @JsonProperty("duration_minutes")
    Integer getDurationMinutes();

    @JsonProperty("price_per_seat")
    Double getPricePerSeat();

    @JsonProperty("available_seats")
    Integer getAvailableSeats();

    @JsonProperty("average_rating")
    Double getAverageRating();

    @JsonProperty("status")
    String getStatus();
}
