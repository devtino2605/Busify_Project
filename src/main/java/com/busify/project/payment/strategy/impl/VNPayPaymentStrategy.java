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

    @Override
    public String createPaymentUrl(Payment paymentEntity, PaymentRequestDTO paymentRequest) {
        try {
            // Lấy IP address (có thể lấy từ request context hoặc mặc định)
            // Chuyển đổi amount sang VND cents (VNPay yêu cầu số nguyên)
            String amount = String
                    .valueOf(paymentEntity.getAmount().multiply(new java.math.BigDecimal("100")).longValue());

            String orderInfo = "Thanh toan ve xe buyt cho booking " + paymentEntity.getBooking().getId();
            String orderId = paymentEntity.getTransactionCode();

            String paymentUrl = VNPayUtil.createPaymentUrl(
                    vnPayConfig.getApiUrl(),
                    vnPayConfig.getMerchantCode(),
                    vnPayConfig.getSecretKey(),
                    vnPayConfig.getReturnUrl(),
                    orderId,
                    amount,
                    orderInfo, null);

            log.info("Created VNPay payment URL for payment ID: {}", paymentEntity.getPaymentId());
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

            seatReleaseService.cancelReleaseTask(sPayment.getBooking().getId());

            eventPublisher.publishEvent(
                    new PaymentSuccessEvent(this, "Payment successful for transaction: " + transactionCode, sPayment));
            return PaymentResponseDTO.builder()
                    .paymentId(payment.getPaymentId())
                    .status(payment.getStatus())
                    .bookingId(payment.getBooking().getId())
                    .build();

        } catch (Exception e) {
            log.error("Error handling VNPay callback: ", e);
            throw VNPayException.paymentFailed(e);
        }
    }
}
