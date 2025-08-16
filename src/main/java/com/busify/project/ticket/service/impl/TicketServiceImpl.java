package com.busify.project.ticket.service.impl;

import com.busify.project.auth.service.EmailService;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.entity.Tickets;
import com.busify.project.ticket.enums.TicketStatus;
import com.busify.project.ticket.mapper.TicketMapper;
import com.busify.project.ticket.repository.TicketRepository;
import com.busify.project.ticket.service.TicketService;
import com.busify.project.trip_seat.repository.TripSeatRepository;
import com.busify.project.user.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TripSeatRepository tripSeatRepository;
    private final EmailService emailService;

    @Override
    public List<TicketResponseDTO> createTicketsFromBooking(Long bookingId) {
        Bookings booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        String[] seatNumbers = booking.getSeatNumber().split(",");

        BigDecimal pricePerSeat = booking.getTrip().getPricePerSeat();

        String passengerName;
        String passengerPhone;

        if (booking.getCustomer() instanceof Profile profile) {
            passengerName = profile.getFullName();
            passengerPhone = profile.getPhoneNumber();
        } else {
            passengerName = booking.getGuestFullName();
            passengerPhone = booking.getGuestPhone();
        }

        List<Tickets> tickets = new ArrayList<>();
        for (String seat : seatNumbers) {
            Tickets ticket = new Tickets();
            ticket.setBooking(booking);
            ticket.setPrice(pricePerSeat);
            ticket.setPassengerName(passengerName);
            ticket.setPassengerPhone(passengerPhone);
            ticket.setSeatNumber(seat.trim());
            ticket.setStatus(TicketStatus.valid);
            ticket.setTicketCode(generateTicketCode());
            tickets.add(ticket);
        }

        booking.setStatus(BookingStatus.confirmed);
        bookingRepository.save(booking);

        List<Tickets> savedTickets = ticketRepository.saveAll(tickets);

        Long tripId = booking.getTrip().getId();
        for (String seat : seatNumbers) {
            tripSeatRepository.upsertSeat(tripId, seat.trim(), "booked");
        }

        // Lấy email
        String toEmail = booking.getCustomer() instanceof Profile profile
                ? profile.getEmail()
                : booking.getGuestEmail();

        // Gửi email vé
        if (toEmail != null && !toEmail.isEmpty()) {
            emailService.sendTicketEmail(toEmail, passengerName, savedTickets);
        }

        return savedTickets.stream()
                .map(ticketMapper::toTicketResponseDTO)
                .collect(Collectors.toList());
    }

    private String generateTicketCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        List<Tickets> tickets = ticketRepository.findAll();
        return tickets.stream()
                .map(ticketMapper::toTicketResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TicketResponseDTO> searchTicketsByTicketCode(String ticketCode) {
        Optional<Tickets> ticket = ticketRepository.findByTicketCode(ticketCode);
        if (ticket.isPresent()) {
            return Optional.of(ticketMapper.toTicketResponseDTO(ticket.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<TicketResponseDTO> searchTicketsByName(String name) {
        List<Tickets> tickets = ticketRepository.findByPassengerName(name);
        return tickets.stream()
                .map(ticketMapper::toTicketResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponseDTO> searchTicketsByPhone(String phone) {
        List<Tickets> tickets = ticketRepository.findByPassengerPhone(phone);
        return tickets.stream()
                .map(ticketMapper::toTicketResponseDTO)
                .collect(Collectors.toList());
    }
}
