package com.busify.project.booking.mapper;

import com.busify.project.booking.dto.response.BookingDetailResponse;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.entity.Bookings;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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


    public static BookingDetailResponse toDetailDTO(Bookings booking) {
        if (booking == null) return null;

        BookingDetailResponse dto = new BookingDetailResponse();
        dto.setBookingId(booking.getId());

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

        dto.setRouteStart(start);
        dto.setRouteEnd(end);

        dto.setOperatorName(trip.getBus().getOperator().getName());
        dto.setDepartureTime(trip.getDepartureTime());
        dto.setArrivalEstimateTime(trip.getEstimatedArrivalTime());

        // Bus info
        BookingDetailResponse.BusInfo bus = new BookingDetailResponse.BusInfo();
        bus.setModel(trip.getBus().getModel());
        bus.setLicensePlate(trip.getBus().getLicensePlate());
        dto.setBus(bus);

        // Tickets
        List<BookingDetailResponse.TicketInfo> ticketInfos = booking.getTickets().stream().map(ticket -> {
            BookingDetailResponse.TicketInfo t = new BookingDetailResponse.TicketInfo();
            t.setPassengerName(ticket.getPassengerName());
            t.setPhone(ticket.getPassengerPhone());
            if (ticket.getBooking().getCustomer().getEmail() != null) {
                t.setEmail(ticket.getBooking().getCustomer().getEmail());
            } else {
                t.setEmail(ticket.getBooking().getGuestEmail());
            }
            t.setSeatNumber(ticket.getBooking().getSeatNumber());
            t.setTicketCode(ticket.getTicketCode());
            return t;
        }).collect(Collectors.toList());
        dto.setTickets(ticketInfos);

        dto.setStatus(booking.getStatus().name().toLowerCase());

        // Payment info
        BookingDetailResponse.PaymentInfo paymentInfo = new BookingDetailResponse.PaymentInfo();
        paymentInfo.setTotalAmount(booking.getTotalAmount());

        List<BookingDetailResponse.PaymentInfo.PaymentDetail> payments = booking.getPayments().stream().map(p -> {
            BookingDetailResponse.PaymentInfo.PaymentDetail pd = new BookingDetailResponse.PaymentInfo.PaymentDetail();
            pd.setAmount(p.getAmount());
            pd.setMethod(p.getPaymentMethod());
            pd.setTimestamp(p.getPaidAt());
            return pd;
        }).collect(Collectors.toList());

        // Tính remainingAmount = totalAmount - tổng amount của các payment
        BigDecimal totalPaid = payments.stream()
                .map(BookingDetailResponse.PaymentInfo.PaymentDetail::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingAmount = booking.getTotalAmount().subtract(totalPaid);
        paymentInfo.setRemainingAmount(remainingAmount);

        paymentInfo.setPayments(payments);
        dto.setPaymentInfo(paymentInfo);


        return dto;
    }
}
