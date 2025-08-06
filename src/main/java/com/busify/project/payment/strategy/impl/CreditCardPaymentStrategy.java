package com.busify.project.payment.strategy.impl;

import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public String createPaymentUrl(Payment paymentEntity, PaymentRequestDTO paymentRequest) {

        log.info("Creating credit card payment for payment ID: {}", paymentEntity.getPaymentId());

        // Placeholder implementation
        throw new RuntimeException("Credit Card payment chưa được implement. Vui lòng sử dụng PayPal.");
    }

    @Override
    public PaymentResponseDTO executePayment(Payment paymentEntity, String paymentId, String payerId) {
        log.info("Executing credit card payment for payment ID: {}", paymentEntity.getPaymentId());

        // Placeholder implementation
        throw new RuntimeException("Credit Card payment execution chưa được implement.");
    }

    @Override
    public PaymentResponseDTO cancelPayment(Payment paymentEntity, String paymentId) {
        log.info("Cancelling credit card payment for payment ID: {}", paymentEntity.getPaymentId());

        // Placeholder implementation - simple status update
        return PaymentResponseDTO.builder()
                .paymentId(paymentEntity.getPaymentId())
                .status(PaymentStatus.cancelled)
                .build();
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.CREDIT_CARD.name().equals(paymentMethod);
    }
}
