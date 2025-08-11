package com.busify.project.route.services;

import com.busify.project.route.dto.response.PopularRouteResponse;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.route.entity.Route;
import com.busify.project.route.repository.RouteRepository;

import lombok.RequiredArgsConstructor;

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
}