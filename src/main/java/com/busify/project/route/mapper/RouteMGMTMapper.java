package com.busify.project.route.mapper;

import com.busify.project.route.dto.response.RouteMGMTResposeDTO;
import com.busify.project.route.entity.Route;
import org.springframework.stereotype.Component;

@Component
public class RouteMGMTMapper {

    public static RouteMGMTResposeDTO toRouteDetailResponseDTO(Route route) {
        if (route == null) return null;

        RouteMGMTResposeDTO dto = new RouteMGMTResposeDTO();
        dto.setId(route.getId());
        dto.setName(route.getName());
        dto.setStartLocationId(route.getStartLocation() != null ? route.getStartLocation().getId() : null);
        dto.setStartLocationName(route.getStartLocation() != null ? route.getStartLocation().getName() : null);
        dto.setStartLocationAddress(route.getStartLocation().getAddress() + ", " + route.getStartLocation().getCity());
        dto.setEndLocationId(route.getEndLocation() != null ? route.getEndLocation().getId() : null);
        dto.setEndLocationName(route.getEndLocation() != null ? route.getEndLocation().getName() : null);
        dto.setEndLocationAddress(route.getEndLocation().getAddress() + ", " + route.getEndLocation().getCity());
        dto.setDefaultDurationMinutes(route.getDefaultDurationMinutes());
        dto.setDefaultPrice(route.getDefaultPrice());

        return dto;
    }
}
