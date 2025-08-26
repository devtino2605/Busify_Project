package com.busify.project.trip.service;

import com.busify.project.trip.dto.response.TopTripRevenueDTO;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.dto.request.TripUpdateStatusRequest;
import com.busify.project.trip.dto.response.TripByDriverResponseDTO;
import com.busify.project.trip.dto.response.TripResponse;
import com.busify.project.trip.dto.response.TripRouteResponse;
import com.busify.project.trip.dto.response.TripStopResponse;

import java.util.List;
import java.util.Map;

public interface TripService {
    List<TripFilterResponseDTO> getAllTrips();

    List<TripFilterResponseDTO> filterTrips(TripFilterRequestDTO filter);

    List<TripResponse> findTopUpcomingTripByOperator();

    Map<String, Object> getTripDetailById(Long tripId);

    List<TripRouteResponse> getTripRouteById(Long tripId);

    List<TripStopResponse> getTripStopsById(Long tripId);

    Map<String, Object> updateTripStatus(Long tripId, TripUpdateStatusRequest request);
    
    List<TripByDriverResponseDTO> getTripsByDriverId(Long driverId);
    
    List<Map<String, Object>> getNextTripsOfOperator(Long operatorId);
    // Lấy top 10 trips có doanh thu cao nhất theo năm
    public List<TopTripRevenueDTO> getTop10TripsByRevenueAndYear(Integer year);
}
