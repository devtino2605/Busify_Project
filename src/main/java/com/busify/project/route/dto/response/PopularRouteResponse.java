package com.busify.project.route.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularRouteResponse {
    private Long routeId; // Sử dụng routeId thay vì id để khớp với cột trong database
    private String routeName;
    private String durationHours;
    private BigDecimal startingPrice;

    // private Long startLocationId;
    // private Long endLocationId;
    // private Integer defaultDurationMinutes;
    // private Double defaultPrice;
}