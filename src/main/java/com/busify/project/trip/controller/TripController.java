package com.busify.project.trip.controller;


import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.service.impl.TripServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripServiceImpl tripService;
    private final RestClient.Builder builder;

    // get highlights trip;
    @GetMapping("/upcoming-trips")
    public ApiResponse<List<TripResponse>> getUpcomingTrips(){
        List<TripResponse> trips = tripService.findTopUpcomingTripByOperator();
        return ApiResponse.<List<TripResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Upcoming trips retrieved successfully")
                .result(trips)
                .build();
    }
}
