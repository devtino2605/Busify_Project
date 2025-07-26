// Controller
package com.busify.project.bus_operator.controller;

import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;
import com.busify.project.bus_operator.service.BusOperatorService;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bus-operators")
@RequiredArgsConstructor
public class BusOperatorController {

    private final BusOperatorService busOperatorService;

    @GetMapping("/rating")
    public ApiResponse<List<BusOperatorRatingResponse>> getAllBusOperatorsByRatingWithLimit(
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        try {
            List<BusOperatorRatingResponse> busOperators = busOperatorService.getAllBusOperatorsByRating(limit);
            return ApiResponse.success("Lọc chuyến đi thành công", busOperators);
        } catch (Exception e) {
            return ApiResponse.internalServerError("Đã xảy ra lỗi khi lọc chuyến đi: " + e.getMessage());
        }
    }
}