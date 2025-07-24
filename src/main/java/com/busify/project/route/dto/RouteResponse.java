

// package com.busify.project.route.dto;

// public class RouteResponse {
//     private Long routeId;
//     private String routeName;
//     private String durationHours;
//     private Double startingPrice;

//     // Constructors
//     public RouteResponse() {}

//     public RouteResponse(Long routeId, String routeName, String durationHours, Double startingPrice) {
//         this.routeId = routeId;
//         this.routeName = routeName;
//         this.durationHours = durationHours;
//         this.startingPrice = startingPrice;
//     }

//     // Getters and Setters
//     public Long getRouteId() { return routeId; }
//     public void setRouteId(Long routeId) { this.routeId = routeId; }

//     public String getRouteName() { return routeName; }
//     public void setRouteName(String routeName) { this.routeName = routeName; }

//     public String getDurationHours() { return durationHours; }
//     public void setDurationHours(String durationHours) { this.durationHours = durationHours; }

//     public Double getStartingPrice() { return startingPrice; }
//     public void setStartingPrice(Double startingPrice) { this.startingPrice = startingPrice; }
// }


package com.busify.project.route.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private Long routeId; // Sử dụng routeId thay vì id để khớp với cột trong database
    private String routeName;
    private String durationHours;
    private BigDecimal startingPrice;
}