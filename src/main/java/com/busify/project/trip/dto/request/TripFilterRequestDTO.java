package com.busify.project.trip.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
public class TripFilterRequestDTO {
    private Long startLocation;
    private Long endLocation;
    private Instant departureDate;
    private String[] busModels;
    private Instant untilTime;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "Time zone must be in the format 'region/city'")
    private String timeZone;
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Operator name can only contain alphanumeric characters and spaces")
    private String operatorName;
    private String[] amenities;
    private int availableSeats;
}
