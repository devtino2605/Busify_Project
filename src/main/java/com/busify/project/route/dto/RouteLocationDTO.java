package com.busify.project.route.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteLocationDTO {
    @JsonProperty("start_location")
    private String startLocation;

    @JsonProperty("end_location")
    private String endLocation;

    @JsonProperty("start_city")
    private String startCity;

    @JsonProperty("end_city")
    private String endCity;
}

