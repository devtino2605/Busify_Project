package com.busify.project.trip.dto.response;

import com.busify.project.trip.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripMGMTResponseDTO {
    private Long id;
    private Long routeId;
    private String routeName;
    private Long busId;
    private Long driverId;
    private Instant departureTime;
    private Instant estimatedArrivalTime;
    private TripStatus status;
    private BigDecimal pricePerSeat;
}
