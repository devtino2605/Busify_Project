package com.busify.project.location.controller;

import com.busify.project.bus_model.dto.response.BusModelForOperatorResponse;
import com.busify.project.bus_model.service.BusModelService;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.location.dto.response.LocationForOperatorResponse;
import com.busify.project.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public ApiResponse<List<LocationForOperatorResponse>> getAllLocations() {
        List<LocationForOperatorResponse> locations = locationService.getAllLocations();
        return ApiResponse.success("All locations fetched successfully", locations);
    }
}
