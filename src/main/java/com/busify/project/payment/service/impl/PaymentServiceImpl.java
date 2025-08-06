package com.busify.project.payment.service.impl;

import com.busify.project.booking.dto.response.BookingDetailResponseDTO;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.repository.BookingsRepository;
import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentDetailResponseDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.repository.PaymentRepository;
import com.busify.project.payment.service.PaymentService;
import com.busify.project.payment.strategy.PaymentStrategy;
import com.busify.project.payment.strategy.PaymentStrategyFactory;
import com.busify.project.user.entity.Profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingsRepository bookingsRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequest) {
        // Lấy thông tin booking
        Bookings booking = bookingsRepository.findById(paymentRequest.getBookingId())
                .orElseThrow(() -> new RuntimeException("Not found booking"));

        // Kiểm tra xem đã có payment nào cho booking này chưa
        Payment existingPayment = findExistingPayment(booking.getId());

        Payment paymentEntity;
        if (existingPayment != null) {
            paymentEntity = existingPayment;
            log.info("Found existing payment for booking {}: Payment ID {}", booking.getId(),
                    paymentEntity.getPaymentId());

            // Nếu payment đã completed hoặc cancelled, không cho phép thanh toán lại
            if (paymentEntity.getStatus() == PaymentStatus.completed) {
                throw new RuntimeException("This booking has been successfully paid");
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

            return PaymentResponseDTO.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.pending)
                    .paymentUrl(paymentUrl)
                    .build();

        } catch (Exception e) {
            log.error("Error creating payment: ", e);
            throw new RuntimeException("Error creating payment: " + e.getMessage());
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
                    .orElseThrow(
                            () -> new RuntimeException("Not found payment with PayPal ID: " + paypalPaymentId));

            // Lấy strategy phù hợp
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentEntity.getPaymentMethod());

            // Execute payment thông qua strategy
            return strategy.executePayment(paymentEntity, paypalPaymentId, payerId);

        } catch (Exception e) {
            log.error("Error processing payment: ", e);
            throw new RuntimeException("Error processing payment: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponseDTO executePayment(String dbPaymentId, String payerId) {
        try {
            // Lấy thông tin payment từ database
            Payment paymentEntity = paymentRepository.findById(Long.valueOf(dbPaymentId))
                    .orElseThrow(() -> new RuntimeException("Not found payment"));

            // Lấy strategy phù hợp
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentEntity.getPaymentMethod());

            // Get payment ID from entity (specific to each gateway)
            String gatewayPaymentId = getGatewayPaymentId(paymentEntity);

            // Execute payment thông qua strategy
            return strategy.executePayment(paymentEntity, gatewayPaymentId, payerId);

        } catch (Exception e) {
            log.error("Error processing payment: ", e);
            throw new RuntimeException("Error processing payment: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponseDTO cancelPayment(String paymentId) {
        try {
            Payment paymentEntity = paymentRepository.findById(Long.valueOf(paymentId))
                    .orElseThrow(() -> new RuntimeException("Not found payment"));

            // Lấy strategy phù hợp
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentEntity.getPaymentMethod());

            // Get payment ID from entity (specific to each gateway)
            String gatewayPaymentId = getGatewayPaymentId(paymentEntity);

            // Cancel payment thông qua strategy
            return strategy.cancelPayment(paymentEntity, gatewayPaymentId);

        } catch (Exception e) {
            log.error("Error cancelling payment: ", e);
            throw new RuntimeException("Error cancelling payment: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponseDTO cancelPaymentByPayPalId(String paypalPaymentId) {
        try {
            Payment paymentEntity = paymentRepository.findByPaymentGatewayId(paypalPaymentId)
                    .orElseThrow(
                            () -> new RuntimeException("Not found payment with PayPal ID: " + paypalPaymentId));

            // Lấy strategy phù hợp
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(paymentEntity.getPaymentMethod());

            // Cancel payment thông qua strategy
            return strategy.cancelPayment(paymentEntity, paypalPaymentId);

        } catch (Exception e) {
            log.error("Error cancelling payment with PayPal ID: ", e);
            throw new RuntimeException("Error cancelling payment: " + e.getMessage());
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
                throw new RuntimeException("Credit card payment not implemented");
            case BANK_TRANSFER:
                throw new RuntimeException("Bank transfer payment not implemented");
            default:
                throw new RuntimeException("Unsupported payment method: " + paymentEntity.getPaymentMethod());
        }
    }

    private String generateTransactionCode() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    @Override
    public PaymentDetailResponseDTO getPaymentDetails(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Not found payment with ID: " + paymentId));
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
}
