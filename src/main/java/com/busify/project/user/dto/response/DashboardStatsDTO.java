package com.busify.project.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dashboard statistics data")
public class DashboardStatsDTO {
    
    @Schema(description = "Total number of users", example = "1250")
    private Long totalUsers;
    
    @Schema(description = "Total number of vehicles", example = "85")
    private Long totalVehicles;
    
    @Schema(description = "Total number of routes", example = "45")
    private Long totalRoutes;
    
    @Schema(description = "Monthly revenue in VND", example = "15000000")
    private Double monthlyRevenue;
    
    @Schema(description = "Number of active routes", example = "42")
    private Long activeRoutes;
    
    @Schema(description = "Number of pending users", example = "15")
    private Long pendingUsers;
    
    @Schema(description = "Number of today bookings", example = "125")
    private Long todayBookings;
    
    @Schema(description = "Number of pending complaints", example = "8")
    private Long pendingComplaints;
    
    @Schema(description = "Number of completed trips", example = "2850")
    private Long completedTrips;
    
    @Schema(description = "Number of cancelled trips", example = "45")
    private Long cancelledTrips;
}