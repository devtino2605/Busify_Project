package com.busify.project.route.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteMGMTResposeDTO {
    private Long id;
    private String name;
    private Long startLocationId;
    private String startLocationName;
    private String startLocationAddress;
    private Long endLocationId;
    private String endLocationName;
    private String endLocationAddress;
    private Integer defaultDurationMinutes;
    private BigDecimal defaultPrice;
}
