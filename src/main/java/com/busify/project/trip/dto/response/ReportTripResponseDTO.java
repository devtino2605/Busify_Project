package com.busify.project.trip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTripResponseDTO {
    private Long tripId;
    private String startLocation;
    private String endLocation;
    private Instant departureTime;
    private Instant arrivalTime;

    private Long totalPassengers;
    private BigDecimal totalIncome;
    private Long busId;
}
