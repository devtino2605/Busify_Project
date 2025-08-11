package com.busify.project.bus_operator.controller;

import com.busify.project.bus_operator.dto.response.BusOperatorDetailsResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorResponse;
import com.busify.project.bus_operator.service.imp.BusOperatorServiceImpl;
import com.busify.project.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/bus-operators")
@RequiredArgsConstructor
public class BusOperatorController {

    private final BusOperatorServiceImpl busOperatorService;

    @GetMapping("/rating")
    public ApiResponse<List<BusOperatorRatingResponse>> getAllBusOperatorsByRatingWithLimit(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<BusOperatorRatingResponse> busOperators = busOperatorService.getAllBusOperatorsByRating(limit);
         return ApiResponse.<List<BusOperatorRatingResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Upcoming trips retrieved successfully")
                .result(busOperators)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BusOperatorDetailsResponse> getOperatorById(@PathVariable Long id) {
        BusOperatorDetailsResponse busOperator = busOperatorService.getOperatorById(id);
        return ApiResponse.<BusOperatorDetailsResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Bus operator retrieved successfully")
                .result(busOperator)
                .build();
    }

    @GetMapping
    public ApiResponse<List<BusOperatorResponse>> getAllOperators() {
        List<BusOperatorResponse> busOperators = busOperatorService.getAllActiveOperators();
        return ApiResponse.success("All bus operators fetched successfully", busOperators);
    }
}