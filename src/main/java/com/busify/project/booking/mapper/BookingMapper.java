package com.busify.project.booking.mapper;

import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.entity.Bookings;

public class BookingMapper {

    public static BookingHistoryResponse toDTO(Bookings bookings) {
        if (bookings == null) return null;

        BookingHistoryResponse dto = new BookingHistoryResponse();
        if (bookings.getTrip() != null) {
            dto.setBooking_id(bookings.getTrip().getId());
            dto.setRoute_name(bookings.getTrip().getRoute().getName());
            dto.setDeparture_time(bookings.getTrip().getDepartureTime());
            dto.setArrival_time(bookings.getTrip().getEstimatedArrivalTime());
            dto.setDeparture_name(bookings.getTrip().getRoute().getStartLocation().getName());
            dto.setArrival_name(bookings.getTrip().getRoute().getEndLocation().getName());
            dto.setBooking_code(bookings.getBookingCode());
            dto.setStatus(bookings.getStatus());
            dto.setTotal_amount(bookings.getTotalAmount());
            dto.setBooking_date(bookings.getCreatedAt());
            dto.setTicket_count(bookings.getTickets().size());
            dto.setPayment_method(bookings.getPayment().getPaymentMethod().getMethod());
        }

        return dto;
    }
}
