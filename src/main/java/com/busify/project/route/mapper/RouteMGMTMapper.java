package com.busify.project.route.mapper;

import com.busify.project.route.dto.response.RouteMGMTResposeDTO;
import com.busify.project.route.dto.response.RouteStopDTO;
import com.busify.project.route.entity.Route;
import com.busify.project.route_stop.entity.RouteStop;
import com.busify.project.route_stop.repository.RouteStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RouteMGMTMapper {

    private final RouteStopRepository routeStopRepository;

    public RouteMGMTResposeDTO toRouteDetailResponseDTO(Route route) {
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

        // Lấy danh sách route stops
        List<RouteStop> routeStops = routeStopRepository.findByRouteIdOrderByStopOrder(route.getId());
        List<RouteStopDTO> routeStopDTOs = routeStops.stream()
                .map(this::toRouteStopDTO)
                .collect(Collectors.toList());
        dto.setRouteStops(routeStopDTOs);

        return dto;
    }

    private RouteStopDTO toRouteStopDTO(RouteStop routeStop) {
        RouteStopDTO dto = new RouteStopDTO();
        dto.setLocationId(routeStop.getLocation().getId());
        dto.setLocationName(routeStop.getLocation().getName());
        dto.setLocationCity(routeStop.getLocation().getCity());
        dto.setLocationAddress(routeStop.getLocation().getAddress());
        dto.setStopOrder(routeStop.getStopOrder());
        dto.setTimeOffsetFromStart(routeStop.getTimeOffsetFromStart());
        return dto;
    }
}
