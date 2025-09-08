package com.busify.project.ticket.service.impl;

import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.auth.service.EmailService;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.ticket.dto.request.TicketUpdateRequestDTO;
import com.busify.project.ticket.dto.request.UpdateTicketStatusRequestDTO;
import com.busify.project.ticket.dto.response.TicketDetailResponseDTO;
import com.busify.project.ticket.dto.response.TicketResponseDTO;
import com.busify.project.ticket.dto.response.TripPassengerListResponseDTO;
import com.busify.project.ticket.dto.response.BookingTicketsValidationResponseDTO;
import com.busify.project.ticket.dto.response.UpdateTicketStatusResponseDTO;
import com.busify.project.ticket.entity.Tickets;
import com.busify.project.ticket.enums.SellMethod;
import com.busify.project.ticket.enums.TicketStatus;
import com.busify.project.ticket.exception.TicketProcessingException;
import com.busify.project.ticket.mapper.TicketMapper;
import com.busify.project.ticket.repository.TicketRepository;
import com.busify.project.ticket.service.TicketService;
import com.busify.project.trip_seat.repository.TripSeatRepository;
import com.busify.project.trip_seat.services.TripSeatService;
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
    private final TripSeatService tripSeatService;

    @Override
    public List<TicketResponseDTO> createTicketsFromBooking(Long bookingId, SellMethod sellMethod) {
        Bookings booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

        String[] seatNumbers = booking.getSeatNumber().split(",");

        BigDecimal pricePerSeat = booking.getTrip().getPricePerSeat();
        // Sử dụng giá từ booking (đã tính toán) thay vì giá gốc từ trip
        BigDecimal totalAmount = booking.getTotalAmount();

        if (seatNumbers.length > 0) {
            pricePerSeat = totalAmount.divide(BigDecimal.valueOf(seatNumbers.length), 2, RoundingMode.HALF_UP);
        } else {
            pricePerSeat = booking.getTrip().getPricePerSeat(); // fallback
        }

        System.out.println("DEBUG: Total amount: " + totalAmount + ", Seats: " + seatNumbers.length
                + ", Price per seat: " + pricePerSeat);

        String passengerName;
        String passengerPhone;

        if (booking.getCustomer() instanceof Profile profile) {
            passengerName = profile.getFullName();
            passengerPhone = profile.getPhoneNumber();
        } else {
            passengerName = booking.getGuestFullName();
            passengerPhone = booking.getGuestPhone();
        }

        Optional<User> seller = Optional.empty();

        System.out.println("DEBUG: Sell method: " + (sellMethod == SellMethod.MANUAL));
        System.out.println("Sell method value: " + sellMethod);

        if (sellMethod == SellMethod.MANUAL) {
            // Lấy user hiện tại từ JWT context
            String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";
            seller = userRepository.findByEmail(email);
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
            ticket.setSellMethod(sellMethod != null ? sellMethod : SellMethod.AUTO);
            ticket.setSeller(seller.orElse(null));
            tickets.add(ticket);
        }

        booking.setStatus(BookingStatus.confirmed);
        bookingRepository.save(booking);

        System.out.println("DEBUG: Saving " + tickets.size() + " tickets to the database");

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

        // Gửi email vé với PDF attachment
        if (toEmail != null && !toEmail.isEmpty()) {
            try {
                emailService.sendTicketEmail(toEmail, passengerName, savedTickets);
                System.out.println("DEBUG: Email with PDF sent successfully");
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
                // Nếu là admin, operator, hoặc customer_service thì cho phép xóa mà không cần
                // kiểm tra chủ vé
            } else {
                // Nếu không phải các quyền trên, kiểm tra xem có phải là chủ vé không
                if (!ticket.getBooking().getCustomer().getEmail().equals(email)) {
                    throw new SecurityException("Bạn không có quyền xóa vé này");
                }
            }

            // Before changing ticket status to cancelled
            String fullName = ticket.getPassengerName();
            String toEmail = ticket.getBooking().getGuestEmail() != null ? ticket.getBooking().getGuestEmail()
                    : ticket.getBooking().getCustomer().getEmail();

            emailService.sendTicketCancelledEmail(toEmail, fullName, ticket);

            // change ticket status to cancelled
            ticket.setStatus(TicketStatus.cancelled);
            ticketRepository.save(ticket);

            // change trip seat status to available
            tripSeatService.changeTripSeatStatusToAvailable(ticket.getBooking().getTrip().getId(),
                    ticket.getSeatNumber());

            // update audit log
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("DELETE");
            auditLog.setTargetEntity("TICKET");
            auditLog.setTargetId(ticket.getTicketId());
            auditLog.setDetails(String.format("{\"ticket_code\":\"%s\"}", ticket.getTicketCode()));
            auditLog.setUser(user);
            auditLogService.save(auditLog);
        } catch (Exception e) {
            // Log exception or handle as needed
            throw TicketProcessingException.deletionFailed(e);
        }
    }

    @Override
    public BookingTicketsValidationResponseDTO validateBookingTrip(Long tripId, String bookingCode) {
        // 1. Tìm booking theo booking code
        Optional<Bookings> bookingOpt = bookingRepository.findByBookingCode(bookingCode);

        if (bookingOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy booking với mã: " + bookingCode);
        }

        Bookings booking = bookingOpt.get();

        // 2. Kiểm tra booking có thuộc về trip này không
        if (!booking.getTrip().getId().equals(tripId)) {
            throw new IllegalArgumentException("Vé này không thuộc về chuyến đi hiện tại. " +
                    "Booking thuộc trip ID: " + booking.getTrip().getId() +
                    ", nhưng đang kiểm tra trip ID: " + tripId);
        }

        // 3. Lấy danh sách tickets của booking này
        List<Tickets> tickets = ticketRepository.findByBookingCode(bookingCode);

        if (tickets.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy vé nào cho booking code: " + bookingCode);
        }

        // 4. Tạo response DTO
        BookingTicketsValidationResponseDTO response = new BookingTicketsValidationResponseDTO();
        response.setTripId(tripId);
        response.setBookingCode(bookingCode);
        response.setBookingId(booking.getId());

        // Lấy thông tin hành khách chính (có thể từ customer hoặc guest)
        String passengerInfo;
        if (booking.getCustomer() != null) {
            // Nếu có customer thì lấy từ customer
            User customer = booking.getCustomer();
            if (customer instanceof Profile) {
                Profile profile = (Profile) customer;
                passengerInfo = profile.getFullName() + " (" + customer.getEmail() + ")";
            } else {
                passengerInfo = "Customer ID: " + customer.getId() + " (" + customer.getEmail() + ")";
            }
        } else {
            // Nếu không có customer thì lấy từ guest info
            passengerInfo = booking.getGuestFullName() + " (" + booking.getGuestEmail() + ")";
        }
        response.setPassengerInfo(passengerInfo);

        // 5. Convert tickets to DTO
        List<BookingTicketsValidationResponseDTO.TicketValidationDTO> ticketDTOs = tickets.stream()
                .map(ticket -> {
                    BookingTicketsValidationResponseDTO.TicketValidationDTO ticketDTO = new BookingTicketsValidationResponseDTO.TicketValidationDTO();
                    ticketDTO.setTicketId(ticket.getTicketId());
                    ticketDTO.setTicketCode(ticket.getTicketCode());
                    ticketDTO.setSeatNumber(ticket.getSeatNumber());
                    ticketDTO.setPassengerName(ticket.getPassengerName());
                    ticketDTO.setPassengerPhone(ticket.getPassengerPhone());
                    ticketDTO.setStatus(ticket.getStatus().toString());
                    // Có thể thêm field isUsed nếu có trong entity
                    ticketDTO.setIsUsed(ticket.getStatus() == TicketStatus.used);
                    return ticketDTO;
                })
                .collect(Collectors.toList());

        response.setTickets(ticketDTOs);

        return response;
    }

    @Override
    public UpdateTicketStatusResponseDTO updateTicketStatus(UpdateTicketStatusRequestDTO request) {
        List<String> ticketCodes = request.getTicketCodes();
        String statusStr = request.getStatus();

        // Convert string to enum
        TicketStatus targetStatus;
        try {
            targetStatus = TicketStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status không hợp lệ: " + statusStr);
        }

        // Validate status - chỉ cho phép used và cancelled
        if (targetStatus != TicketStatus.used && targetStatus != TicketStatus.cancelled) {
            throw new IllegalArgumentException("Chỉ được phép cập nhật status thành 'used' hoặc 'cancelled'");
        }

        UpdateTicketStatusResponseDTO response = new UpdateTicketStatusResponseDTO();
        response.setTotalTickets(ticketCodes.size());
        response.setStatus(statusStr);

        List<UpdateTicketStatusResponseDTO.TicketUpdateResult> results = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        for (String ticketCode : ticketCodes) {
            UpdateTicketStatusResponseDTO.TicketUpdateResult result = new UpdateTicketStatusResponseDTO.TicketUpdateResult();
            result.setTicketCode(ticketCode);

            try {
                // Tìm ticket theo code
                Optional<Tickets> ticketOpt = ticketRepository.findByTicketCode(ticketCode);

                if (ticketOpt.isEmpty()) {
                    result.setSuccess(false);
                    result.setMessage("Không tìm thấy vé với mã: " + ticketCode);
                    failCount++;
                } else {
                    Tickets ticket = ticketOpt.get();
                    String previousStatus = ticket.getStatus().toString();
                    result.setPreviousStatus(previousStatus);

                    // Kiểm tra logic nghiệp vụ
                    if (targetStatus == TicketStatus.used && ticket.getStatus() == TicketStatus.used) {
                        result.setSuccess(false);
                        result.setMessage("Vé đã được sử dụng trước đó");
                        failCount++;
                    } else if (targetStatus == TicketStatus.cancelled && ticket.getStatus() == TicketStatus.cancelled) {
                        result.setSuccess(false);
                        result.setMessage("Vé đã được hủy trước đó");
                        failCount++;
                    } else if (ticket.getStatus() == TicketStatus.cancelled && targetStatus == TicketStatus.used) {
                        result.setSuccess(false);
                        result.setMessage("Không thể sử dụng vé đã bị hủy");
                        failCount++;
                    } else {
                        // Cập nhật status
                        ticket.setStatus(targetStatus);
                        ticketRepository.save(ticket);

                        result.setSuccess(true);
                        result.setNewStatus(targetStatus.toString());
                        result.setMessage("Cập nhật thành công");
                        successCount++;

                        // Log audit nếu cần
                        try {
                            String currentUserEmail = jwtUtil.getCurrentUserLogin().orElse("system");
                            User user = userRepository.findByEmailIgnoreCase(currentUserEmail).orElse(null);

                            if (user != null) {
                                AuditLog auditLog = new AuditLog();
                                auditLog.setAction("UPDATE_STATUS");
                                auditLog.setTargetEntity("TICKET");
                                auditLog.setTargetId(ticket.getTicketId());
                                auditLog.setDetails(String.format(
                                        "{\"ticket_code\":\"%s\",\"previous_status\":\"%s\",\"new_status\":\"%s\",\"reason\":\"%s\"}",
                                        ticketCode, previousStatus, targetStatus,
                                        request.getReason() != null ? request.getReason() : ""));
                                auditLog.setUser(user);
                                auditLogService.save(auditLog);
                            }
                        } catch (Exception auditException) {
                            // Log audit error but don't fail the main operation
                            System.err.println("Failed to create audit log: " + auditException.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                result.setSuccess(false);
                result.setMessage("Lỗi khi cập nhật: " + e.getMessage());
                failCount++;
            }

            results.add(result);
        }

        response.setSuccessfulUpdates(successCount);
        response.setFailedUpdates(failCount);
        response.setResults(results);

        return response;
    }

    @Override
    public int autoCancelValidTicketsWhenTripDeparted(Long tripId) {
        try {
            System.out
                    .println("=== DEBUG: autoCancelValidTicketsWhenTripDeparted called for tripId: " + tripId + " ===");

            // Đầu tiên, kiểm tra có vé nào của trip này không
            List<Tickets> allTicketsForTrip = ticketRepository.findByTripId(tripId);
            System.out.println("Total tickets found for trip " + tripId + ": " + allTicketsForTrip.size());

            // Log trạng thái các vé trước khi cập nhật
            for (Tickets ticket : allTicketsForTrip) {
                System.out.println("Ticket ID: " + ticket.getTicketId() +
                        ", Code: " + ticket.getTicketCode() +
                        ", Status: " + ticket.getStatus());
            }

            // Cập nhật tất cả vé có status = valid thành cancelled cho trip này
            int cancelledCount = ticketRepository.cancelValidTicketsByTripId(tripId);
            System.out.println("Number of tickets cancelled: " + cancelledCount);

            // Kiểm tra lại sau khi cập nhật
            List<Tickets> ticketsAfterUpdate = ticketRepository.findByTripId(tripId);
            System.out.println("=== After update ===");
            for (Tickets ticket : ticketsAfterUpdate) {
                System.out.println("Ticket ID: " + ticket.getTicketId() +
                        ", Code: " + ticket.getTicketCode() +
                        ", Status: " + ticket.getStatus());
            }

            // Log audit cho hành động tự động hủy vé
            if (cancelledCount > 0) {
                try {
                    String currentUserEmail = jwtUtil.getCurrentUserLogin().orElse("system");
                    User user = userRepository.findByEmailIgnoreCase(currentUserEmail).orElse(null);

                    AuditLog auditLog = new AuditLog();
                    auditLog.setAction("AUTO_CANCEL_TICKETS");
                    auditLog.setTargetEntity("TRIP");
                    auditLog.setTargetId(tripId);
                    auditLog.setDetails(String.format(
                            "{\"trip_id\":%d,\"cancelled_tickets_count\":%d,\"reason\":\"Trip status changed to departed\"}",
                            tripId, cancelledCount));
                    if (user != null) {
                        auditLog.setUser(user);
                    }
                    auditLogService.save(auditLog);
                    System.out.println("Audit log created successfully");
                } catch (Exception auditException) {
                    System.err.println(
                            "Failed to create audit log for auto-cancel tickets: " + auditException.getMessage());
                }
            }

            System.out.println("=== END DEBUG: autoCancelValidTicketsWhenTripDeparted ===");
            return cancelledCount;
        } catch (Exception e) {
            System.err.println("Error auto-cancelling valid tickets for trip " + tripId + ": " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public List<TicketResponseDTO> getTicketByOperatorId(Long operatorId) {
        List<Tickets> tickets = ticketRepository.findByOperatorId(operatorId);
        return tickets.stream()
                .map(ticketMapper::toTicketResponseDTO)
                .collect(Collectors.toList());
    }
}
