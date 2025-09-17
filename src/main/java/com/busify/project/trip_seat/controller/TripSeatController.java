package com.busify.project.trip_seat.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.trip_seat.dto.TripSeatsStatusReponse;
import com.busify.project.trip_seat.services.TripSeatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trip-seats")
@Tag(name = "Trip Seats", description = "Trip Seat Management API")
public class TripSeatController {
    @Autowired
    private TripSeatService tripSeatService;

    @Operation(summary = "Get seat availability by trip ID")
    @GetMapping("/{tripId}")
    public ResponseEntity<ApiResponse<TripSeatsStatusReponse>> getSeatAvailability(@PathVariable Long tripId) {
        TripSeatsStatusReponse response = new TripSeatsStatusReponse(tripId,
                tripSeatService.getTripSeatsStatus(tripId));
        final ApiResponse<TripSeatsStatusReponse> apiResponse = ApiResponse.success(
                "Trip seats status fetched successfully",
                response);
        return ResponseEntity.ok(apiResponse);
    }
}