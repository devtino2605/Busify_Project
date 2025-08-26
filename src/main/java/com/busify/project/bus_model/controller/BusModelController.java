package com.busify.project.bus_model.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.bus_model.entity.BusModel;
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
    public ApiResponse<List<BusModel>> getAllBusModels() {
        List<BusModel> busModels = busModelService.getAllBusModels();
        return ApiResponse.success(busModels);
    }
}
