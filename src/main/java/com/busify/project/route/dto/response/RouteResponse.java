package com.busify.project.route.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private Long id;
    private String name;
    private String startLocation;
    private String endLocation;
    private Integer defaultDurationMinutes;
    private String defaultPrice;
}
