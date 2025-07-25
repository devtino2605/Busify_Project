package com.busify.project.trip.controller;

import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.service.impl.TripServiceImpl;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;

import java.util.List;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripServiceImpl tripService;
    private final RestClient.Builder builder;

    @GetMapping
    public ApiResponse<List<TripFilterResponseDTO>> getAllTrips() {
        List<TripFilterResponseDTO> trips = tripService.getAllTrips();
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
    public ApiResponse<List<TripFilterResponseDTO>> filterTrips(@RequestBody TripFilterRequestDTO filter) {
        try {
            List<TripFilterResponseDTO> filteredTrips = tripService.filterTrips(filter);
            return ApiResponse.success("Lọc chuyến đi thành công", filteredTrips);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lọc chuyến đi: " + e.getMessage());
        }
    }
}
