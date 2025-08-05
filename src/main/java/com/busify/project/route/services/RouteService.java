// package com.busify.project.route.services;

// import com.busify.project.route.dto.response.RouteResponse;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import com.busify.project.route.RouteRepository;

// import java.util.List;

// @Service
// public class RouteService {
//     @Autowired
//     private RouteRepository routeRepository;

//     public List<RouteResponse> getPopularRoutes() {
//         return routeRepository.findPopularRoutes();
//     }
// }

// package com.busify.project.route.services;

// import com.busify.project.route.dto.response.RouteResponse;
// import com.busify.project.route.RouteRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class RouteService {
//     @Autowired
//     private RouteRepository routeRepository;

//     public List<RouteResponse> getPopularRoutes() {
//         return routeRepository.findPopularRoutes();
//     }
// }

package com.busify.project.route.services;

import com.busify.project.route.repository.RouteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busify.project.route.dto.response.PopularRouteResponse;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.route.entity.Route;

import java.util.List;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    public List<PopularRouteResponse> getPopularRoutes() {
        return routeRepository.findPopularRoutes();
    }

    public List<RouteResponse> getAllRoutes() {
        List<Route> routes = routeRepository.findAll();
        return routes.stream()
                .map(RouteResponse::from)
                .toList();
    }
}