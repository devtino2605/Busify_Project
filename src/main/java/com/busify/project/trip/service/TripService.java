package com.busify.project.trip.service;

import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.dto.response.TripResponse;

import java.util.List;

public interface TripService {
    List<TripFilterResponseDTO> getAllTrips();
    List<TripFilterResponseDTO> filterTrips(TripFilterRequestDTO filter);
    List<TripResponse> findTopUpcomingTripByOperator();
}
