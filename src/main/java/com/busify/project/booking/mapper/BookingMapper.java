package com.busify.project.booking.mapper;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.util.BookingCodeGen;
import com.busify.project.trip.entity.Trip;
import com.busify.project.user.entity.User;

public class BookingMapper {

    public static BookingHistoryResponse toDTO(Bookings bookings) {
        if (bookings == null)
            return null;

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

    public static Bookings fromRequestDTOtoEntity(BookingAddRequestDTO request, Trip trip, User customer,
            String guestFullName, String guestPhone, String guestEmail, String guestAddress) {
        if (request == null)
            return null;

        Bookings bookings = new Bookings();
        bookings.setTrip(trip);
        if (customer != null) {
            bookings.setCustomer(customer);
        } else {
            bookings.setGuestAddress(guestAddress);
            bookings.setGuestEmail(guestEmail);
            bookings.setGuestFullName(guestFullName);
            bookings.setGuestPhone(guestPhone);
        }
        bookings.setSeatNumber(request.getSeatNumber());
        bookings.setTotalAmount(request.getTotalAmount());
        bookings.setBookingCode(BookingCodeGen.generateBookingCode());
        bookings.setStatus(BookingStatus.pending);

        return bookings;
    }

    public static BookingAddResponseDTO toResponseAddDTO(Bookings bookings) {
        if (bookings == null)
            return null;

        BookingAddResponseDTO response = new BookingAddResponseDTO();
        response.setSeatNumber(bookings.getSeatNumber());
        response.setTotalAmount(bookings.getTotalAmount());
        response.setStatus(bookings.getStatus());
        return response;
    }
}
