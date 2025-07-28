package com.busify.project.route.mapper;

import com.busify.project.route.dto.response.RouteFilterTripResponse;
import com.busify.project.route.entity.Route;

public class RouteMapper {

    public static RouteFilterTripResponse toDTO(Route route) {
        if (route == null) return null;

        RouteFilterTripResponse dto = new RouteFilterTripResponse();
        dto.setId(route.getId());
        dto.setName(route.getName());

        return dto;
    }
}
