package com.busify.project.bus_operator.controller;

import com.busify.project.bus_operator.dto.request.BusOperatorFilterRequest;
import com.busify.project.bus_operator.dto.request.CreateBusOperatorRequest;
import com.busify.project.bus_operator.dto.request.UpdateBusOperatorRequest;
import com.busify.project.bus_operator.dto.response.BusOperatorDetailsResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorForManagement;
import com.busify.project.bus_operator.dto.response.BusOperatorManagementPageResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;
import com.busify.project.bus_operator.dto.response.BusOperatorResponse;
import com.busify.project.bus_operator.dto.response.WeeklyBusOperatorReportDTO;
import com.busify.project.bus_operator.service.imp.BusOperatorServiceImpl;
import com.busify.project.common.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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

    @GetMapping("/management")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BusOperatorManagementPageResponse> getBusOperatorsForManagementPaginated(
            BusOperatorFilterRequest filterRequest) {
        BusOperatorManagementPageResponse response = busOperatorService.getBusOperatorsForManagement(filterRequest);
        return ApiResponse.<BusOperatorManagementPageResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Bus operators for management fetched successfully with pagination and filters")
                .result(response)
                .build();
    }

    @PostMapping("/management")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BusOperatorForManagement> createBusOperator(
            @Valid @ModelAttribute CreateBusOperatorRequest request) {
        BusOperatorForManagement busOperator = busOperatorService.createBusOperator(request);
        return ApiResponse.<BusOperatorForManagement>builder()
                .code(HttpStatus.CREATED.value())
                .message("Bus operator created successfully")
                .result(busOperator)
                .build();
    }

    @GetMapping("/management/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BusOperatorForManagement> getBusOperatorForManagement(@PathVariable Long id) {
        BusOperatorForManagement busOperator = busOperatorService.getBusOperatorForManagementById(id);
        return ApiResponse.<BusOperatorForManagement>builder()
                .code(HttpStatus.OK.value())
                .message("Bus operator retrieved successfully")
                .result(busOperator)
                .build();
    }

    @PutMapping("/management/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BusOperatorForManagement> updateBusOperator(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateBusOperatorRequest request) {
        BusOperatorForManagement busOperator = busOperatorService.updateBusOperator(id, request);
        return ApiResponse.<BusOperatorForManagement>builder()
                .code(HttpStatus.OK.value())
                .message("Bus operator updated successfully")
                .result(busOperator)
                .build();
    }

    @DeleteMapping("/management/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteBusOperator(@PathVariable Long id) {
        busOperatorService.deleteBusOperator(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Bus operator deleted successfully")
                .build();
    }

    @GetMapping("/{id}/report")
    public ApiResponse<WeeklyBusOperatorReportDTO> getWeeklyReportByOperatorId(@PathVariable Long id) {
        WeeklyBusOperatorReportDTO report = busOperatorService.getWeeklyReportByOperatorId(id);
        return ApiResponse.<WeeklyBusOperatorReportDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Weekly report retrieved successfully")
                .result(report)
                .build();
    }

    @GetMapping("/user")
    public ApiResponse<BusOperatorResponse> getOperatorInfo() {
        BusOperatorResponse operatorInfo = busOperatorService.getOperatorDetailByUser();
        return ApiResponse.<BusOperatorResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Bus operator info retrieved successfully")
                .result(operatorInfo)
                .build();
    }

}