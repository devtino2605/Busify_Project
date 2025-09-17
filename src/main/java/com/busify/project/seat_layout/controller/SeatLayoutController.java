package com.busify.project.seat_layout.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.seat_layout.dto.response.SeatLayoutFilterTripResponse;
import com.busify.project.seat_layout.service.SeatLayoutService;
import com.busify.project.seat_layout.entity.SeatLayout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seat-layout")
@RequiredArgsConstructor
@Tag(name = "Seat Layout", description = "Seat Layout Management API")
public class SeatLayoutController {
    private final SeatLayoutService seatLayoutService;

    @Operation(summary = "Get all seat layouts")
    @GetMapping
    public ApiResponse<List<SeatLayoutFilterTripResponse>> getAllSeatLayouts() {
        List<SeatLayoutFilterTripResponse> seatLayouts = seatLayoutService.getAllSeatLayouts();
        return ApiResponse.success("Lấy danh sách layout ghế thành công", seatLayouts);
    }

    @Operation(summary = "Get seat layout by trip ID")
    @GetMapping("/trip/{id}")
    public ApiResponse<Optional<SeatLayout>> getSeatLayoutByTripId(@PathVariable Integer id) {
        Optional<SeatLayout> seatLayout = seatLayoutService.getSeatLayoutByTripId(Long.valueOf(id));
        return ApiResponse.success("Lấy layout ghế theo trip thành công", seatLayout);
    }
}