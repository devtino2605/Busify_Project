package com.busify.project.route.dto.response;


import com.busify.project.location.entity.Location;
import com.busify.project.route.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteResponse {
    private Long id;
    private String name;
    private String start_location;
    private String end_location;
    private Integer default_duration_minutes;
    private BigDecimal default_price;

    public static RouteResponse from(Route route) {
        if (route == null) {
            return null;
        }
        RouteResponse response = new RouteResponse();
        response.setId(route.getId());
        response.setName(route.getName());
        response.setEnd_location(route.getStartLocation().getName());
        response.setStart_location(route.getEndLocation().getName());
        response.setDefault_duration_minutes(route.getDefaultDurationMinutes());
        response.setDefault_price(route.getDefaultPrice());
        return response;
    }
}
