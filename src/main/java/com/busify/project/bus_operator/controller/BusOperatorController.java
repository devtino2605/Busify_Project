// Controller
package com.busify.project.bus_operator.controller;

import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;
import com.busify.project.bus_operator.service.BusOperatorService;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.dto.response.TripResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        List<BusOperatorRatingResponse> busOperators = busOperatorService.getAllBusOperatorsByRating(limit);
         return ApiResponse.<List<BusOperatorRatingResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Upcoming trips retrieved successfully")
                .result(busOperators)
                .build();
    }

}