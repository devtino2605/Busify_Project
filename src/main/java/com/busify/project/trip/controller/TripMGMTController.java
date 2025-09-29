package com.busify.project.trip.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.dto.request.TripMGMTRequestDTO;
import com.busify.project.trip.dto.response.ReportTripResponseDTO;
import com.busify.project.trip.dto.response.TripDeleteResponseDTO;
import com.busify.project.trip.dto.response.TripMGMTResponseDTO;
import com.busify.project.trip.enums.TripStatus;
import com.busify.project.trip.service.TripMGMTService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip-management")
@RequiredArgsConstructor
@Tag(name = "Trip Management", description = "Trip Management API")
public class TripMGMTController {

    private final TripMGMTService tripMGMTService;

    @GetMapping
    @Operation(summary = "Get all trips with filters")
    public ApiResponse<?> getAllTrips(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TripStatus status,
            @RequestParam(required = false) String licensePlate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return tripMGMTService.getAllTrips(keyword, status, licensePlate, page, size);
    }

    @PostMapping
    @Operation(summary = "Add a new trip")
    public ApiResponse<TripMGMTResponseDTO> addTrip(@Valid @RequestBody TripMGMTRequestDTO requestDTO) {
        return ApiResponse.success("Thêm chuyến đi thành công", tripMGMTService.addTrip(requestDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update trip by ID")
    public ApiResponse<TripMGMTResponseDTO> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripMGMTRequestDTO requestDTO) {
        return ApiResponse.success("Cập nhật chuyến đi thành công", tripMGMTService.updateTrip(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete trip by ID")
    public ApiResponse<TripDeleteResponseDTO> deleteTrip(
            @PathVariable Long id,
            @RequestParam boolean isDelete) {
        String message = isDelete
                ? "Xóa chuyến đi thành công"
                : "Bạn đã xác nhận không xóa chuyến đi";

        return ApiResponse.success(message, tripMGMTService.deleteTrip(id, isDelete));
    }

    @GetMapping("/report/{operatorId}")
    @Operation(summary = "Get trip report by operator ID")
    public ApiResponse<List<ReportTripResponseDTO>> getReportTripByOperatorId(@PathVariable Long operatorId) {
        return tripMGMTService.reportTrips(operatorId);
    }
}
