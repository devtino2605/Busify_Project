package com.busify.project.trip.service;

import com.busify.project.trip.dto.TripDTO;
import com.busify.project.trip.dto.TripFilterRequestDTO;

import java.util.List;

public interface TripService {
    List<TripDTO> getAllTrips();
    List<TripDTO> filterTrips(TripFilterRequestDTO filter);
}
