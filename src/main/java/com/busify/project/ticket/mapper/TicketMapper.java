package com.busify.project.ticket.mapper;

import com.busify.project.ticket.dto.response.TicketDetailResponseDTO;
import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.dto.response.TripPassengerListResponseDTO;
import com.busify.project.ticket.entity.Tickets;
import com.busify.project.ticket.enums.TicketStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketMapper {

    public TicketResponseDTO toTicketResponseDTO(Tickets ticket) {
        if (ticket == null)
            return null;

        TicketResponseDTO dto = new TicketResponseDTO();
        TicketResponseDTO.TicketInfo info = new TicketResponseDTO.TicketInfo();

        info.setTicketId(ticket.getTicketId());
        info.setPassengerName(ticket.getPassengerName());

        info.setPassengerPhone(ticket.getPassengerPhone());

        info.setPrice(ticket.getPrice());
        info.setSeatNumber(ticket.getSeatNumber());
        info.setStatus(ticket.getStatus());
        info.setTicketCode(ticket.getTicketCode());

        if (ticket.getBooking() != null) {
            info.setBookingId(ticket.getBooking().getId());
        }

        dto.setTickets(info);
        return dto;
    }

    public TicketDetailResponseDTO toTicketDetailResponseDTO(Tickets ticket) {
        if (ticket == null)
            return null;

        var booking = ticket.getBooking();
        var trip = booking.getTrip();
        var route = trip.getRoute();
        var bus = trip.getBus();
        var operator = bus.getOperator();

        // Build booking info
        var bookingInfo = TicketDetailResponseDTO.BookingInfo.builder()
                .bookingId(booking.getId())
                .bookingCode(booking.getBookingCode())
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .bookingDate(booking.getCreatedAt())
                .customerEmail(
                        booking.getCustomer() != null ? booking.getCustomer().getEmail() : booking.getGuestEmail())
                .customerPhone(booking.getCustomer() instanceof com.busify.project.user.entity.Profile profile
                        ? profile.getPhoneNumber()
                        : booking.getGuestPhone())
                .customerAddress(booking.getCustomer() instanceof com.busify.project.user.entity.Profile profile
                        ? profile.getAddress()
                        : booking.getGuestAddress())
                .paymentMethod(booking.getPayment() != null ? booking.getPayment().getPaymentMethod() : null)
                .paidAt(booking.getPayment() != null ? booking.getPayment().getPaidAt() : null)
                .build();

        // Build location info
        var startLocation = TicketDetailResponseDTO.LocationInfo.builder()
                .name(route.getStartLocation().getName())
                .address(route.getStartLocation().getAddress())
                .city(route.getStartLocation().getCity())
                .latitude(route.getStartLocation().getLatitude())
                .longitude(route.getStartLocation().getLongitude())
                .build();

        var endLocation = TicketDetailResponseDTO.LocationInfo.builder()
                .name(route.getEndLocation().getName())
                .address(route.getEndLocation().getAddress())
                .city(route.getEndLocation().getCity())
                .latitude(route.getEndLocation().getLatitude())
                .longitude(route.getEndLocation().getLongitude())
                .build();

        // Build route info
        var routeInfo = TicketDetailResponseDTO.RouteInfo.builder()
                .routeId(route.getId())
                .routeName(route.getName())
                .startLocation(startLocation)
                .endLocation(endLocation)
                .durationMinutes(route.getDefaultDurationMinutes())
                .build();

        // Build bus info
        var busInfo = TicketDetailResponseDTO.BusInfo.builder()
                .busId(bus.getId())
                .modelName(bus.getModel().getName())
                .licensePlate(bus.getLicensePlate())
                .totalSeats(bus.getTotalSeats())
                .amenities(bus.getAmenities())
                .build();

        // Build operator info
        var operatorInfo = TicketDetailResponseDTO.OperatorInfo.builder()
                .operatorId(operator.getId())
                .operatorName(operator.getName())
                .hotline(operator.getHotline())
                .build();

        // Build trip info
        var tripInfo = TicketDetailResponseDTO.TripInfo.builder()
                .tripId(trip.getId())
                .departureTime(trip.getDepartureTime())
                .arrivalTime(trip.getEstimatedArrivalTime())
                .pricePerSeat(trip.getPricePerSeat())
                .route(routeInfo)
                .bus(busInfo)
                .operator(operatorInfo)
                .build();

        // Build main response
        return TicketDetailResponseDTO.builder()
                .ticketCode(ticket.getTicketCode())
                .passengerName(ticket.getPassengerName())
                .passengerPhone(ticket.getPassengerPhone())
                .seatNumber(ticket.getSeatNumber())
                .price(ticket.getPrice())
                .status(ticket.getStatus())
                .booking(bookingInfo)
                .trip(tripInfo)
                .build();
    }

    public TripPassengerListResponseDTO.PassengerInfo mapToPassengerInfo(Object[] row) {
        TripPassengerListResponseDTO.PassengerInfo passenger = new TripPassengerListResponseDTO.PassengerInfo();
        
        passenger.setTicketId(row[0] != null ? ((Number) row[0]).longValue() : null);
        passenger.setPassengerName((String) row[1]);
        passenger.setPassengerPhone((String) row[2]);
        passenger.setEmail((String) row[3]);
        passenger.setSeatNumber((String) row[4]);
        passenger.setStatus(row[5] != null ? TicketStatus.valueOf(row[5].toString()) : null);
        passenger.setTicketCode((String) row[6]);
        
        return passenger;
    }

    public List<TripPassengerListResponseDTO.PassengerInfo> mapToPassengerInfoList(List<Object[]> rows) {
        return rows.stream()
                .map(this::mapToPassengerInfo)
                .collect(Collectors.toList());
    }
}
