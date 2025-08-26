package com.busify.project.seat_layout.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.seat_layout.dto.response.SeatLayoutFilterTripResponse;
import com.busify.project.seat_layout.service.SeatLayoutService;
import com.busify.project.seat_layout.entity.SeatLayout;
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
public class SeatLayoutController {
    private final SeatLayoutService seatLayoutService;

    @GetMapping
    public ApiResponse<List<SeatLayoutFilterTripResponse>> getAllSeatLayouts() {
        List<SeatLayoutFilterTripResponse> seatLayouts = seatLayoutService.getAllSeatLayouts();
        return ApiResponse.success("Lấy danh sách tuyến đường thành công thành công", seatLayouts);
    }

    @GetMapping("/trip/{id}")
    public ApiResponse<Optional<SeatLayout>> getSeatLayoutByTripId(@PathVariable Integer id) {
        // covert to long
        Optional<SeatLayout> seatLayout = seatLayoutService.getSeatLayoutByTripId(Long.valueOf(id));
        return ApiResponse.success("Lấy bố trí ghế cho chuyến đi thành công", seatLayout);
    }

}
