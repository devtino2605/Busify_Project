package com.busify.project.route_stop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteStopMGMTResponseDTO {
    private Long routeId;
    private Long locationId;
    private String routeName;
    private String locationName;
    private String locationAddress;
    private Integer stopOrder;
    private Integer timeOffsetFromStart;
}
