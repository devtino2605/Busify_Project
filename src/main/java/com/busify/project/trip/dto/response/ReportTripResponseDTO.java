package com.busify.project.trip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportTripResponseDTO {
    private Long tripId;
    private String startLocation;
    private String endLocation;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private Long totalPassengers;
    private BigDecimal totalIncome;
    private Long busId;
}
