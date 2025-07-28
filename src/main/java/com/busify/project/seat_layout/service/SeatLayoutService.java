package com.busify.project.seat_layout.service;

import com.busify.project.seat_layout.dto.response.SeatLayoutFilterTripResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeatLayoutService {
    List<SeatLayoutFilterTripResponse> getAllSeatLayouts();
}
