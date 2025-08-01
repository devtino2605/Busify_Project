package com.busify.project.booking.mapper;

import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.entity.Bookings;

public class BookingMapper {

    public static BookingHistoryResponse toDTO(Bookings bookings) {
        if (bookings == null) return null;

        BookingHistoryResponse dto = new BookingHistoryResponse();
        if (bookings.getTrip() != null) {
            dto.setRoute_name(bookings.getTrip().getRoute().getName());
            dto.setDepartureTime(bookings.getTrip().getDepartureTime());
            dto.setBooking_code(bookings.getBookingCode());
            dto.setStatus(bookings.getStatus());
        }

        return dto;
    }
}
