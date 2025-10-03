package com.busify.project.route.service;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.route.dto.request.RouteMGMTRequestDTO;
import com.busify.project.route.dto.response.RouteDeleteResponseDTO;
import com.busify.project.route.dto.response.RouteMGMTResposeDTO;

public interface RouteMGMTService {
    RouteMGMTResposeDTO addRoute(RouteMGMTRequestDTO requestDTO);

    RouteMGMTResposeDTO updateRoute(Long id, RouteMGMTRequestDTO requestDTO);

    RouteDeleteResponseDTO deleteRoute(Long id, boolean isDelete);

    ApiResponse<?> getAllRoutes(String keyword, int page, int size);
}
