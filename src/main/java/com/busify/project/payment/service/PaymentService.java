package com.busify.project.payment.service;

import org.springframework.stereotype.Service;

import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;

@Service
public interface PaymentService {

    // Define methods for payment processing
    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequest);

    // Execute PayPal payment
    PaymentResponseDTO executePayment(String dbPaymentId, String payerId);

    // Execute PayPal payment by PayPal payment ID
    PaymentResponseDTO executePaymentByPayPalId(String paypalPaymentId, String payerId);

    // Cancel PayPal payment
    PaymentResponseDTO cancelPayment(String paymentId);

    // Cancel PayPal payment by PayPal payment ID
    PaymentResponseDTO cancelPaymentByPayPalId(String paypalPaymentId);
}
