package com.busify.project.bus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.bus.dto.response.BusLayoutResponseDTO;
import com.busify.project.bus.service.BusService;
import com.busify.project.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("api/bus")
@RestController
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @GetMapping("/layout/{busId}")
    public ApiResponse<BusLayoutResponseDTO> getBusSeatLayoutMap(@PathVariable Long busId) {
        return ApiResponse.success("Lấy sơ đồ ghế xe thành công", busService.getBusSeatLayoutMap(busId));
    }

}