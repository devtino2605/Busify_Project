package com.busify.project.trip.controller;

import com.busify.project.trip.dto.TripDTO;
import com.busify.project.trip.dto.TripFilterRequestDTO;
import com.busify.project.trip.service.TripService;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping
    public ApiResponse<List<TripDTO>> getAllTrips() {
        List<TripDTO> trips = tripService.getAllTrips();
        return ApiResponse.success("Lấy danh sách chuyến đi thành công", trips);
    }

    @PostMapping("/filter")
    public List<TripDTO> filterTrips(@RequestBody TripFilterRequestDTO filter) {
        return tripService.filterTrips(filter);
    }

}
