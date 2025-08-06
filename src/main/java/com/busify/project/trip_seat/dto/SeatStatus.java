package com.busify.project.trip_seat.dto;

import com.busify.project.trip_seat.enums.TripSeatStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatStatus {
    private String seatNumber;
    private TripSeatStatus status;
}