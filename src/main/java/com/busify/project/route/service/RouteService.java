package com.busify.project.route.service;

import com.busify.project.route.dto.response.PopularRouteResponse;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.route.dto.response.TopRouteRevenueDTO;
import com.busify.project.route.entity.Route;
import com.busify.project.route.repository.RouteRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService {
    protected final RouteRepository routeRepository;

    public List<PopularRouteResponse> getPopularRoutes() {
        return routeRepository.findPopularRoutes();
    }

    public List<RouteResponse> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        return routes.stream()
                .map(RouteResponse::from)
                .collect(Collectors.toList());
    }

    // Lấy top 10 routes có doanh thu cao nhất theo năm
    public List<TopRouteRevenueDTO> getTop10RoutesByRevenueAndYear(Integer year) {
        LocalDate now = LocalDate.now();
        int reportYear = (year != null) ? year : now.getYear();
        return routeRepository.findTop10RoutesByRevenueAndYear(reportYear);
    }

}