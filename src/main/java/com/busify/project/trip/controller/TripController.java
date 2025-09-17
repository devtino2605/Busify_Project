package com.busify.project.trip.controller;

import com.busify.project.trip.dto.response.FilterResponseDTO;
import com.busify.project.trip.dto.response.TopTripRevenueDTO;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.dto.request.TripUpdateStatusRequest;
import com.busify.project.trip.dto.response.TripByDriverResponseDTO;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.dto.response.TripResponseByRegionDTO;
import com.busify.project.trip.dto.response.TripRouteResponse;
import com.busify.project.trip.dto.response.TripStopResponse;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.service.impl.TripServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripServiceImpl tripService;

    @GetMapping
    public ApiResponse<List<TripFilterResponseDTO>> getAllTrips() {
        List<TripFilterResponseDTO> trips = tripService.getAllTrips();
        return ApiResponse.success("Lấy danh sách chuyến đi thành công", trips);
    }

    @GetMapping("/driver/my-trips")
    @PreAuthorize("hasRole('STAFF')")
    public ApiResponse<List<TripFilterResponseDTO>> getMyTrips() {
        try {
            List<TripFilterResponseDTO> trips = tripService.getTripsForCurrentDriver();
            return ApiResponse.success("Lấy danh sách chuyến đi của tài xế thành công", trips);
        } catch (IllegalStateException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lấy danh sách chuyến đi: " + e.getMessage());
        }
    }

    // Move specific paths BEFORE path variables
    @GetMapping("/upcoming-trips")
    public ApiResponse<List<TripResponse>> getUpcomingTrips() {
        try {
            List<TripResponse> trips = tripService.findTopUpcomingTripByOperator();
            return ApiResponse.success("Lấy danh sách các chuyến đi sắp tới thành công", trips);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lọc chuyến đi: " + e.getMessage());
        }
    }

    @PostMapping("/filter")
    public ApiResponse<FilterResponseDTO> filterTrips(@RequestBody TripFilterRequestDTO filter,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        try {
            FilterResponseDTO filteredTrips = tripService.filterTrips(filter, page, size);
            return ApiResponse.success("Lọc chuyến đi thành công", filteredTrips);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lọc chuyến đi: " + e.getMessage());
        }
    }

    @GetMapping("/similar/{tripId}")
    public ApiResponse<List<TripFilterResponseDTO>> getSimilarTrips(@PathVariable Long tripId) {
        try {
            List<TripFilterResponseDTO> tripRouteResponses = tripService.getTripRouteByIdExcludingTrip(tripId);
            return ApiResponse.success("Lấy thông tin chuyến đi tương tự thành công", tripRouteResponses);
        } catch (Exception e) {
            return ApiResponse
                    .internalServerError("Đã xảy ra lỗi khi lấy thông tin chuyến đi tương tự: " + e.getMessage());
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

    @GetMapping("/{tripId}/stops")
    public ApiResponse<List<TripStopResponse>> getTripStops(@PathVariable Long tripId) {
        try {
            List<TripStopResponse> tripStops = tripService.getTripStopsById(tripId);
            return ApiResponse.success("Lấy thông tin các điểm dừng của chuyến đi thành công", tripStops);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lấy thông tin các điểm dừng: " + e.getMessage());
        }
    }

    @GetMapping("/{operatorId}/trips")
    public ApiResponse<List<Map<String, Object>>> getNextTripsOfOperator(@PathVariable Long operatorId) {
        try {
            List<Map<String, Object>> nextTrips = tripService.getNextTripsOfOperator(operatorId);
            return ApiResponse.success("Lấy thông tin các chuyến đi sắp tới của nhà điều hành thành công", nextTrips);
        } catch (Exception e) {
            return ApiResponse
                    .internalServerError("Đã xảy ra lỗi khi lấy thông tin các chuyến đi sắp tới: " + e.getMessage());
        }
    }

    @GetMapping("/driver/{driverId}")
    public ApiResponse<List<TripByDriverResponseDTO>> getTripsByDriverId(@PathVariable Long driverId) {
        try {
            List<TripByDriverResponseDTO> trips = tripService.getTripsByDriverId(driverId);
            return ApiResponse.success("Lấy danh sách chuyến đi của tài xế thành công", trips);
        } catch (Exception e) {
            return ApiResponse
                    .internalServerError("Đã xảy ra lỗi khi lấy danh sách chuyến đi của tài xế: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{tripId}/status", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Map<String, Object>> updateTripStatus(
            @PathVariable Long tripId,
            @Valid @RequestBody TripUpdateStatusRequest request) {
        try {
            Map<String, Object> result = tripService.updateTripStatus(tripId, request);
            return ApiResponse.success("Cập nhật trạng thái chuyến đi thành công", result);
        } catch (IllegalArgumentException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (IllegalStateException e) {
            return ApiResponse.badRequest(e.getMessage());
        } catch (Exception e) {
            return ApiResponse
                    .internalServerError("Đã xảy ra lỗi khi cập nhật trạng thái chuyến đi: " + e.getMessage());
        }
    }

    @GetMapping("/admin/top-revenue-trips")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<TopTripRevenueDTO>> getTop10TripsByRevenue(
            @RequestParam(value = "year", required = false) Integer year) {

        List<TopTripRevenueDTO> topTrips;
        topTrips = tripService.getTop10TripsByRevenueAndYear(year);

        return ApiResponse.<List<TopTripRevenueDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Top 10 trips by revenue retrieved successfully")
                .result(topTrips)
                .build();
    }

    @GetMapping("/by-regions")
    public ApiResponse<TripResponseByRegionDTO> getTripsEachRegion() {
        TripResponseByRegionDTO tripsByRegion = tripService.getTripsEachRegion();
        return ApiResponse.success("Lấy số lượng chuyến đi theo từng vùng thành công", tripsByRegion);
    }
}
