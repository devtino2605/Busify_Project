// package com.busify.project.route.services;

// import com.busify.project.route.dto.RouteResponse;
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

// import com.busify.project.route.dto.RouteResponse;
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

import com.busify.project.route.dto.RouteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.busify.project.route.RouteRepository;

import java.util.List;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    public List<RouteResponse> getPopularRoutes() {
        return routeRepository.findPopularRoutes();
    }
}