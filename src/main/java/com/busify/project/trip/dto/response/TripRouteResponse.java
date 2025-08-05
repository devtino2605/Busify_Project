package com.busify.project.trip.dto.response;

import com.busify.project.route.dto.RouteLocationDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import java.time.Instant;

public interface TripRouteResponse {
    @JsonProperty("tripId")
    Long getTripId();

    @JsonProperty("operatorName")
    String getOperatorName();

    @JsonProperty("route")
    @Value("#{new com.busify.project.route.dto.RouteLocationDTO(target.startLocation, target.endLocation)}")
    RouteLocationDTO getRoute();

    @JsonProperty("departureTime")
    Instant getDepartureTime();

    @JsonProperty("arrivalEstimateTime")
    Instant getArrivalEstimateTime();

    @JsonProperty("durationMinutes")
    Integer getDurationMinutes();

    @JsonProperty("pricePerSeat")
    Double getPricePerSeat();

    @JsonProperty("availableSeats")
    Integer getAvailableSeats();

    @JsonProperty("averageRating")
    Double getAverageRating();

    @JsonProperty("status")
    String getStatus();
}
