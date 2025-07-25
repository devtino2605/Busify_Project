package com.busify.project.trip.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class TripFilterRequestDTO {
    private Long routeId;
    private Long operatorId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Long> seatLayoutIds;
    private LocalDate departureTime; // yyyy-MM-dd
    private String durationFilter; // e.g., "BETWEEN_3_AND_6"
    private Map<String, Object> amenities;
}
