package com.busify.project.ticket.service.impl;

import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.auth.service.EmailService;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.common.utils.JwtUtils;
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
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final AuditLogService auditLogService;
    private final JwtUtils jwtUtil;
    private final UserRepository userRepository;

    @Override
    public List<TicketResponseDTO> createTicketsFromBooking(Long bookingId) {
        Bookings booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        String[] seatNumbers = booking.getSeatNumber().split(",");

        // Sử dụng giá từ booking (đã tính toán) thay vì giá gốc từ trip
        BigDecimal totalAmount = booking.getTotalAmount();
        BigDecimal pricePerSeat;

        if (seatNumbers.length > 0) {
            pricePerSeat = totalAmount.divide(BigDecimal.valueOf(seatNumbers.length), 2, RoundingMode.HALF_UP);
        } else {
            pricePerSeat = booking.getTrip().getPricePerSeat(); // fallback
        }

        System.out.println("DEBUG: Total amount: " + totalAmount + ", Seats: " + seatNumbers.length
                + ", Price per seat: " + pricePerSeat);

        String passengerName;
        String passengerPhone;

        // Ưu tiên thông tin guest nếu có, nếu không mới lấy từ customer
        if (booking.getGuestFullName() != null && !booking.getGuestFullName().trim().isEmpty()) {
            passengerName = booking.getGuestFullName();
            passengerPhone = booking.getGuestPhone();
            System.out.println("DEBUG: Using guest info - Name: " + passengerName + ", Phone: " + passengerPhone);
        } else if (booking.getCustomer() instanceof Profile profile) {
            passengerName = profile.getFullName();
            passengerPhone = profile.getPhoneNumber();
            System.out.println("DEBUG: Using customer info - Name: " + passengerName + ", Phone: " + passengerPhone);
        } else {
            passengerName = "Unknown Passenger";
            passengerPhone = "Unknown Phone";
            System.out.println("DEBUG: Using default info - Name: " + passengerName + ", Phone: " + passengerPhone);
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
        List<TripPassengerListResponseDTO.PassengerInfo> passengers = ticketMapper
                .mapToPassengerInfoList(passengerData);

        // Tạo response DTO
        TripPassengerListResponseDTO response = new TripPassengerListResponseDTO();
        response.setTripId(tripId);
        response.setPassengers(passengers);

        // Có thể thêm thông tin trip nếu cần
        // (operator name, route name, departure time)
        // Bạn có thể thêm query để lấy thông tin này

        return response;
    }

    @Override
    public TicketResponseDTO updateTicketInTrip(Long tripId, Long ticketId, TicketUpdateRequestDTO updateRequest) {
        // Tìm vé trong chuyến đi cụ thể
        Tickets ticket = ticketRepository.findByTripIdAndTicketId(tripId, ticketId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy vé " + ticketId + " trong chuyến đi " + tripId));

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

    @Override
    public void deleteTicketByCode(String ticketCode) {
        try {
            // 1. Lấy email user hiện tại từ JWT context
            String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";

            // 2. Lấy user từ DB dựa trên email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // find ticket by ticket code
            Tickets ticket = ticketRepository.findByTicketCode(ticketCode)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy vé với mã: " + ticketCode));

            // Kiểm tra
            String roleName = user.getRole().getName();
            if (roleName.equals("ADMIN") || roleName.equals("OPERATOR") || roleName.equals("CUSTOMER_SERVICE")) {
                // Nếu là admin, operator, hoặc customer_service thì cho phép xóa mà không cần kiểm tra chủ vé
            } else {
                // Nếu không phải các quyền trên, kiểm tra xem có phải là chủ vé không
                if (!ticket.getBooking().getCustomer().getEmail().equals(email)) {
                    throw new SecurityException("Bạn không có quyền xóa vé này");
                }
            }

            // change ticket status to cancelled
            ticket.setStatus(TicketStatus.cancelled);
            ticketRepository.save(ticket);

            // update audit log
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("DELETE");
            auditLog.setTargetEntity("TICKET");
            auditLog.setTargetId(ticket.getTicketId());
            auditLog.setDetails("Ticket deleted: " + ticket.getTicketCode());
            auditLog.setUser(user);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            // Log exception or handle as needed
            throw new RuntimeException("Lỗi khi xóa vé: " + e.getMessage(), e);
        }
    }
}
