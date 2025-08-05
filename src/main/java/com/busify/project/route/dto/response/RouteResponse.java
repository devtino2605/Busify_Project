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
    private String start_location;
    private String end_location;
    private Integer default_duration_minutes;
    private BigDecimal default_price;
}
