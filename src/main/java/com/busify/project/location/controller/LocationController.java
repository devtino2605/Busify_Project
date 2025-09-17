package com.busify.project.location.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.location.dto.response.LocationForOperatorResponse;
import com.busify.project.location.service.LocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Tag(name = "Locations", description = "Location Management API")
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "Get all locations")
    @GetMapping
    public ApiResponse<List<LocationForOperatorResponse>> getAllLocations() {
        List<LocationForOperatorResponse> locations = locationService.getAllLocations();
        return ApiResponse.success("All locations fetched successfully", locations);
    }
}