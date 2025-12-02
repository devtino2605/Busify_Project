package com.busify.project.payment.strategy.impl;

import com.busify.project.common.event.PaymentSuccessEvent;
import com.busify.project.common.publisher.BusifyEventPublisher;
import com.busify.project.payment.config.MoMoConfig;
import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.repository.PaymentRepository;
import com.busify.project.payment.strategy.PaymentStrategy;
import com.busify.project.payment.util.MoMoUtil;
import com.busify.project.trip_seat.services.SeatReleaseService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * MoMoPaymentStrategy
 * 
 * Implementation of Payment Strategy for MoMo payment gateway
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-12-01
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MoMoPaymentStrategy implements PaymentStrategy {

    private final MoMoConfig moMoConfig;
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final BusifyEventPublisher eventPublisher;
    private final SeatReleaseService seatReleaseService;

    @Override
    public String createPaymentUrl(Payment paymentEntity, PaymentRequestDTO paymentRequest) {
        try {
            // Generate request ID (unique for each request)
            String requestId = MoMoUtil.generateRequestId();

            // Generate order ID from transaction code
            String orderId = MoMoUtil.generateOrderId(paymentEntity.getTransactionCode());

            // Generate order info based on payment type
            String orderInfo;
            if (paymentEntity.isBooking()) {
                orderInfo = "Thanh toan ve xe - Booking #" + paymentEntity.getBooking().getBookingCode();
            } else if (paymentEntity.isCargo()) {
                orderInfo = "Thanh toan hang hoa - Cargo #" + paymentEntity.getCargoBooking().getCargoCode();
            } else {
                throw new RuntimeException("Invalid payment type");
            }

            // Amount in VND (MoMo uses Long, no decimal)
            Long amount = paymentEntity.getAmount().longValue();

            // Extra data (optional - can be empty string)
            String extraData = "";

            // Generate signature
            String signature = MoMoUtil.generateSignature(
                    moMoConfig.getAccessKey(),
                    amount,
                    extraData,
                    moMoConfig.getIpnUrl(),
                    orderId,
                    orderInfo,
                    moMoConfig.getPartnerCode(),
                    moMoConfig.getRedirectUrl(),
                    requestId,
                    moMoConfig.getRequestType(),
                    moMoConfig.getSecretKey());

            // Build request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", moMoConfig.getPartnerCode());
            requestBody.put("partnerName", "Busify");
            requestBody.put("storeId", "BusifyStore");
            requestBody.put("requestId", requestId);
            requestBody.put("amount", amount);
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", orderInfo);
            requestBody.put("redirectUrl", moMoConfig.getRedirectUrl());
            requestBody.put("ipnUrl", moMoConfig.getIpnUrl());
            requestBody.put("lang", "vi");
            requestBody.put("extraData", extraData);
            requestBody.put("requestType", moMoConfig.getRequestType());
            requestBody.put("signature", signature);

            log.info("Creating MoMo payment URL for order: {}", orderId);
            log.debug("MoMo request body: {}", requestBody);

            // Call MoMo API
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    moMoConfig.getCreatePaymentUrl(),
                    entity,
                    String.class);

            // Parse response
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            int resultCode = jsonResponse.get("resultCode").asInt();
            String message = jsonResponse.get("message").asText();

            if (resultCode == 0) {
                // Success - get payment URL
                String payUrl = jsonResponse.get("payUrl").asText();

                // Save MoMo request ID for tracking
                paymentEntity.setPaymentGatewayId(requestId);
                paymentRepository.save(paymentEntity);

                log.info("MoMo payment URL created successfully for payment ID: {}", paymentEntity.getPaymentId());
                return payUrl;
            } else {
                // Failed
                log.error("MoMo payment creation failed. Code: {}, Message: {}", resultCode, message);
                throw new RuntimeException("MoMo payment failed: " + message);
            }

        } catch (Exception e) {
            log.error("Error creating MoMo payment URL: ", e);
            throw new RuntimeException("Failed to create MoMo payment", e);
        }
    }

    @Override
    public PaymentResponseDTO executePayment(Payment paymentEntity, String paymentId, String payerId) {
        try {
            // MoMo payment is auto-executed via IPN callback
            // This method is called after receiving successful IPN

            paymentEntity.setStatus(PaymentStatus.completed);
            paymentEntity.setPaidAt(LocalDateTime.now());
            paymentEntity.setUpdatedAt(LocalDateTime.now());

            // paymentId here is MoMo's transId
            if (paymentId != null && !paymentId.isEmpty()) {
                paymentEntity.setPaymentGatewayId(paymentId);
            }

            paymentRepository.save(paymentEntity);

            log.info("MoMo payment executed successfully for payment ID: {}", paymentEntity.getPaymentId());

            return PaymentResponseDTO.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.completed)
                    .bookingId(paymentEntity.isBooking() ? paymentEntity.getBooking().getId() : null)
                    .build();

        } catch (Exception e) {
            log.error("Error executing MoMo payment: ", e);
            paymentEntity.setStatus(PaymentStatus.failed);
            paymentEntity.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(paymentEntity);

            throw new RuntimeException("Failed to execute MoMo payment", e);
        }
    }

    @Override
    public PaymentResponseDTO cancelPayment(Payment paymentEntity, String paymentId) {
        try {
            // MoMo doesn't require explicit cancellation
            // Just update status in our system
            paymentEntity.setStatus(PaymentStatus.cancelled);
            paymentEntity.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(paymentEntity);

            log.info("MoMo payment cancelled for payment ID: {}", paymentEntity.getPaymentId());

            return PaymentResponseDTO.builder()
                    .paymentId(paymentEntity.getPaymentId())
                    .status(PaymentStatus.cancelled)
                    .build();

        } catch (Exception e) {
            log.error("Error cancelling MoMo payment: ", e);
            throw new RuntimeException("Failed to cancel MoMo payment", e);
        }
    }

    @Override
    public boolean supports(String paymentMethod) {
        return "MOMO".equalsIgnoreCase(paymentMethod);
    }

    /**
     * Verify MoMo IPN callback signature
     * 
     * @param params IPN parameters from MoMo
     * @return true if signature is valid
     */
    public boolean verifyIpnSignature(Map<String, String> params) {
        String receivedSignature = params.get("signature");
        return MoMoUtil.verifySignature(params, receivedSignature, moMoConfig.getSecretKey());
    }

    /**
     * Handle MoMo callback (IPN or redirect)
     * Similar to VNPay's handleCallback
     * 
     * @param orderId    Order ID (cleaned transaction code)
     * @param resultCode Result code from MoMo
     * @param amount     Amount string
     * @param orderInfo  Order information
     * @param transId    MoMo transaction ID
     * @return PaymentResponseDTO
     */
    public PaymentResponseDTO handleCallback(String orderId, String resultCode, String amount,
            String orderInfo, String transId) {
        try {
            // Find payment by cleaned transaction code
            // Note: orderId is alphanumeric-only version of transactionCode
            Payment payment = paymentRepository.findByTransactionCode(orderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));

            if ("0".equals(resultCode)) { // MoMo success code is "0"
                payment.setStatus(PaymentStatus.completed);
                payment.setPaidAt(LocalDateTime.now());

                // Save MoMo transaction ID for refund
                if (transId != null && !transId.isEmpty()) {
                    payment.setPaymentGatewayId(transId);
                }

                log.info("MoMo callback success for order: {}, MoMo transId: {}", orderId, transId);
            } else {
                payment.setStatus(PaymentStatus.failed);
                log.warn("MoMo callback failed for order: {}, result code: {}", orderId, resultCode);
            }

            payment.setUpdatedAt(LocalDateTime.now());
            Payment savedPayment = paymentRepository.save(payment);

            // Handle post-payment actions based on payment type
            if (savedPayment.isBooking()) {
                // Booking payment - release seats and publish event
                seatReleaseService.cancelReleaseTask(savedPayment.getBooking().getId());

                eventPublisher.publishEvent(
                        new PaymentSuccessEvent(this, "MoMo payment successful for order: " + orderId,
                                savedPayment));

                return PaymentResponseDTO.builder()
                        .paymentId(savedPayment.getPaymentId())
                        .status(savedPayment.getStatus())
                        .bookingId(savedPayment.getBooking().getId())
                        .build();
            } else if (savedPayment.isCargo()) {
                // Cargo payment - just publish event
                // Email will be sent when cargo status is updated to CONFIRMED (after
                // inspection)
                eventPublisher.publishEvent(
                        new PaymentSuccessEvent(this, "MoMo cargo payment successful for order: " + orderId,
                                savedPayment));

                return PaymentResponseDTO.builder()
                        .paymentId(savedPayment.getPaymentId())
                        .status(savedPayment.getStatus())
                        .cargoBookingId(savedPayment.getCargoBooking().getCargoBookingId())
                        .build();
            } else {
                return PaymentResponseDTO.builder()
                        .paymentId(savedPayment.getPaymentId())
                        .status(savedPayment.getStatus())
                        .build();
            }

        } catch (Exception e) {
            log.error("Error handling MoMo callback: ", e);
            throw new RuntimeException("Failed to handle MoMo callback", e);
        }
    }
}
