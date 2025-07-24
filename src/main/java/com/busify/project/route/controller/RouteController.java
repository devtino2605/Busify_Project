package com.busify.project.route.controller;

import com.busify.project.route.dto.RouteResponse;
import com.busify.project.route.services.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RouteController {
    @Autowired
    private RouteService routeService;

    @GetMapping("/popular-routes")
    public List<RouteResponse> getPopularRoutes() {
        return routeService.getPopularRoutes();
    }
}