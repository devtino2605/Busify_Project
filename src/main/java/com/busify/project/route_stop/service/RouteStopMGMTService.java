package com.busify.project.route_stop.service;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.route_stop.dto.request.RouteStopMGMTRequestDTO;
import com.busify.project.route_stop.dto.response.RouteStopDeleteResponseDTO;
import com.busify.project.route_stop.dto.response.RouteStopMGMTResponseDTO;

public interface RouteStopMGMTService {
    RouteStopMGMTResponseDTO addRouteStop(RouteStopMGMTRequestDTO requestDTO);

    RouteStopMGMTResponseDTO updateRouteStop(RouteStopMGMTRequestDTO requestDTO);

    RouteStopDeleteResponseDTO deleteRouteStop(Long routeId, Long locationId, boolean isDelete);

    ApiResponse<?> getStopsByRoute(Long routeId);
}
