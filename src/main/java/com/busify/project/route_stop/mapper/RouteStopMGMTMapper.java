package com.busify.project.route_stop.mapper;

import com.busify.project.route_stop.dto.response.RouteStopMGMTResponseDTO;
import com.busify.project.route_stop.entity.RouteStop;

public class RouteStopMGMTMapper {

    public static RouteStopMGMTResponseDTO toResponseDTO(RouteStop entity) {
        if (entity == null) return null;

        RouteStopMGMTResponseDTO dto = new RouteStopMGMTResponseDTO();
        dto.setRouteId(entity.getRoute().getId());
        dto.setLocationId(entity.getLocation().getId());
        dto.setRouteName(entity.getRoute().getName());
        dto.setLocationName(entity.getLocation().getName());
        dto.setLocationAddress(entity.getLocation().getAddress() + ", " + entity.getLocation().getCity());
        dto.setStopOrder(entity.getStopOrder());
        dto.setTimeOffsetFromStart(entity.getTimeOffsetFromStart());
        return dto;
    }
}
