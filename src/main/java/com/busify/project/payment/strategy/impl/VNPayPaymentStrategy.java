package com.busify.project.payment.strategy.impl;

import com.busify.project.common.event.PaymentSuccessEvent;
import com.busify.project.common.publisher.BusifyEventPublisher;
import com.busify.project.payment.config.VNPayConfig;
import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.repository.PaymentRepository;
import com.busify.project.payment.strategy.PaymentStrategy;
import com.busify.project.payment.util.VNPayUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class VNPayPaymentStrategy implements PaymentStrategy {

    private final VNPayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;
    private final BusifyEventPublisher eventPublisher;

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
            throw new RuntimeException("Không thể tạo URL thanh toán VNPay: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponseDTO executePayment(Payment paymentEntity, String paymentId, String payerId) {
        try {
            // Trong VNPay, việc xác thực thường được thực hiện thông qua callback
            // Ở đây ta chỉ cập nhật trạng thái payment
            paymentEntity.setStatus(PaymentStatus.completed);
            paymentEntity.setPaidAt(Instant.now());
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

            throw new RuntimeException("Lỗi xử lý thanh toán VNPay: " + e.getMessage());
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
            throw new RuntimeException("Lỗi hủy thanh toán VNPay: " + e.getMessage());
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
        try {
            // Tìm payment theo transaction code
            Payment payment = paymentRepository.findByTransactionCode(transactionCode)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch: " + transactionCode));

            if ("00".equals(responseCode)) { // Success
                payment.setStatus(PaymentStatus.completed);
                payment.setPaidAt(Instant.now());
                log.info("VNPay callback success for transaction: {}", transactionCode);
            } else {
                payment.setStatus(PaymentStatus.failed);
                log.warn("VNPay callback failed for transaction: {}, response code: {}", transactionCode, responseCode);
            }

            final Payment sPayment = paymentRepository.save(payment);
            eventPublisher.publishEvent(
                    new PaymentSuccessEvent(this, "Payment successful for transaction: " + transactionCode, sPayment));
            return PaymentResponseDTO.builder()
                    .paymentId(payment.getPaymentId())
                    .bookingId(payment.getBooking().getId())
                    .status(payment.getStatus())
                    .bookingId(payment.getBooking().getId())
                    .build();

        } catch (Exception e) {
            log.error("Error handling VNPay callback: ", e);
            throw new RuntimeException("Lỗi xử lý callback VNPay: " + e.getMessage());
        }
    }
}
