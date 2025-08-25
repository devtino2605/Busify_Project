package com.busify.project.trip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripByDriverResponseDTO {
    private Long tripId;
    private Instant departureTime;
    private Instant estimatedArrivalTime;
    private String status;
    private BigDecimal pricePerSeat;
    private String operatorName;
    private Long routeId;
    private String startCity;
    private String startAddress;
    private String endCity;
    private String endAddress;
    private String busLicensePlate;
    private String busModel;
    private Integer availableSeats;
    private Integer totalSeats;
    private Double averageRating;
}
