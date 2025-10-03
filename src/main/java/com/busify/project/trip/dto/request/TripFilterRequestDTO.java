package com.busify.project.trip.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TripFilterRequestDTO {
    private Long startLocation;
    private Long endLocation;
    private Instant departureDate;
    private String[] busModels;
    private Instant untilTime;
    @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "Time zone must be in the format 'region/city'")
    private String timeZone = "Asia/Ho_Chi_Minh";
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Operator name can only contain alphanumeric characters and spaces")
    private String operatorName;
    private String[] amenities;
    private int availableSeats;
    private String sortBy = "departureTime";
    private String sortDirection = "ASC";
    private String sortBySecondary;
    private String sortDirectionSecondary;
}
