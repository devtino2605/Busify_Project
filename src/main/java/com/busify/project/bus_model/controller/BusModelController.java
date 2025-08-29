package com.busify.project.bus_model.controller;

import com.busify.project.bus_model.dto.response.BusModelForOperatorResponse;
import com.busify.project.bus_model.service.BusModelService;
import com.busify.project.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bus-models")
public class BusModelController {

    private final BusModelService busModelService;

    @GetMapping
    public ApiResponse<List<BusModelForOperatorResponse>> getAllBusModels() {
        List<BusModelForOperatorResponse> busModels = busModelService.getAllBusModels();
        return ApiResponse.success("All bus models fetched successfully", busModels);
    }
}
