package com.busify.project.route.controller;

import com.busify.project.route.dto.response.PopularRouteResponse;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.route.services.RouteService;
import com.busify.project.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @GetMapping("/popular-routes")
    public ApiResponse<List<PopularRouteResponse>> getPopularRoutes() {
        List<PopularRouteResponse> routes = routeService.getPopularRoutes();
        return ApiResponse.success("Popular routes fetched successfully", routes);
    }

    @GetMapping()
    public ApiResponse<List<RouteResponse>> getAllRoutes() {
        List<RouteResponse> routes = routeService.getAllRoutes();
        return ApiResponse.success("All routes fetched successfully", routes);
    }

}
