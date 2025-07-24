package com.busify.project.trip.service;

import com.busify.project.trip.dto.response.TripListResponse;
import com.busify.project.trip.dto.response.TripResponse;

import java.util.List;

public interface TripService {
    public List<TripResponse> getFeaturedTrips();

    public TripListResponse getTrips();

    public List<TripResponse> getHighRatedTripsForCurrentWeek();
}
