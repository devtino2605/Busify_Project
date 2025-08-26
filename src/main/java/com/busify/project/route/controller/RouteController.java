package com.busify.project.route.controller;

import com.busify.project.route.dto.response.PopularRouteResponse;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.route.dto.response.TopRouteRevenueDTO;
import com.busify.project.route.service.RouteService;
import com.busify.project.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // Admin endpoint: Top 10 routes có doanh thu cao nhất
    @GetMapping("/admin/top-revenue-routes")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<TopRouteRevenueDTO>> getTop10RoutesByRevenue(
            @RequestParam(value = "year", required = false) Integer year) {

        List<TopRouteRevenueDTO> topRoutes;
        topRoutes = routeService.getTop10RoutesByRevenueAndYear(year);

        return ApiResponse.<List<TopRouteRevenueDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Top 10 routes by revenue retrieved successfully")
                .result(topRoutes)
                .build();
    }

    // Admin endpoint: Top 10 trips có doanh thu cao nhất

}
