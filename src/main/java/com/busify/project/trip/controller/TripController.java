package com.busify.project.trip.controller;

import com.busify.project.trip.dto.TripDTO;
import com.busify.project.trip.dto.TripFilterRequestDTO;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.service.impl.TripServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import java.util.List;


@RestController
@RequestMapping("api/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripServiceImpl tripService;
    private final RestClient.Builder builder;

    @GetMapping
    public ApiResponse<List<TripDTO>> getAllTrips() {
        List<TripDTO> trips = tripService.getAllTrips();
        return ApiResponse.success("Lấy danh sách chuyến đi thành công", trips);
    }

    // get highlights trip;
    @GetMapping("/upcoming-trips")
    public ApiResponse<List<TripResponse>> getUpcomingTrips(){
//        List<TripResponse> trips = tripService.findTopUpcomingTripByOperator();
//        return ApiResponse.<List<TripResponse>>builder()
//                .code(HttpStatus.OK.value())
//                .message("Upcoming trips retrieved successfully")
//                .result(trips)
//                .build();
        try {
            List<TripResponse> trips = tripService.findTopUpcomingTripByOperator();
            return ApiResponse.success("Lấy danh sách các chuyến đi sắp tới thành công", trips);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lọc chuyến đi: " + e.getMessage());
        }
    }

    @PostMapping("/filter")
    public ApiResponse<List<TripDTO>> filterTrips(@RequestBody TripFilterRequestDTO filter) {
        try {
            List<TripDTO> filteredTrips = tripService.filterTrips(filter);
            return ApiResponse.success("Lọc chuyến đi thành công", filteredTrips);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lọc chuyến đi: " + e.getMessage());
        }
    }
}
