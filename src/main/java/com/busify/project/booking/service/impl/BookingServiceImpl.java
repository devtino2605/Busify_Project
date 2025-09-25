package com.busify.project.booking.service.impl;

import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.auth.service.EmailService;
import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.*;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.enums.SellingMethod;
import com.busify.project.booking.mapper.BookingMapper;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.booking.service.BookingService;
import com.busify.project.booking.exception.BookingNotFoundException;
import com.busify.project.booking.exception.BookingUnauthorizedException;
import com.busify.project.booking.exception.BookingSeatUnavailableException;
import com.busify.project.booking.exception.BookingAuthenticationException;
import com.busify.project.booking.exception.BookingPromotionException;
import com.busify.project.booking.exception.BookingCreationException;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.employee.repository.EmployeeRepository;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.refund.dto.request.RefundRequestDTO;
import com.busify.project.refund.service.RefundService;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.entity.Promotion;
import com.busify.project.promotion.enums.PromotionType;
import com.busify.project.promotion.repository.PromotionRepository;
import com.busify.project.promotion.service.PromotionService;
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
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.busify.project.auth.util.PdfGeneratorUtil;
import java.io.IOException;
import java.util.Arrays;

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
    private final PromotionService promotionService;
    private final RefundService refundService;
    private final BusOperatorRepository busOperatorRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Map<String, Long> getBookingCountsByStatus() {
        // 1. Lấy user hiện tại
        String email = jwtUtil.getCurrentUserLogin()
                .orElseThrow(() -> new BookingAuthenticationException("User not authenticated."));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Khởi tạo map với tất cả các trạng thái và giá trị 0
        Map<String, Long> statusCounts = Arrays.stream(BookingStatus.values())
                .collect(Collectors.toMap(Enum::name, status -> 0L));

        // 3. Lấy số lượng từ repository
        List<Object[]> results = bookingRepository.countBookingsByStatusForCustomer(user.getId());

        // 4. Cập nhật map với số lượng thực tế
        for (Object[] result : results) {
            BookingStatus status = (BookingStatus) result[0];
            Long count = (Long) result[1];
            statusCounts.put(status.name(), count);
        }

        return statusCounts;
    }

    @Override
    public ApiResponse<?> getBookingHistory(int page, int size, String status) {
        // 1. Lấy email user hiện tại từ JWT context
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";

        // 2. Lấy user từ DB dựa trên email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 3. Truy vấn booking theo user.id và status (nếu có)
        Pageable pageable = PageRequest.of(page - 1, size); // page Spring bắt đầu từ 0
        Page<Bookings> bookingPage;

        if (status != null && !status.trim().isEmpty()) {
            try {
                BookingStatus bookingStatus = BookingStatus.valueOf(status.toLowerCase());
                bookingPage = bookingRepository.findByCustomerIdAndStatus(user.getId(), bookingStatus, pageable);
            } catch (IllegalArgumentException e) {
                return ApiResponse.error(400, "Invalid status value: " + status);
            }
        } else {
            bookingPage = bookingRepository.findByCustomerId(user.getId(), pageable);
        }

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

        Optional<Promotion> promotionOpt = Optional.empty();
        List<PromotionResponseDTO> appliedPromotions = new ArrayList<>();

        try {
            // Apply promotion by ID (AUTO type from campaign)
            if (request.getPromotionId() != null) {
                PromotionResponseDTO autoPromotion = promotionService.validateAndApplyPromotionById(
                        customer.getId(),
                        request.getPromotionId(),
                        request.getTotalAmount());

                if (autoPromotion != null) {
                    appliedPromotions.add(autoPromotion);
                }
            }

            // Apply promotion by code (COUPON type)
            if (request.getDiscountCode() != null && !request.getDiscountCode().trim().isEmpty()) {
                PromotionResponseDTO couponPromotion = promotionService.validateAndApplyPromotion(
                        customer.getId(),
                        request.getDiscountCode(),
                        request.getTotalAmount());

                if (couponPromotion != null) {
                    appliedPromotions.add(couponPromotion);
                }
            }

            // Set promotion info for the booking (use first promotion for backward
            // compatibility)
            if (!appliedPromotions.isEmpty()) {
                PromotionResponseDTO primaryPromotion = appliedPromotions.get(0);
                promotionOpt = promotionRepository.findByCode(primaryPromotion.getCode());

                // Set code để mark as used sau này
                if (request.getDiscountCode() == null || request.getDiscountCode().trim().isEmpty()) {
                    request.setDiscountCode(primaryPromotion.getCode());
                }
            }
        } catch (RuntimeException e) {
            // Determine error context based on input type
            String errorContext = "AUTO";
            if (request.getPromotionId() != null && request.getDiscountCode() != null) {
                errorContext = "CODE:" + request.getDiscountCode();
            } else if (request.getPromotionId() != null) {
                errorContext = "ID:" + request.getPromotionId();
            } else if (request.getDiscountCode() != null && !request.getDiscountCode().trim().isEmpty()) {
                errorContext = request.getDiscountCode();
            }

            throw BookingPromotionException.promotionNotApplicable(errorContext, e.getMessage());
        }

        final Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new BookingCreationException("Trip not found with ID: " + request.getTripId()));

        request.setSellingMethod(SellingMethod.ONLINE);
        Bookings booking = bookingRepository.save(
                BookingMapper.fromRequestDTOtoEntity(
                        request, trip, customer,
                        request.getGuestFullName(), request.getGuestPhone(), request.getGuestEmail(),
                        request.getGuestAddress(), promotionOpt.orElse(null)));

        // Mark promotions as used based on type
        for (PromotionResponseDTO appliedPromotion : appliedPromotions) {
            if (appliedPromotion.getPromotionType() == PromotionType.coupon) {
                // COUPON: mark existing UserPromotion as used
                promotionService.markPromotionAsUsed(customer.getId(), appliedPromotion.getCode());
            } else if (appliedPromotion.getPromotionType() == PromotionType.auto) {
                // AUTO: create new UserPromotion record and mark as used (1-time limit)
                promotionService.createAndMarkAutoPromotionAsUsed(customer.getId(), appliedPromotion.getId());
            }
        }

        String[] seatNumbers = request.getSeatNumber().split(",");
        for (String seatNum : seatNumbers) {
            lockSeat(seatNum.trim(), customer, trip.getId());
            seatReleaseService.scheduleRelease(seatNum.trim(), booking.getId());
        }

        return BookingMapper.toResponseAddDTO(booking);
    }

    @Transactional
    public BookingAddResponseDTO addBookingManual(BookingAddRequestDTO request) {

        String email = jwtUtil.getCurrentUserLogin()
                .orElseThrow(() -> new BookingAuthenticationException(
                        "User not authenticated. Please login to make a booking."));

        log.info("info of request: {}", request);
        // Try both case sensitive and case insensitive search
        User seller = userRepository.findByEmail(email)
                .or(() -> userRepository.findByEmailIgnoreCase(email))
                .orElseThrow(() -> new BookingCreationException("User not found with email: " + email));
        Logger logger = Logger.getLogger(BookingServiceImpl.class.getName());
        logger.info("User role: " + seller.getRole().getName());
        if (!seller.getRole().getName().equals("STAFF") && !seller.getRole().getName().equals("OPERATOR")
                && !seller.getRole().getName().equals("CUSTOMER_SERVICE")) {
            throw new BookingCreationException("Invalid user role");
        }

        final Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new BookingCreationException("Trip not found with ID: " + request.getTripId()));

        Bookings booking = BookingMapper.fromRequestDTOtoEntity(
                request, trip, null,
                request.getGuestFullName(), request.getGuestPhone(), request.getGuestEmail(),
                request.getGuestAddress(), null);
        booking.setSellingMethod(SellingMethod.OFFLINE);
        booking.setStatus(BookingStatus.confirmed);
        booking = bookingRepository.save(booking);

        AuditLog auditLog = new AuditLog();
        auditLog.setAction("CREATE_MANUAL_BOOKING");
        auditLog.setTargetEntity("BOOKING");
        auditLog.setTargetId(booking.getId());
        auditLog.setDetails(String.format("{\"booking_code\":\"%s\", \"trip_id\":%d, \"selling_method\":\"OFFLINE\"}",
                booking.getBookingCode(), trip.getId()));
        auditLog.setUser(seller);
        auditLogService.save(auditLog);

        String[] seatNumbers = request.getSeatNumber().split(",");
        for (String seatNum : seatNumbers) {
            lockSeat(seatNum.trim(), seller, trip.getId());
        }

        return BookingMapper.toResponseAddDTO(booking);
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

        // Thêm thông tin refund nếu booking đã hủy và đã thanh toán hoặc đã refund
        if ((booking.getStatus() == BookingStatus.canceled_by_user
                || booking.getStatus() == BookingStatus.canceled_by_operator)
                && booking.getPayment() != null && (booking.getPayment().getStatus() == PaymentStatus.completed
                        || booking.getPayment().getStatus() == PaymentStatus.refunded)) {
            dto.setRefunds(refundService.getRefundsByPaymentId(booking.getPayment().getPaymentId()));
        }

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
    public ApiResponse<?> getAllBookings(int page, int size) {
        try {
            if (page < 1)
                page = 1;
            if (size < 1)
                size = 10;

            Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
            Page<Bookings> bookingPage = bookingRepository.findAll(pageable);

            List<BookingHistoryResponse> content = bookingPage.stream()
                    .map(BookingMapper::toDTO)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("result", content);
            response.put("pageNumber", bookingPage.getNumber() + 1);
            response.put("pageSize", bookingPage.getSize());
            response.put("totalRecords", bookingPage.getTotalElements());
            response.put("totalPages", bookingPage.getTotalPages());
            response.put("hasNext", bookingPage.hasNext());
            response.put("hasPrevious", bookingPage.hasPrevious());

            return ApiResponse.success("Lấy danh sách đặt vé thành công", response);
        } catch (Exception e) {
            log.error("Error getting all bookings", e);
            return ApiResponse.error(500, "Lỗi khi lấy danh sách đặt vé: " + e.getMessage());
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
            String sellingMethod, // Added sellingMethod parameter
            int page,
            int size) {

        // Validate page parameters
        if (page < 1)
            page = 1;
        if (size < 1)
            size = 10;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        // Convert status string to enum
        BookingStatus bookingStatus = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                bookingStatus = BookingStatus.valueOf(status.toLowerCase());
            } catch (IllegalArgumentException e) {
                return ApiResponse.error(400, "Invalid status value: " + status);
            }
        }

        // Convert sellingMethod string to enum
        SellingMethod sellingMethodEnum = null;
        if (sellingMethod != null && !sellingMethod.trim().isEmpty()) {
            try {
                sellingMethodEnum = SellingMethod.valueOf(sellingMethod.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ApiResponse.error(400, "Invalid selling method value: " + sellingMethod);
            }
        }

        // Perform search
        Page<Bookings> bookingPage = bookingRepository.searchBookings(
                bookingCode,
                bookingStatus,
                route,
                sellingMethodEnum, // Pass sellingMethodEnum to repository
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

        // 3. Kiểm tra quyền
        String roleName = user.getRole().getName();
        if (roleName.equals("ADMIN") || roleName.equals("OPERATOR") || roleName.equals("CUSTOMER_SERVICE")) {
            // Nếu là admin, operator, hoặc customer_service thì cho phép xóa mà không cần
            // kiểm tra chủ vé
        } else {
            // Nếu không phải các quyền trên, kiểm tra xem có phải là chủ vé không
            if (!booking.getCustomer().getEmail().equals(email)) {
                throw new BookingUnauthorizedException("You are not authorized to cancel this booking");
            }
        }

        // Bổ sung: Kiểm tra điều kiện hoàn tiền
        Instant now = Instant.now();
        Instant createdAt = booking.getCreatedAt();
        Instant departureTime = booking.getTrip().getDepartureTime(); // Giả sử Trip có field departureTime (Instant)

        double refundPercentage = 0.0;
        String refundReason = "";

        Duration timeSinceBooking = Duration.between(createdAt, now);
        Duration timeToDeparture = Duration.between(now, departureTime);

        if (timeSinceBooking.toHours() <= 24) {
            refundPercentage = 1.0; // 100%
            refundReason = "Hủy trong vòng 24 giờ sau khi đặt";
        } else if (timeToDeparture.toHours() >= 24) {
            refundPercentage = 0.7; // 70%
            refundReason = "Hủy trước chuyến đi khoảng 1 ngày";
        } else {
            refundPercentage = 0.0; // 0%
            refundReason = "Hủy sát giờ khởi hành";
        }

        // Ghi log về hoàn tiền
        log.info("Refund calculation for booking {}: {} ({}%)", bookingCode, refundReason, refundPercentage * 100);
        // Bạn có thể tích hợp với payment service ở đây để thực hiện refund thực tế, ví
        // dụ:
        // paymentService.refund(booking.getPayment().getId(), refundPercentage);

        // Tiếp tục logic cũ
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

        // Process refund if payment exists and is completed
        processRefundIfApplicable(booking);

        // Update trip seat status
        for (Tickets ticket : booking.getTickets()) {
            tripSeatService.changeTripSeatStatusToAvailable(ticket.getBooking().getTrip().getId(),
                    ticket.getSeatNumber());
        }

        // 4. save audit log (bổ sung chi tiết hoàn tiền)
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("DELETE");
        auditLog.setTargetEntity("BOOKING");
        auditLog.setTargetId(booking.getId());
        auditLog.setDetails(
                String.format("{\"booking_code\":\"%s\", \"refund_percentage\": %.2f, \"refund_reason\": \"%s\"}",
                        booking.getBookingCode(), refundPercentage, refundReason));
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
                            tripId, completedCount));
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

    /**
     * Xử lý refund tự động nếu booking đã thanh toán
     */
    private void processRefundIfApplicable(Bookings booking) {
        try {
            Payment payment = booking.getPayment();

            // Kiểm tra có payment và đã completed chưa
            if (payment != null && payment.getStatus() == PaymentStatus.completed) {
                log.info("Processing automatic refund for booking: {}", booking.getBookingCode());

                // Tạo refund request
                RefundRequestDTO refundRequest = new RefundRequestDTO();
                refundRequest.setPaymentId(payment.getPaymentId());
                refundRequest.setRefundReason("Booking cancelled by user/operator");
                refundRequest.setNotes("Automatic refund due to booking cancellation");

                // Tạo refund (chưa process)
                refundService.createRefund(refundRequest);

                log.info("Refund request created successfully for booking: {}", booking.getBookingCode());
            } else {
                log.info("No refund needed for booking: {} (no payment or payment not completed)",
                        booking.getBookingCode());
            }
        } catch (Exception e) {
            log.error("Error processing refund for booking: {}", booking.getBookingCode(), e);
            // Không throw exception để không ảnh hưởng đến việc cancel booking
        }
    }

    @Override
    public byte[] exportBookingToPdf(String bookingCode) {
        Bookings booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new BookingNotFoundException(bookingCode));

        String fullName;
        if (booking.getGuestFullName() != null) {
            fullName = booking.getGuestFullName();
        } else {
            User customer = booking.getCustomer();
            if (customer instanceof Profile) {
                fullName = ((Profile) customer).getFullName();
            } else if (customer != null) {
                fullName = customer.getEmail(); // Fallback to email if not a Profile
            } else {
                fullName = "Khách hàng";
            }
        }

        try {
            return PdfGeneratorUtil.generateTicketPDF(fullName, booking.getTickets());
        } catch (IOException e) {
            log.error("Error generating PDF for booking {}: {}", bookingCode, e.getMessage(), e);
            throw new RuntimeException("Could not generate PDF for booking " + bookingCode, e);
        }
    }

    @Override
    public List<BookingGuestResponse> getAllGuests() {
        // 1. Lấy email user hiện tại từ JWT
        String email = jwtUtil.getCurrentUserLogin().orElse("");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Lấy operatorId từ user
        Long operatorId = 0L;

        // Nếu là OPERATOR
        if (user.getRole().getName().equals("OPERATOR")) {
            operatorId = busOperatorRepository.findOperatorIdByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy BusOperator cho user này"));
        }
        // Nếu là STAFF
        else if (user.getRole().getName().equals("STAFF")) {
            operatorId = employeeRepository.findOperatorIdByStaffUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Operator cho staff này"));
        }

        // 3. Trả về danh sách guest
        return bookingRepository.findGuestsByOperator(operatorId);
    }

}
