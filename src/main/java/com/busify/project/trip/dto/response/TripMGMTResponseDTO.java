package com.busify.project.trip.dto.response;

import com.busify.project.trip.enums.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripMGMTResponseDTO {
    private Long id;
    private Long routeId;
    private String routeName;
    private Long busId;
    private String licensePlate;
    private Long driverId;
    private String driverName;
    private LocalDateTime departureTime;
    private LocalDateTime estimatedArrivalTime;
    private TripStatus status;
    private BigDecimal pricePerSeat;
    private Integer availableSeats;
    private Integer totalSeats;
}
