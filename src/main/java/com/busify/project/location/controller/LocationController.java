package com.busify.project.location.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.location.dto.response.LocationForOperatorResponse;
import com.busify.project.location.service.LocationService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;
    
    @GetMapping
    public ApiResponse<List<LocationForOperatorResponse>> getAllLocations() {
        List<LocationForOperatorResponse> locations = locationService.getAllLocations();
        return ApiResponse.success("All locations fetched successfully", locations);
    }
}
