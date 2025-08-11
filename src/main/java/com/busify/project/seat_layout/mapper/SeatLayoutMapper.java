package com.busify.project.seat_layout.mapper;

import com.busify.project.seat_layout.dto.response.SeatLayoutFilterTripResponse;
import com.busify.project.seat_layout.entity.SeatLayout;

public class SeatLayoutMapper {

    public static SeatLayoutFilterTripResponse toDTO(SeatLayout seatLayout) {
        if (seatLayout == null) return null;

        SeatLayoutFilterTripResponse dto = new SeatLayoutFilterTripResponse();
        dto.setId(seatLayout.getId());
        dto.setName(seatLayout.getName());

        return dto;
    }
}
