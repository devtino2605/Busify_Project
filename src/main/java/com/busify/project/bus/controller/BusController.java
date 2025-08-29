package com.busify.project.bus.controller;

import com.busify.project.bus.dto.response.BusForOperatorResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.bus.dto.response.BusDetailResponseDTO;
import com.busify.project.bus.dto.response.BusLayoutResponseDTO;
import com.busify.project.bus.service.BusService;
import com.busify.project.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("api/bus")
@RestController
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @GetMapping
    public ApiResponse<List<BusForOperatorResponse>> getAllBuses() {
        List<BusForOperatorResponse> buses = busService.getAllBuses();
        return ApiResponse.success("All buses fetched successfully", buses);
    }

    @GetMapping("/layout/{busId}")
    public ApiResponse<BusLayoutResponseDTO> getBusSeatLayoutMap(@PathVariable Long busId) {
        return ApiResponse.success("Lấy sơ đồ ghế xe thành công", busService.getBusSeatLayoutMap(busId));
    }

    @GetMapping("/operator/{id}")
    public ApiResponse<List<BusDetailResponseDTO>> getBusesByOperatorId(@PathVariable Long id) {
        List<BusDetailResponseDTO> buses = busService.getBusesByOperatorId(id);
        return ApiResponse.success("Buses retrieved successfully", buses);
    }
}