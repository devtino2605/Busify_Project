package com.busify.project.route.dto.response;

import java.math.BigDecimal;

import com.busify.project.route.entity.Route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteResponse {
    private Long id;
    private String name;
    private String startLocation;
    private String endLocation;
    private Integer defaultDurationMinutes;
    private BigDecimal defaultPrice;

    public static RouteResponse from(Route route) {
        if (route == null) {
            return null;
        }
        RouteResponse response = new RouteResponse();
        response.setId(route.getId());
        response.setName(route.getName());
        response.setEndLocation(route.getStartLocation().getName());
        response.setStartLocation(route.getEndLocation().getName());
        response.setDefaultDurationMinutes(route.getDefaultDurationMinutes());
        response.setDefaultPrice(route.getDefaultPrice());
        return response;
    }
}
