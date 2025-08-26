package com.busify.project.ticket.service.impl;

import com.busify.project.auth.service.EmailService;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.ticket.dto.request.TicketUpdateRequestDTO;
import com.busify.project.ticket.dto.response.TicketDetailResponseDTO;
import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.dto.response.TripPassengerListResponseDTO;
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
import java.math.RoundingMode;
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

        // Debug logging
        System.out.println("DEBUG: Attempting to send email to: " + toEmail);
        System.out.println("DEBUG: Passenger name: " + passengerName);
        System.out.println("DEBUG: Number of tickets: " + savedTickets.size());

        // Gửi email vé
        if (toEmail != null && !toEmail.isEmpty()) {
            try {
                emailService.sendTicketEmail(toEmail, passengerName, savedTickets);
                System.out.println("DEBUG: Email send method called successfully");
            } catch (Exception e) {
                System.err.println("DEBUG: Email send failed: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("DEBUG: No email to send to - toEmail is null or empty");
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

    @Override
    public Optional<TicketDetailResponseDTO> getTicketById(String ticketCode) {
        Optional<Tickets> ticket = ticketRepository.findByTicketCode(ticketCode);
        if (ticket.isPresent()) {
            return Optional.of(ticketMapper.toTicketDetailResponseDTO(ticket.get()));
        }
        return Optional.empty();
    }

    @Override
    public TripPassengerListResponseDTO getPassengersByTripId(Long tripId) {
        // Lấy thông tin hành khách từ repository
        List<Object[]> passengerData = ticketRepository.findPassengersByTripId(tripId);
        
        // Map dữ liệu sang PassengerInfo
        List<TripPassengerListResponseDTO.PassengerInfo> passengers = 
            ticketMapper.mapToPassengerInfoList(passengerData);

        // Tạo response DTO
        TripPassengerListResponseDTO response = new TripPassengerListResponseDTO();
        response.setTripId(tripId);
        response.setPassengers(passengers);
        
      
        
        return response;
    }

    @Override
    public TicketResponseDTO updateTicketInTrip(Long tripId, Long ticketId, TicketUpdateRequestDTO updateRequest) {
        // Tìm vé trong chuyến đi cụ thể
        Tickets ticket = ticketRepository.findByTripIdAndTicketId(tripId, ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vé " + ticketId + " trong chuyến đi " + tripId));

        // Cập nhật thông tin hành khách
        if (updateRequest.getPassengerName() != null) {
            ticket.setPassengerName(updateRequest.getPassengerName());
        }
        if (updateRequest.getPassengerPhone() != null) {
            ticket.setPassengerPhone(updateRequest.getPassengerPhone());
        }
        if (updateRequest.getStatus() != null) {
            ticket.setStatus(updateRequest.getStatus());
        }
        
        // Cập nhật số ghế nếu có thay đổi
        if (updateRequest.getSeatNumber() != null && !updateRequest.getSeatNumber().equals(ticket.getSeatNumber())) {
            // Kiểm tra xem ghế mới có trống không
            tripSeatRepository.upsertSeat(tripId, ticket.getSeatNumber(), "available");
            ticket.setSeatNumber(updateRequest.getSeatNumber());
            tripSeatRepository.upsertSeat(tripId, updateRequest.getSeatNumber(), "booked");
        }

        // Cập nhật email trong booking nếu có
        if (updateRequest.getEmail() != null && ticket.getBooking() != null) {
            Bookings booking = ticket.getBooking();
            booking.setGuestEmail(updateRequest.getEmail());
            bookingRepository.save(booking);
        }

        // Lưu thông tin vé đã cập nhật
        Tickets updatedTicket = ticketRepository.save(ticket);
        
        return ticketMapper.toTicketResponseDTO(updatedTicket);
    }
}
