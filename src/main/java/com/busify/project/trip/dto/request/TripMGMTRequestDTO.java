package com.busify.project.trip.dto.request;

import com.busify.project.trip.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripMGMTRequestDTO {
    private Long routeId;
    private Long busId;
    private Long driverId;
    private Instant departureTime;
    private BigDecimal pricePerSeat;
    private TripStatus status;
}
