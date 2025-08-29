package com.busify.project.bus_model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.bus_model.dto.response.BusModelForOperatorResponse;
import com.busify.project.bus_model.service.BusModelService;
import com.busify.project.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/bus-models")
@RequiredArgsConstructor
public class BusModelController {

    private final BusModelService busModelService;

    @GetMapping
    public ApiResponse<List<BusModelForOperatorResponse>> getAllBusModels() {
        List<BusModelForOperatorResponse> busModels = busModelService.getAllBusModels();
        return ApiResponse.success("All bus models fetched successfully", busModels);
    }
}
