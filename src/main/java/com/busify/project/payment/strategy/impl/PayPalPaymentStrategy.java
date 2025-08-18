package com.busify.project.payment.strategy.impl;

import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.repository.PaymentRepository;
import com.busify.project.payment.service.CurrencyConverterService;
import com.busify.project.payment.strategy.PaymentStrategy;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayPalPaymentStrategy implements PaymentStrategy {

    private final APIContext apiContext;
    private final PaymentRepository paymentRepository;
    private final CurrencyConverterService currencyConverterService;

    @Value("${server.port}")
    private String serverPort;

    @Override
    public String createPaymentUrl(Payment paymentEntity, PaymentRequestDTO paymentRequest) {
        try {
            // Convert VND to USD for PayPal
            BigDecimal vndAmount = paymentEntity.getAmount();
            BigDecimal usdAmount = currencyConverterService.convertVndToUsd(vndAmount);

            log.info("Converting payment amount: {} VND → {} USD", vndAmount, usdAmount);

            // Tạo PayPal payment với USD amount
            com.paypal.api.payments.Payment payment = createPayPalPayment(
                    usdAmount,
                    "USD",
                    "sale",
                    "Thanh toán vé - Booking #" + paymentEntity.getBooking().getId(),
                    "http://localhost:" + serverPort + "/api/payments/cancel",
                    "http://localhost:" + serverPort + "/api/payments/success");

            // Execute payment creation
            com.paypal.api.payments.Payment createdPayment = payment.create(apiContext);

            // Lưu PayPal payment ID
            paymentEntity.setPaymentGatewayId(createdPayment.getId());
            paymentRepository.save(paymentEntity);

            // Lấy approval URL
            return createdPayment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .map(Links::getHref)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy URL phê duyệt PayPal"));

        } catch (PayPalRESTException e) {
            log.error("Lỗi khi tạo thanh toán PayPal: ", e);
            throw new RuntimeException("Lỗi khi tạo thanh toán PayPal: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponseDTO executePayment(Payment paymentEntity, String paymentId, String payerId) {
        try {
            log.info("Executing PayPal payment - DB ID: {}, PayPal ID: {}, Payer ID: {}",
                    paymentEntity.getPaymentId(), paymentId, payerId);

            // Execute PayPal payment
            com.paypal.api.payments.Payment payment = com.paypal.api.payments.Payment.get(apiContext, paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            com.paypal.api.payments.Payment executedPayment = payment.execute(apiContext, paymentExecution);

            log.info("PayPal payment state: {}", executedPayment.getState());

            if ("approved".equals(executedPayment.getState())) {
                // Cập nhật trạng thái payment
                paymentEntity.setStatus(PaymentStatus.completed);
                paymentEntity.setPaidAt(Instant.now());
                paymentRepository.save(paymentEntity);

                log.info("Payment completed successfully - DB ID: {}", paymentEntity.getPaymentId());

                return PaymentResponseDTO.builder()
                        .paymentId(paymentEntity.getPaymentId())
                        .status(PaymentStatus.completed)
                        .build();
            } else {
                paymentEntity.setStatus(PaymentStatus.failed);
                paymentRepository.save(paymentEntity);

                log.warn("Payment not approved - State: {}, DB ID: {}", executedPayment.getState(),
                        paymentEntity.getPaymentId());

                return PaymentResponseDTO.builder()
                        .paymentId(paymentEntity.getPaymentId())
                        .status(PaymentStatus.failed)
                        .build();
            }

        } catch (PayPalRESTException e) {
            log.error("Lỗi khi thực hiện thanh toán PayPal: ", e);
            throw new RuntimeException("Lỗi khi thực hiện thanh toán PayPal: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponseDTO cancelPayment(Payment paymentEntity, String paymentId) {
        try {
            paymentEntity.setStatus(PaymentStatus.cancelled);
            paymentRepository.save(paymentEntity);

            log.info("PayPal payment cancelled successfully - PayPal ID: {}, DB ID: {}", paymentId,
                    paymentEntity.getPaymentId());

            return PaymentResponseDTO.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.cancelled)
                    .build();

        } catch (Exception e) {
            log.error("Lỗi khi hủy thanh toán PayPal: ", e);
            throw new RuntimeException("Lỗi khi hủy thanh toán PayPal: " + e.getMessage());
        }
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.PAYPAL.name().equals(paymentMethod);
    }

    private com.paypal.api.payments.Payment createPayPalPayment(
            BigDecimal total,
            String currency,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {

        // Set up amount
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(total.setScale(2, RoundingMode.HALF_UP).toString());

        // Set up transaction
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Set up payer
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Set up redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        // Create payment
        com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        payment.setRedirectUrls(redirectUrls);

        return payment;
    }
}
