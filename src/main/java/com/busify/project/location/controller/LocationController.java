package com.busify.project.location.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.location.dto.response.LocationDTO;
import com.busify.project.location.dto.response.LocationForOperatorResponse;
import com.busify.project.location.service.LocationService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * Lấy tất cả locations để làm dropdown cho việc chọn điểm dừng
     * GET /api/locations/dropdown
     */
    @GetMapping("/dropdown")
    public ApiResponse<List<LocationDTO>> getAllLocationsForDropdown() {
        return locationService.getAllLocationsForDropdown();
    }

    /**
     * Tìm kiếm locations theo keyword để làm dropdown
     * GET /api/locations/dropdown/search?keyword=hanoi
     */
    @GetMapping("/dropdown/search")
    public ApiResponse<List<LocationDTO>> searchLocationsForDropdown(
            @RequestParam(required = false) String keyword
    ) {
        return locationService.searchLocationsForDropdown(keyword);
    }
}
