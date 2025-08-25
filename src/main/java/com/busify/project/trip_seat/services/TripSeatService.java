package com.busify.project.trip_seat.services;

import lombok.RequiredArgsConstructor;

import com.busify.project.trip_seat.dto.SeatStatus;
import com.busify.project.trip_seat.entity.TripSeat;
import com.busify.project.trip_seat.enums.TripSeatStatus;
import com.busify.project.trip_seat.repository.TripSeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripSeatService {

    private final TripSeatRepository tripSeatRepository;

    public List<SeatStatus> getTripSeatsStatus(Long tripId) {
        List<TripSeat> tripSeats = tripSeatRepository.findByTripId(tripId);
        return tripSeats.stream()
                .map(tripSeat -> new SeatStatus(tripSeat.getId().getSeatNumber(), tripSeat.getStatus()))
                .collect(Collectors.toList());
    }

    public boolean changeTripSeatStatusToAvailable(Long tripId, String seatNumber) {
        TripSeat tripSeat = tripSeatRepository.findByTripIdAndSeatNumber(tripId, seatNumber);
        if (tripSeat != null) {
            tripSeat.setStatus(TripSeatStatus.available);
            tripSeatRepository.save(tripSeat);
            return true;
        }
        return false;
    }
}