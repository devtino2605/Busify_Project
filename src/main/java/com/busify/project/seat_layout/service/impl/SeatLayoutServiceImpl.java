package com.busify.project.seat_layout.service.impl;

import com.busify.project.seat_layout.dto.response.SeatLayoutFilterTripResponse;
import com.busify.project.seat_layout.entity.SeatLayout;
import com.busify.project.seat_layout.mapper.SeatLayoutMapper;
import com.busify.project.seat_layout.repository.SeatLayoutRepository;
import com.busify.project.seat_layout.service.SeatLayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatLayoutServiceImpl implements SeatLayoutService {
    @Autowired
    private SeatLayoutRepository seatLayoutRepository;

    @Override
    public List<SeatLayoutFilterTripResponse> getAllSeatLayouts() {
        return seatLayoutRepository.findAll()
                .stream()
                .map(SeatLayoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SeatLayout> getSeatLayoutByTripId(Long tripId) {
        return seatLayoutRepository.findSeatLayoutByTripId(tripId);
    }
}
