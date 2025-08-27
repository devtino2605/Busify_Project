package com.busify.project.seat_layout.service;

import com.busify.project.seat_layout.dto.response.SeatLayoutFilterTripResponse;
import com.busify.project.seat_layout.entity.SeatLayout;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SeatLayoutService {
    List<SeatLayoutFilterTripResponse> getAllSeatLayouts();
    Optional<SeatLayout> getSeatLayoutByTripId(Long tripId);
}
