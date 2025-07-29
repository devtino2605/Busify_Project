package com.busify.project.trip.controller;

import com.busify.project.trip.dto.TripDTO;
import com.busify.project.trip.dto.TripFilterRequestDTO;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.dto.response.TripRouteResponse;
import com.busify.project.trip.dto.response.TripStopResponse;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.service.impl.TripServiceImpl;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripServiceImpl tripService;

    @GetMapping
    public ApiResponse<List<TripDTO>> getAllTrips() {
        List<TripDTO> trips = tripService.getAllTrips();
        return ApiResponse.success("Lấy danh sách chuyến đi thành công", trips);
    }

    // get highlights trip;
    @GetMapping("/upcoming-trips")
    public ApiResponse<List<TripResponse>> getUpcomingTrips() {
        // List<TripResponse> trips = tripService.findTopUpcomingTripByOperator();
        // return ApiResponse.<List<TripResponse>>builder()
        // .code(HttpStatus.OK.value())
        // .message("Upcoming trips retrieved successfully")
        // .result(trips)
        // .build();
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

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getTripById(@PathVariable Long id) {
        try {
            return ApiResponse.success("Lấy thông tin chuyến đi thành công", tripService.getTripDetailById(id));
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lấy thông tin chuyến đi: " + e.getMessage());
        }
    }

    @GetMapping("/similar")
    public ApiResponse<List<TripRouteResponse>> getSimilarTrips(@RequestParam String routeId) {
        try {
            List<TripRouteResponse> tripRouteResponses = tripService.getTripRouteById(Long.parseLong(routeId));
            return ApiResponse.success("Lấy thông tin chuyến đi tương tự thành công", tripRouteResponses);
        } catch (Exception e) {
            return ApiResponse
                    .internalServerError("Đã xảy ra lỗi khi lấy thông tin chuyến đi tương tự: " + e.getMessage());
        }
    }

    @GetMapping("/{tripId}/stops")
    public ApiResponse<List<TripStopResponse>> getTripStops(@PathVariable Long tripId) {
        try {
            List<TripStopResponse> tripStops = tripService.getTripStopsById(tripId);
            return ApiResponse.success("Lấy thông tin các điểm dừng của chuyến đi thành công", tripStops);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lấy thông tin các điểm dừng: " + e.getMessage());
        }
    }
}
