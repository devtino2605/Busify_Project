package com.busify.project.trip.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TripFilterRequestDTO {
    private String startCity;
    private String endCity;
    private LocalDateTime departureDate;
    private String[] busModels;
    private LocalDateTime untilTime;
    @Pattern(regexp = "^[a-zA-Z0-9_/]+$", message = "Time zone must be in the format 'region/city'")
    private String timeZone = "Asia/Ho_Chi_Minh";
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Operator name can only contain alphanumeric characters and spaces")
    private String operatorName;
    private String[] amenities;
    private int availableSeats;
    private String sortBy;
    private String sortDirection = "ASC";
    private String sortBySecondary;
    private String sortDirectionSecondary;
}