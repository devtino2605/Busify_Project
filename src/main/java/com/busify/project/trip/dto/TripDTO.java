package com.busify.project.trip.dto;

import com.busify.project.trip.enums.TripStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
public class TripDTO {
    private Long id;

    private Long routeId;
    private String routeName; // Nếu bạn có tên tuyến đường
    private Integer duration;

    private Long busId;
    private String busPlateNumber; // Nếu bạn có

    private Long operatorId;

    private Integer seatLayoutId;
    private String seatLayoutName;

    private Map<String, Object> amenities;

    private Instant departureTime;
    private Instant estimatedArrivalTime;

    private TripStatus status;
    private BigDecimal pricePerSeat;
}
