package com.busify.project.payment.service.impl;

import com.busify.project.booking.dto.response.BookingDetailResponseDTO;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentDetailResponseDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.exception.PaymentBookingException;
import com.busify.project.payment.exception.PaymentMethodException;
import com.busify.project.payment.exception.PaymentNotFoundException;
import com.busify.project.payment.exception.PaymentProcessingException;
import com.busify.project.payment.mapper.PaymentMapper;
import com.busify.project.payment.repository.PaymentRepository;
import com.busify.project.payment.service.PaymentService;
import com.busify.project.payment.strategy.PaymentStrategy;
import com.busify.project.payment.strategy.PaymentStrategyFactory;
import com.busify.project.user.entity.Profile;
import com.busify.project.audit_log.entity.AuditLog;
import com.busify.project.audit_log.service.AuditLogService;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingsRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequest) {
        // Lấy thông tin booking
        Bookings booking = bookingsRepository.findById(paymentRequest.getBookingId())
                .orElseThrow(() -> PaymentBookingException.bookingNotFound());

        // Kiểm tra xem đã có payment nào cho booking này chưa
        Payment existingPayment = findExistingPayment(booking.getId());

        Payment paymentEntity;
        if (existingPayment != null) {
            paymentEntity = existingPayment;
            log.info("Found existing payment for booking {}: Payment ID {}", booking.getId(),
                    paymentEntity.getPaymentId());

            // Nếu payment đã completed hoặc cancelled, không cho phép thanh toán lại
            if (paymentEntity.getStatus() == PaymentStatus.completed) {
                throw PaymentBookingException.bookingAlreadyPaid();
            } else if (paymentEntity.getStatus() == PaymentStatus.cancelled
                    && paymentEntity.getStatus() == PaymentStatus.failed) {
                // Reset payment để có thể thanh toán lại
                resetPaymentForRetry(paymentEntity);
            }
            // Nếu status là pending hoặc failed, cho phép tiếp tục thanh toán
        } else {
            // Tạo payment mới
            paymentEntity = createNewPayment(booking, paymentRequest);
            log.info("Created new payment for booking {}: Payment ID {}", booking.getId(),
                    paymentEntity.getPaymentId());
        }

        // Lấy strategy phù hợp với payment method khác
        PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentRequest.getPaymentMethod());

        try {
            // Tạo payment URL thông qua strategy
            String paymentUrl = strategy.createPaymentUrl(paymentEntity, paymentRequest);

            // Audit log for payment creation
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("CREATE");
                auditLog.setTargetEntity("PAYMENT");
                auditLog.setTargetId(paymentEntity.getPaymentId());
                auditLog.setDetails(String.format("{\"payment_id\":%d,\"booking_id\":%d,\"amount\":%.2f,\"payment_method\":\"%s\",\"transaction_code\":\"%s\",\"status\":\"%s\",\"action\":\"create\"}", 
                        paymentEntity.getPaymentId(), booking.getId(), paymentEntity.getAmount(), 
                        paymentEntity.getPaymentMethod(), paymentEntity.getTransactionCode(), paymentEntity.getStatus()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
            } catch (Exception e) {
                log.error("Failed to create audit log for payment creation: {}", e.getMessage());
            }

            return PaymentResponseDTO.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.pending)
                    .paymentUrl(paymentUrl)
                    .bookingId(booking.getId())
                    .build();

        } catch (Exception e) {
            log.error("Error creating payment: ", e);
            throw PaymentProcessingException.creationFailed(e);
        }
    }

    /**
     * Tìm payment đã tồn tại cho booking và payment method
     */
    private Payment findExistingPayment(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    /**
     * Tạo payment mới
     */
    private Payment createNewPayment(Bookings booking, PaymentRequestDTO paymentRequest) {
        Payment paymentEntity = new Payment();
        paymentEntity.setBooking(booking);
        paymentEntity.setPaymentMethod(paymentRequest.getPaymentMethod());
        paymentEntity.setAmount(booking.getTotalAmount());
        paymentEntity.setTransactionCode(generateTransactionCode());
        paymentEntity.setStatus(PaymentStatus.pending);

        return paymentRepository.save(paymentEntity);
    }

    /**
     * Reset payment để có thể retry
     */
    private void resetPaymentForRetry(Payment paymentEntity) {
        paymentEntity.setStatus(PaymentStatus.pending);
        paymentEntity.setPaidAt(null);
        paymentEntity.setPaymentGatewayId(null); // Clear gateway specific IDs
        paymentRepository.save(paymentEntity);
        log.info("Reset payment {} for retry", paymentEntity.getPaymentId());
    }

    @Override
    public PaymentResponseDTO executePaymentByPayPalId(String paypalPaymentId, String payerId) {
        try {
            // Tìm payment trong database theo PayPal payment ID
            Payment paymentEntity = paymentRepository.findByPaymentGatewayId(paypalPaymentId)
                    .orElseThrow(() -> PaymentNotFoundException.notFound());

            // Lấy strategy phù hợp
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentEntity.getPaymentMethod());

            // Strategy chỉ xử lý logic thanh toán và cập nhật trạng thái
            strategy.executePayment(paymentEntity, paypalPaymentId, payerId);

            // Audit log for payment execution
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("EXECUTE");
                auditLog.setTargetEntity("PAYMENT");
                auditLog.setTargetId(paymentEntity.getPaymentId());
                auditLog.setDetails(String.format("{\"payment_id\":%d,\"paypal_payment_id\":\"%s\",\"payer_id\":\"%s\",\"amount\":%.2f,\"status\":\"%s\",\"action\":\"execute_payment\"}", 
                        paymentEntity.getPaymentId(), paypalPaymentId, payerId, paymentEntity.getAmount(), paymentEntity.getStatus()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
            } catch (Exception e) {
                log.error("Failed to create audit log for payment execution: {}", e.getMessage());
            }

            // Map từ entity sang DTO để đảm bảo bookingId có trong response
            return PaymentMapper.toResponse(paymentEntity);

        } catch (Exception e) {
            log.error("Error processing payment: ", e);
            throw PaymentProcessingException.processingFailed(e);
        }
    }

    @Override
    public PaymentResponseDTO executePayment(String dbPaymentId, String payerId) {
        try {
            // Lấy thông tin payment từ database
            Payment paymentEntity = paymentRepository.findById(Long.valueOf(dbPaymentId))
                    .orElseThrow(() -> PaymentNotFoundException.notFound());

            // Lấy strategy phù hợp
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentEntity.getPaymentMethod());

            // Get payment ID from entity (specific to each gateway)
            String gatewayPaymentId = getGatewayPaymentId(paymentEntity);

            // Execute payment thông qua strategy
            return strategy.executePayment(paymentEntity, gatewayPaymentId, payerId);

        } catch (Exception e) {
            log.error("Error processing payment: ", e);
            throw PaymentProcessingException.processingFailed(e);
        }
    }

    @Override
    public PaymentResponseDTO cancelPayment(String paymentId) {
        try {
            Payment paymentEntity = paymentRepository.findById(Long.valueOf(paymentId))
                    .orElseThrow(() -> PaymentNotFoundException.notFound());

            // Lấy strategy phù hợp
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentEntity.getPaymentMethod());

            // Get payment ID from entity (specific to each gateway)
            String gatewayPaymentId = getGatewayPaymentId(paymentEntity);

            // Cancel payment thông qua strategy
            PaymentResponseDTO result = strategy.cancelPayment(paymentEntity, gatewayPaymentId);

            // Audit log for payment cancellation
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("CANCEL");
                auditLog.setTargetEntity("PAYMENT");
                auditLog.setTargetId(paymentEntity.getPaymentId());
                auditLog.setDetails(String.format("{\"payment_id\":%d,\"gateway_payment_id\":\"%s\",\"amount\":%.2f,\"payment_method\":\"%s\",\"status\":\"%s\",\"action\":\"cancel_payment\"}", 
                        paymentEntity.getPaymentId(), gatewayPaymentId, paymentEntity.getAmount(), 
                        paymentEntity.getPaymentMethod(), paymentEntity.getStatus()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
            } catch (Exception e) {
                log.error("Failed to create audit log for payment cancellation: {}", e.getMessage());
            }

            return result;

        } catch (Exception e) {
            log.error("Error cancelling payment: ", e);
            throw PaymentProcessingException.processingFailed(e);
        }
    }

    @Override
    public PaymentResponseDTO cancelPaymentByPayPalId(String paypalPaymentId) {
        try {
            Payment paymentEntity = paymentRepository.findByPaymentGatewayId(paypalPaymentId)
                    .orElseThrow(() -> PaymentNotFoundException.notFound());

            // Lấy strategy phù hợp
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentEntity.getPaymentMethod());

            // Cancel payment thông qua strategy
            PaymentResponseDTO result = strategy.cancelPayment(paymentEntity, paypalPaymentId);

            // Audit log for payment cancellation by PayPal ID
            try {
                User currentUser = getCurrentUser();
                AuditLog auditLog = new AuditLog();
                auditLog.setAction("CANCEL");
                auditLog.setTargetEntity("PAYMENT");
                auditLog.setTargetId(paymentEntity.getPaymentId());
                auditLog.setDetails(String.format("{\"payment_id\":%d,\"paypal_payment_id\":\"%s\",\"amount\":%.2f,\"payment_method\":\"%s\",\"status\":\"%s\",\"action\":\"cancel_payment_by_paypal_id\"}", 
                        paymentEntity.getPaymentId(), paypalPaymentId, paymentEntity.getAmount(), 
                        paymentEntity.getPaymentMethod(), paymentEntity.getStatus()));
                auditLog.setUser(currentUser);
                auditLogService.save(auditLog);
            } catch (Exception e) {
                log.error("Failed to create audit log for payment cancellation by PayPal ID: {}", e.getMessage());
            }

            return result;

        } catch (Exception e) {
            log.error("Error cancelling payment with PayPal ID: ", e);
            throw PaymentProcessingException.processingFailed(e);
        }
    }

    /**
     * Lấy gateway payment ID dựa trên payment method
     */
    private String getGatewayPaymentId(Payment paymentEntity) {
        switch (paymentEntity.getPaymentMethod()) {
            case PAYPAL:
                return paymentEntity.getPaymentGatewayId();
            case CREDIT_CARD:
                throw PaymentMethodException.creditCardNotImplemented();
            case BANK_TRANSFER:
                throw PaymentMethodException.methodNotSupported();
            default:
                throw PaymentMethodException.methodNotSupported();
        }
    }

    private String generateTransactionCode() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    @Override
    public PaymentDetailResponseDTO getPaymentDetails(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> PaymentNotFoundException.notFound());
        BookingDetailResponseDTO bookingDetails = new BookingDetailResponseDTO();
        bookingDetails = BookingDetailResponseDTO.builder()
                .bookingId(payment.getBooking().getId())
                .departureTime(payment.getBooking().getTrip().getDepartureTime())
                .arrivalTime(payment.getBooking().getTrip().getEstimatedArrivalTime())
                .bookingCode(payment.getBooking().getBookingCode())
                .bookingDate(payment.getBooking().getCreatedAt())
                .departureName(payment.getBooking().getTrip().getRoute().getStartLocation().getName())
                .arrivalName(payment.getBooking().getTrip().getRoute().getEndLocation().getName())
                .status(payment.getBooking().getStatus())
                .build();

        Profile customer = null;
        if (payment.getBooking().getCustomer() != null) {
            customer = payment.getBooking().getCustomer() instanceof Profile
                    ? (Profile) payment.getBooking().getCustomer()
                    : null;
        }
        return PaymentDetailResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .amount(payment.getAmount())
                .transactionCode(payment.getTransactionCode())
                .paymentMethod(payment.getPaymentMethod())
                .bookingDetails(bookingDetails)
                .customerName(customer != null ? customer.getFullName() : payment.getBooking().getGuestFullName())
                .customerEmail(customer != null ? customer.getEmail() : payment.getBooking().getGuestEmail())
                .customerPhone(customer != null ? customer.getPhoneNumber() : payment.getBooking().getGuestPhone())
                .status(payment.getStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }

    // Helper method to get current user from SecurityContext
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }
        
        String email = authentication.getName();
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}