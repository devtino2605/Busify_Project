package com.busify.project.route.services.impl;

import com.busify.project.route.dto.response.PopularRouteResponse;
import com.busify.project.route.dto.response.RouteFilterTripResponse;
import com.busify.project.route.mapper.RouteMapper;
import com.busify.project.route.repository.RouteRepository;
import com.busify.project.route.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteServiceImplement implements RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Override
    public List<RouteFilterTripResponse> getAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(RouteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PopularRouteResponse> getPopularRoutes() {
        return routeRepository.findPopularRoutes();
    }
}
