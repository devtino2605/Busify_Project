package com.busify.project.route.services.impl;

import com.busify.project.route.dto.response.RouteFilterTripResponse;
import com.busify.project.route.mapper.RouteMapper;
import com.busify.project.route.repository.RouteRepository;
import com.busify.project.route.services.RouteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteServiceImplement extends RouteService {

    RouteServiceImplement(RouteRepository routeRepository) {
        super(routeRepository);
    }

    public List<RouteFilterTripResponse> getRouteFilterTripResponses() {
        return routeRepository.findAll().stream()
                .map(RouteMapper::toDTO)
                .collect(Collectors.toList());
    }
}
