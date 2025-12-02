package com.busify.project.payment.strategy.impl;

import com.busify.project.common.event.PaymentSuccessEvent;
import com.busify.project.common.publisher.BusifyEventPublisher;
import com.busify.project.payment.config.VNPayConfig;
import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.exception.PaymentNotFoundException;
import com.busify.project.payment.exception.VNPayException;
import com.busify.project.payment.repository.PaymentRepository;
import com.busify.project.payment.strategy.PaymentStrategy;
import com.busify.project.payment.util.VNPayUtil;
import com.busify.project.ticket.service.TicketService;
import com.busify.project.trip_seat.services.SeatReleaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class VNPayPaymentStrategy implements PaymentStrategy {

    private final VNPayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;
    private final BusifyEventPublisher eventPublisher;
    private final SeatReleaseService seatReleaseService;
    private final TicketService ticketService;

    @Override
    public String createPaymentUrl(Payment paymentEntity, PaymentRequestDTO paymentRequest) {
        try {
            // Lấy IP address (có thể lấy từ request context hoặc mặc định)
            // Chuyển đổi amount sang VND cents (VNPay yêu cầu số nguyên)
            String amount = String
                    .valueOf(paymentEntity.getAmount().multiply(new java.math.BigDecimal("100")).longValue());

            // Generate order info and return URL based on payment type
            String orderInfo;
            String returnUrl = vnPayConfig.getReturnUrl();
            if (paymentEntity.isBooking()) {
                orderInfo = "Thanh toan ve - Booking #" + paymentEntity.getBooking().getId();
            } else if (paymentEntity.isCargo()) {
                orderInfo = "Thanh toan hang hoa - Cargo #" + paymentEntity.getCargoBooking().getCargoCode();
            } else {
                throw VNPayException.urlGenerationFailed();
            }
            String orderId = paymentEntity.getTransactionCode();

            String paymentUrl = VNPayUtil.createPaymentUrl(
                    vnPayConfig.getApiUrl(),
                    vnPayConfig.getMerchantCode(),
                    vnPayConfig.getSecretKey(),
                    returnUrl,
                    orderId,
                    amount,
                    orderInfo, null);

            log.info("Created VNPay payment URL for {} payment ID: {}",
                    paymentEntity.getPaymentType(), paymentEntity.getPaymentId());
            return paymentUrl;

        } catch (Exception e) {
            log.error("Error creating VNPay payment URL: ", e);
            throw VNPayException.urlGenerationFailed(e);
        }
    }

    @Override
    public PaymentResponseDTO executePayment(Payment paymentEntity, String paymentId, String payerId) {
        try {
            // Trong VNPay, việc xác thực thường được thực hiện thông qua callback
            // Ở đây ta chỉ cập nhật trạng thái payment
            paymentEntity.setStatus(PaymentStatus.completed);
            paymentEntity.setPaidAt(LocalDateTime.now());
            paymentEntity.setPaymentGatewayId(paymentId);

            paymentRepository.save(paymentEntity);

            log.info("VNPay payment executed successfully for payment ID: {}", paymentEntity.getPaymentId());

            return PaymentResponseDTO.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.completed)
                    .build();

        } catch (Exception e) {
            log.error("Error executing VNPay payment: ", e);
            paymentEntity.setStatus(PaymentStatus.failed);
            paymentRepository.save(paymentEntity);

            throw VNPayException.paymentFailed(e);
        }
    }

    @Override
    public PaymentResponseDTO cancelPayment(Payment paymentEntity, String paymentId) {
        try {
            // VNPay không hỗ trợ cancel trực tiếp, chỉ có thể refund
            // Ở đây ta chỉ cập nhật trạng thái
            paymentEntity.setStatus(PaymentStatus.cancelled);
            paymentRepository.save(paymentEntity);

            log.info("VNPay payment cancelled for payment ID: {}", paymentEntity.getPaymentId());

            return PaymentResponseDTO.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.cancelled)
                    .build();

        } catch (Exception e) {
            log.error("Error cancelling VNPay payment: ", e);
            throw VNPayException.paymentFailed(e);
        }
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.VNPAY.name().equals(paymentMethod);
    }

    /**
     * Xử lý callback từ VNPay (có thể gọi từ controller)
     */
    public PaymentResponseDTO handleCallback(String transactionCode, String responseCode, String amount,
            String orderInfo) {
        return handleCallback(transactionCode, responseCode, amount, orderInfo, null);
    }

    /**
     * Xử lý callback từ VNPay với VNPay transaction number
     */
    public PaymentResponseDTO handleCallback(String transactionCode, String responseCode, String amount,
            String orderInfo, String vnpTransactionNo) {
        try {
            // Tìm payment theo transaction code
            Payment payment = paymentRepository.findByTransactionCode(transactionCode)
                    .orElseThrow(() -> PaymentNotFoundException.transactionNotFound());

            if ("00".equals(responseCode)) { // Success
                payment.setStatus(PaymentStatus.completed);
                payment.setPaidAt(LocalDateTime.now());

                // Lưu VNPay transaction number để dùng cho refund
                if (vnpTransactionNo != null && !vnpTransactionNo.isEmpty()) {
                    payment.setPaymentGatewayId(vnpTransactionNo);
                }

                log.info("VNPay callback success for transaction: {}, VNPay TxnNo: {}",
                        transactionCode, vnpTransactionNo);
            } else {
                payment.setStatus(PaymentStatus.failed);
                log.warn("VNPay callback failed for transaction: {}, response code: {}", transactionCode, responseCode);
            }

            final Payment sPayment = paymentRepository.save(payment);

            // Handle post-payment actions based on payment type
            if (sPayment.isBooking()) {
                // Booking payment - release seats and create tickets
                seatReleaseService.cancelReleaseTask(sPayment.getBooking().getId());

                eventPublisher.publishEvent(
                        new PaymentSuccessEvent(this, "Payment successful for transaction: " + transactionCode,
                                sPayment));

                Long bookingId = sPayment.getBooking().getId();
                System.out.println("Booking Id: " + bookingId);
                ticketService.createTicketsFromBooking(bookingId, null);

                return PaymentResponseDTO.builder()
                        .paymentId(sPayment.getPaymentId())
                        .status(sPayment.getStatus())
                        .bookingId(bookingId)
                        .build();
            } else if (sPayment.isCargo()) {
                // Cargo payment - just publish event
                // Email will be sent when cargo status is updated to CONFIRMED (after
                // inspection)
                eventPublisher.publishEvent(
                        new PaymentSuccessEvent(this, "Cargo payment successful for transaction: " + transactionCode,
                                sPayment));

                return PaymentResponseDTO.builder()
                        .paymentId(sPayment.getPaymentId())
                        .status(sPayment.getStatus())
                        .bookingId(null)
                        .cargoBookingId(sPayment.getCargoBooking().getCargoBookingId())
                        .build();
            } else {
                // Unknown payment type
                return PaymentResponseDTO.builder()
                        .paymentId(sPayment.getPaymentId())
                        .status(sPayment.getStatus())
                        .build();
            }

        } catch (Exception e) {
            log.error("Error handling VNPay callback: ", e);
            throw VNPayException.paymentFailed(e);
        }
    }
}
