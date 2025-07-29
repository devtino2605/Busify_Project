package com.busify.project.trip.service;

import com.busify.project.trip.dto.TripDTO;
import com.busify.project.trip.dto.TripFilterRequestDTO;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.dto.response.TripRouteResponse;
import com.busify.project.trip.dto.response.TripStopResponse;

import java.util.List;
import java.util.Map;

public interface TripService {
    List<TripDTO> getAllTrips();

    List<TripDTO> filterTrips(TripFilterRequestDTO filter);

    List<TripResponse> findTopUpcomingTripByOperator();

    Map<String, Object> getTripDetailById(Long tripId);

    List<TripRouteResponse> getTripRouteById(Long tripId);

    List<TripStopResponse> getTripStopsById(Long tripId);
}
