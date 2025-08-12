package com.busify.project.payment.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentDetailResponseDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.service.impl.PaymentServiceImpl;
import com.busify.project.payment.strategy.impl.VNPayPaymentStrategy;
import com.busify.project.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentServiceImpl paymentService;
    private final VNPayPaymentStrategy vnPayPaymentStrategy;
    private final TicketService ticketService;

    @PostMapping("/create")
    public ApiResponse<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO paymentRequest) {
        try {
            PaymentResponseDTO response = paymentService.createPayment(paymentRequest);
            return ApiResponse.<PaymentResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Payment created successfully")
                    .result(response)
                    .build();
        } catch (Exception e) {
            log.error("Error creating payment: ", e);
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Create payment fail. Error " + e.getMessage());
        }
    }

    @GetMapping("/status/{paymentId}")
    public ApiResponse<PaymentResponseDTO> getPaymentStatus(@PathVariable Long paymentId) {
        try {
            log.info("Getting payment status for payment ID: {}", paymentId);

            return ApiResponse.<PaymentResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Payment status retrieved successfully")
                    .result(PaymentResponseDTO.builder()
                            .paymentId(paymentId)
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("Error getting payment status: ", e);
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error getting payment status");
        }
    }

    @GetMapping("/success")
    public ApiResponse<PaymentResponseDTO> paymentSuccess(@RequestParam("paymentId") String paypalPaymentId,
            @RequestParam("PayerID") String payerId) {
        try {
            log.info("PayPal success callback - PayPal Payment ID: {}, Payer ID: {}",
                    paypalPaymentId, payerId);

            PaymentResponseDTO response = paymentService.executePaymentByPayPalId(paypalPaymentId, payerId);
            if (response.getStatus().name().equals("completed")) {
                // Lấy bookingId từ response hoặc từ Payment entity
                Long bookingId = response.getBookingId();
                System.out.println("Booking Id: "+bookingId);
                ticketService.createTicketsFromBooking(bookingId);
                return ApiResponse.<PaymentResponseDTO>builder()
                        .code(HttpStatus.OK.value())
                        .message("Payment executed successfully")
                        .result(response)
                        .build();
            } else {
                return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Payment not completed");
            }
        } catch (Exception e) {
            log.error("Error executing PayPal payment: ", e);
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error executing PayPal payment");
        }
    }

    @GetMapping("/cancel")
    public ApiResponse<PaymentResponseDTO> paymentCancel(
            @RequestParam(value = "paymentId", required = false) String paypalPaymentId) {
        try {
            if (paypalPaymentId != null) {
                log.info("PayPal cancel callback - PayPal Payment ID: {}", paypalPaymentId);
                // Tìm payment trong DB và cancel
            }
            // Redirect to cancel page

            PaymentResponseDTO response = paymentService.cancelPaymentByPayPalId(paypalPaymentId);
            return ApiResponse.<PaymentResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Payment cancelled successfully")
                    .result(response)
                    .build();
        } catch (Exception e) {
            log.error("Error cancelling PayPal payment: ", e);
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error cancelling PayPal payment");
        }
    }

    @GetMapping("/debug")
    public RedirectView debugCallback(@RequestParam Map<String, String> allParams) {
        log.info("PayPal Debug Callback - All parameters: {}", allParams);
        return new RedirectView("http://localhost:8080/paypal-debug.html?" +
                allParams.entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .reduce((a, b) -> a + "&" + b)
                        .orElse(""));
    }

    // VNPay callback endpoints
    @GetMapping("/vnpay/callback")
    public ApiResponse<PaymentResponseDTO> vnPayCallback(@RequestParam Map<String, String> allParams) {
        try {
            log.info("VNPay callback received with parameters: {}", allParams);

            String transactionCode = allParams.get("vnp_TxnRef");
            String responseCode = allParams.get("vnp_ResponseCode");
            String amount = allParams.get("vnp_Amount");
            String orderInfo = allParams.get("vnp_OrderInfo");

            // Xác thực chữ ký (tùy chọn - có thể bỏ qua để đơn giản)
            // boolean isValid = VNPayUtil.verifyCallback(allParams,
            // vnPayConfig.getSecretKey());
            // if (!isValid) {
            // throw new RuntimeException("Invalid VNPay signature");
            // }

            PaymentResponseDTO response = vnPayPaymentStrategy.handleCallback(
                    transactionCode, responseCode, amount, orderInfo);

            return ApiResponse.<PaymentResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("VNPay payment processed successfully")
                    .result(response)
                    .build();

        } catch (Exception e) {
            log.error("Error handling VNPay callback: ", e);
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Error handling VNPay callback");
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<PaymentDetailResponseDTO> getPaymentDetails(@PathVariable("id") Integer paymentId) {
        // Logic to retrieve payment details by paymentId
        PaymentDetailResponseDTO paymentDetails = paymentService.getPaymentDetails(paymentId.longValue());
        return ApiResponse.<PaymentDetailResponseDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Payment details retrieved successfully")
                .result(paymentDetails)
                .build();
    }

}
