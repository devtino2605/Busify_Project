package com.busify.project.booking.mapper;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.dto.response.BookingUpdateResponseDTO;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.util.BookingCodeGen;
import com.busify.project.trip.entity.Trip;
import com.busify.project.user.entity.User;
import com.busify.project.user.entity.Profile;
import com.busify.project.booking.dto.response.BookingDetailResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
            if (bookings.getPayment() != null) {
                dto.setPayment_method(bookings.getPayment().getPaymentMethod().getMethod());
            } else {
                dto.setPayment_method(null);
            }
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
        response.setBookingCode(bookings.getBookingCode());
        response.setBookingId(bookings.getId());
        response.setSeatNumber(bookings.getSeatNumber());
        response.setTotalAmount(bookings.getTotalAmount());
        response.setStatus(bookings.getStatus());
        return response;
    }

    public static BookingDetailResponse toDetailDTO(Bookings booking) {
        if (booking == null)
            return null;

        BookingDetailResponse dto = new BookingDetailResponse();
        dto.setBooking_id(booking.getId());

        if (booking.getCustomer() != null && booking.getCustomer().getEmail() != null) {
            dto.setEmail(booking.getCustomer().getEmail());
        } else {
            dto.setEmail(booking.getGuestEmail());
        }

        if (booking.getCustomer() instanceof Profile profile) {
            dto.setPassenger_name(profile.getFullName());
            dto.setPhone(profile.getPhoneNumber());
            dto.setAddress(profile.getAddress());
        } else {
            dto.setPassenger_name(booking.getGuestFullName());
            dto.setPhone(booking.getGuestPhone());
            dto.setAddress(booking.getGuestAddress());
        }

        // Add guest fields to response
        dto.setGuestFullName(booking.getGuestFullName());
        dto.setGuestEmail(booking.getGuestEmail());
        dto.setGuestPhone(booking.getGuestPhone());
        dto.setGuestAddress(booking.getGuestAddress());

        var trip = booking.getTrip();

        // Route start
        BookingDetailResponse.LocationInfo start = new BookingDetailResponse.LocationInfo();
        start.setName(trip.getRoute().getName());
        start.setAddress(trip.getRoute().getStartLocation().getAddress());
        start.setCity(trip.getRoute().getStartLocation().getCity());

        // Route end
        BookingDetailResponse.LocationInfo end = new BookingDetailResponse.LocationInfo();
        end.setName(trip.getRoute().getName());
        end.setAddress(trip.getRoute().getEndLocation().getAddress());
        end.setCity(trip.getRoute().getEndLocation().getCity());

        dto.setRoute_start(start);
        dto.setRoute_end(end);

        dto.setOperator_name(trip.getBus().getOperator().getName());
        dto.setDeparture_time(trip.getDepartureTime());
        dto.setArrival_estimate_time(trip.getEstimatedArrivalTime());

        // Bus info
        BookingDetailResponse.BusInfo bus = new BookingDetailResponse.BusInfo();
        bus.setModel(trip.getBus().getModel());
        bus.setLicense_plate(trip.getBus().getLicensePlate());
        dto.setBus(bus);

        // Tickets
        List<BookingDetailResponse.TicketInfo> ticketInfos = booking.getTickets().stream().map(ticket -> {
            BookingDetailResponse.TicketInfo t = new BookingDetailResponse.TicketInfo();
            t.setSeat_number(ticket.getSeatNumber());
            t.setTicket_code(ticket.getTicketCode());
            return t;
        }).collect(Collectors.toList());
        dto.setTickets(ticketInfos);

        dto.setStatus(booking.getStatus().name().toLowerCase());

        // Payment info
        BookingDetailResponse.PaymentInfo paymentInfo = new BookingDetailResponse.PaymentInfo();

        // Giả sử lấy payment đầu tiên nếu có
        if (booking.getPayment() != null) {
            var payment = booking.getPayment();
            paymentInfo.setAmount(payment.getAmount());
            paymentInfo.setMethod(payment.getPaymentMethod());
            paymentInfo.setTimestamp(payment.getPaidAt());
        } else {
            // Nếu không có payment nào
            paymentInfo.setAmount(BigDecimal.ZERO);
            paymentInfo.setMethod(null);
            paymentInfo.setTimestamp(null);
        }

        dto.setPayment_info(paymentInfo);

        return dto;
    }

    public static BookingUpdateResponseDTO toUpdateResponseDTO(Bookings booking) {
        if (booking == null)
            return null;

        BookingUpdateResponseDTO dto = new BookingUpdateResponseDTO();
        dto.setId(booking.getId());
        dto.setBookingCode(booking.getBookingCode());
        dto.setGuestFullName(booking.getGuestFullName());
        dto.setGuestEmail(booking.getGuestEmail());
        dto.setGuestPhone(booking.getGuestPhone());
        dto.setSeatNumber(booking.getSeatNumber());
        dto.setStatus(booking.getStatus());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());
        dto.setTripId(booking.getTrip().getId());

        return dto;
    }
}
