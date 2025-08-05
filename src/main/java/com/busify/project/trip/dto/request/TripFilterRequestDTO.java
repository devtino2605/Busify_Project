package com.busify.project.trip.dto.request;

import lombok.Data;
@Data
public class TripFilterRequestDTO {
    private Long routeId;
    private Long[] busOperatorIds;
    private String departureDate;
    private String[] busModels;
    private String untilTime;
    private Integer availableSeats;
    private String[] amenities;
}
