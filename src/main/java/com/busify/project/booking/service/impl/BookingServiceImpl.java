package com.busify.project.booking.service.impl;

import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.auth.service.EmailService;
import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.dto.response.BookingDetailResponse;
import com.busify.project.booking.dto.response.BookingHistoryResponse;
import com.busify.project.booking.dto.response.BookingStatusCountDTO;
import com.busify.project.booking.dto.response.BookingUpdateResponseDTO;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.mapper.BookingMapper;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.booking.service.BookingService;
import com.busify.project.booking.exception.BookingNotFoundException;
import com.busify.project.booking.exception.BookingUnauthorizedException;
import com.busify.project.booking.exception.BookingSeatUnavailableException;
import com.busify.project.booking.exception.BookingAuthenticationException;
import com.busify.project.booking.exception.BookingPromotionException;
import com.busify.project.booking.exception.BookingCreationException;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.enums.PromotionStatus;
import com.busify.project.promotion.repository.PromotionRepository;
import com.busify.project.ticket.entity.Tickets;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip_seat.entity.TripSeat;
import com.busify.project.trip_seat.enums.TripSeatStatus;
import com.busify.project.trip_seat.repository.TripSeatRepository;
import com.busify.project.trip_seat.services.SeatReleaseService;
import com.busify.project.trip_seat.services.TripSeatService;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final TripSeatRepository tripSeatRepository;
    private final BookingRepository bookingRepository;
    private final JwtUtils jwtUtil;
    private final AuditLogService auditLogService;
    private final EmailService emailService;
    private final TripSeatService tripSeatService;
    private final SeatReleaseService seatReleaseService;
    private final PromotionRepository promotionRepository;

    @Override
    public ApiResponse<?> getBookingHistory(int page, int size) {
        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";

        // 2. Lấy user từ DB dựa trên email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 3. Truy vấn booking theo user.id
        Pageable pageable = PageRequest.of(page - 1, size); // page Spring bắt đầu từ 0
        Page<Bookings> bookingPage = bookingRepository.findByCustomerId(user.getId(), pageable);

        // 4. Mapping booking sang DTO
        List<BookingHistoryResponse> content = bookingPage
                .stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());

        // 5. Đóng gói response
        Map<String, Object> response = new HashMap<>();
        response.put("result", content);
        response.put("pageNumber", bookingPage.getNumber() + 1); // Trả về bắt đầu từ 1
        response.put("pageSize", bookingPage.getSize());
        response.put("totalRecords", bookingPage.getTotalElements());
        response.put("totalPages", bookingPage.getTotalPages());
        response.put("hasNext", bookingPage.hasNext());
        response.put("hasPrevious", bookingPage.hasPrevious());

        return ApiResponse.success("Lấy lịch sử đặt vé thành công", response);
    }

    @Transactional
    public BookingAddResponseDTO addBooking(BookingAddRequestDTO request) {

        String email = jwtUtil.getCurrentUserLogin()
                .orElseThrow(() -> new BookingAuthenticationException(
                        "User not authenticated. Please login to make a booking."));

        log.info("info of request: {}", request);
        // Try both case sensitive and case insensitive search
        User customer = userRepository.findByEmail(email)
                .or(() -> userRepository.findByEmailIgnoreCase(email))
                .orElseThrow(() -> new BookingCreationException("User not found with email: " + email));

        // find promotion by code and status active
        Optional<Promotion> promotionOpt = promotionRepository.findByCode(request.getDiscountCode());

        promotionOpt.ifPresent(p -> {
            ;if(p.getStatus() == PromotionStatus.expired) {
                throw BookingPromotionException.promotionExpired(p.getCode());
            } else if (p.getStatus() == PromotionStatus.inactive) {
                throw BookingPromotionException.promotionNotActive(p.getCode());
            } else {
                int used = promotionRepository.existsUserUseCode(customer.getId(), p.getPromotionId());
                if (used == 1)
                    throw BookingPromotionException.promotionAlreadyUsed(p.getCode());
                if (p.getUsageLimit() == null || p.getUsageLimit() <= 0)
                    throw BookingPromotionException.usageLimitExceeded(p.getCode());
            }
        });

        final Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new BookingCreationException("Trip not found with ID: " + request.getTripId()));

        Bookings booking = bookingRepository.save(
                BookingMapper.fromRequestDTOtoEntity(
                        request, trip, customer,
                        request.getGuestFullName(), request.getGuestPhone(), request.getGuestEmail(),
                        request.getGuestAddress(), promotionOpt.orElse(null)));

        updatePromotionUsageAndUserUse(promotionOpt.orElse(null), customer);

        String[] seatNumbers = request.getSeatNumber().split(",");
        for (String seatNum : seatNumbers) {
            lockSeat(seatNum.trim(), customer, trip.getId());
            seatReleaseService.scheduleRelease(seatNum.trim(), booking.getId());
        }

        return BookingMapper.toResponseAddDTO(booking);
    }

    @Transactional
    public void updatePromotionUsageAndUserUse(Promotion promotion, User user) {
        if (promotion != null) {
            if (promotion.getUsageLimit() <= 0) {
                throw BookingPromotionException.usageLimitExceeded(promotion.getCode());
            }
            promotion.setUsageLimit(promotion.getUsageLimit() - 1);
        }

        // Cập nhật quan hệ many-to-many và chỉ save một lần
        if (user != null && promotion != null) {
            Profile profile = (Profile) user;

            if (!promotion.getProfiles().contains(profile)) {
                promotion.getProfiles().add(profile);
                promotionRepository.save(promotion);
            }

        }
    }

    @Transactional
    public TripSeat lockSeat(String seatNumber, User user, Long tripId) {
        TripSeat seat = tripSeatRepository.findTripSeatBySeatNumberAndTripId(seatNumber, tripId)
                .orElseThrow(() -> new BookingSeatUnavailableException("Seat " + seatNumber + " not found"));

        if (seat.getStatus() != TripSeatStatus.available) {
            throw new BookingSeatUnavailableException("Seat " + seatNumber + " is not available");
        }

        seat.setStatus(TripSeatStatus.locked);
        seat.setLockingUser(user);
        seat.setLockedAt(LocalDateTime.now());

        return tripSeatRepository.save(seat);
    }

    @Override
    public ApiResponse<?> getBookingDetail(String bookingCode) {
        Bookings booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new BookingNotFoundException(bookingCode));

        BookingDetailResponse dto = BookingMapper.toDetailDTO(booking);
        return ApiResponse.success("Lấy chi tiết đặt vé thành công", List.of(dto));
    }

    @Override
    public BookingUpdateResponseDTO updateBooking(String bookingCode, BookingAddRequestDTO request) {
        try {

            // check user
            String email = jwtUtil.getCurrentUserLogin()
                    .orElseThrow(() -> new BookingAuthenticationException(
                            "User not authenticated. Please login to update a booking."));

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BookingAuthenticationException("User not found with email: " + email));

            // get booking
            Bookings booking = bookingRepository.findByBookingCode(bookingCode)
                    .orElseThrow(() -> new BookingNotFoundException(bookingCode));

            // 3. Kiểm tra
            String roleName = user.getRole().getName();
            if (roleName.equals("ADMIN") || roleName.equals("OPERATOR") || roleName.equals("CUSTOMER_SERVICE")) {
                // Nếu là admin, operator, hoặc customer_service thì cho phép sửa mà không cần
                // kiểm tra chủ vé
            } else {
                // Nếu không phải các quyền trên, kiểm tra xem có phải là chủ vé không
                if (!booking.getCustomer().getEmail().equals(email)) {
                    throw new BookingUnauthorizedException("You are not authorized to update this booking");
                }
            }

            // Cập nhật thông tin cho booking
            booking.setGuestFullName(request.getGuestFullName());
            booking.setGuestPhone(request.getGuestPhone());
            booking.setGuestEmail(request.getGuestEmail());
            booking.setGuestAddress(request.getGuestAddress());

            bookingRepository.save(booking);

            // Send email notification
            String fullName = booking.getGuestFullName() != null ? booking.getGuestFullName()
                    : booking.getCustomer().getEmail();
            String toEmail = booking.getGuestEmail() != null ? booking.getGuestEmail()
                    : booking.getCustomer().getEmail();

            emailService.sendBookingUpdatedEmail(toEmail, fullName, booking.getTickets());

            // ghi vào audit log
            AuditLog auditLog = new AuditLog();
            auditLog.setAction("UPDATE");
            auditLog.setTargetEntity("BOOKING");
            auditLog.setTargetId(booking.getId());
            auditLog.setDetails(String.format("{\"booking_code\":\"%s\"}", booking.getBookingCode()));
            auditLog.setUser(user);
            auditLogService.save(auditLog);

            return BookingMapper.toUpdateResponseDTO(booking);
        } catch (Exception e) {
            // Log error if needed
            return null;
        }
    }

    @Override
    public List<BookingHistoryResponse> getAllBookings() {
        try {
            List<Bookings> bookings = bookingRepository.findAll();
            return bookings.stream()
                    .map(BookingMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // Log error if needed
            return List.of();
        }
    }

    @Override
    public ApiResponse<?> searchBookings(
            String bookingCode,
            String route,
            String status,
            LocalDate departureDate,
            LocalDate arrivalDate,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size) {

        // Validate page parameters
        if (page < 1)
            page = 1;
        if (size < 1)
            size = 10;

        Pageable pageable = PageRequest.of(page - 1, size);

        // Convert status string to enum
        BookingStatus bookingStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                bookingStatus = BookingStatus.valueOf(status.toLowerCase());
            } catch (IllegalArgumentException e) {
                return ApiResponse.error(400, "Invalid status value: " + status);
            }
        }

        // Perform search
        Page<Bookings> bookingPage = bookingRepository.searchBookings(
                bookingCode,
                bookingStatus,
                route,
                startDate,
                endDate,
                departureDate,
                arrivalDate,
                pageable);

        // Map to DTOs
        List<BookingHistoryResponse> content = bookingPage
                .stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("result", content);
        response.put("pageNumber", bookingPage.getNumber() + 1);
        response.put("pageSize", bookingPage.getSize());
        response.put("totalRecords", bookingPage.getTotalElements());
        response.put("totalPages", bookingPage.getTotalPages());
        response.put("hasNext", bookingPage.hasNext());
        response.put("hasPrevious", bookingPage.hasPrevious());

        return ApiResponse.success("Tìm kiếm booking thành công", response);
    }

    @Override
    public boolean deleteBooking(String bookingCode) {
        // 1. check user
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. check booking
        Bookings booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new BookingNotFoundException(bookingCode));

        // 3. Kiểm tra
        String roleName = user.getRole().getName();
        if (roleName.equals("ADMIN") || roleName.equals("OPERATOR") || roleName.equals("CUSTOMER_SERVICE")) {
            // Nếu là admin, operator, hoặc customer_service thì cho phép xóa mà không cần
            // kiểm tra chủ vé

            // Before setting booking status to cancelled
            String fullName = booking.getGuestFullName() != null ? booking.getGuestFullName()
                    : booking.getCustomer().getEmail();
            String toEmail = booking.getGuestEmail() != null ? booking.getGuestEmail()
                    : booking.getCustomer().getEmail();

            emailService.sendBookingCancelledEmail(toEmail, fullName, booking.getTickets());

            booking.setStatus(BookingStatus.canceled_by_operator);
        } else {
            // Nếu không phải các quyền trên, kiểm tra xem có phải là chủ vé không
            if (!booking.getCustomer().getEmail().equals(email)) {
                throw new BookingUnauthorizedException("You are not authorized to cancel this booking");
            }
            booking.setStatus(BookingStatus.canceled_by_user);
        }

        bookingRepository.save(booking);

        // Update trip seat status
        for (Tickets ticket : booking.getTickets()) {
            tripSeatService.changeTripSeatStatusToAvailable(ticket.getBooking().getTrip().getId(),
                    ticket.getSeatNumber());
        }

        // 4. save audit log
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("DELETE");
        auditLog.setTargetEntity("BOOKING");
        auditLog.setTargetId(booking.getId());
        auditLog.setDetails(String.format("{\"booking_code\":\"%s\"}", booking.getBookingCode()));
        auditLog.setUser(user);
        auditLogService.save(auditLog);

        return true;
    }

    public List<BookingStatusCountDTO> getBookingStatusCounts() {
        return bookingRepository.findBookingStatusCounts();
    }

    @Override
    public List<BookingStatusCountDTO> getBookingStatusCountsByYear(int year) {
        return bookingRepository.findBookingStatusCountsByYear(year);
    }

    @Override
    @Transactional
    public int markBookingsAsCompletedWhenTripArrived(Long tripId) {
        try {
            log.info("=== DEBUG: markBookingsAsCompletedWhenTripArrived called for tripId: {} ===", tripId);
            
            // Đầu tiên, kiểm tra có booking nào của trip này không
            List<Bookings> allBookingsForTrip = bookingRepository.findAll().stream()
                .filter(b -> b.getTrip().getId().equals(tripId))
                .collect(Collectors.toList());
            
            log.info("Total bookings found for trip {}: {}", tripId, allBookingsForTrip.size());
            
            // Log trạng thái các booking trước khi cập nhật
            for (Bookings booking : allBookingsForTrip) {
                log.info("Booking ID: {}, Code: {}, Status: {}", 
                        booking.getId(), booking.getBookingCode(), booking.getStatus());
            }
            
            // Cập nhật tất cả booking có status = confirmed thành completed cho trip này
            int completedCount = bookingRepository.markBookingsAsCompletedByTripId(tripId);
            log.info("Number of bookings marked as completed: {}", completedCount);
            
            // Kiểm tra lại sau khi cập nhật
            List<Bookings> bookingsAfterUpdate = bookingRepository.findAll().stream()
                .filter(b -> b.getTrip().getId().equals(tripId))
                .collect(Collectors.toList());
                
            log.info("=== After update ===");
            for (Bookings booking : bookingsAfterUpdate) {
                log.info("Booking ID: {}, Code: {}, Status: {}", 
                        booking.getId(), booking.getBookingCode(), booking.getStatus());
            }
            
            // Log audit cho hành động tự động hoàn thành booking
            if (completedCount > 0) {
                try {
                    String currentUserEmail = jwtUtil.getCurrentUserLogin().orElse("system");
                    User user = userRepository.findByEmailIgnoreCase(currentUserEmail).orElse(null);
                    
                    AuditLog auditLog = new AuditLog();
                    auditLog.setAction("AUTO_COMPLETE_BOOKINGS");
                    auditLog.setTargetEntity("TRIP");
                    auditLog.setTargetId(tripId);
                    auditLog.setDetails(String.format(
                        "{\"trip_id\":%d,\"completed_bookings_count\":%d,\"reason\":\"Trip status changed to arrived\"}", 
                        tripId, completedCount
                    ));
                    if (user != null) {
                        auditLog.setUser(user);
                    }
                    auditLogService.save(auditLog);
                    log.info("Audit log created successfully");
                } catch (Exception auditException) {
                    log.error("Failed to create audit log for auto-complete bookings: {}", auditException.getMessage());
                }
            }
            
            log.info("=== END DEBUG: markBookingsAsCompletedWhenTripArrived ===");
            return completedCount;
        } catch (Exception e) {
            log.error("Error auto-completing bookings for trip {}: {}", tripId, e.getMessage(), e);
            return 0;
        }
    }

}