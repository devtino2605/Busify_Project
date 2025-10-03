package com.busify.project.route_stop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteStopDeleteResponseDTO {
    private Long routeId;
    private Long locationId;
}
