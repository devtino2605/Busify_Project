package com.busify.project.refund.strategy.impl;

import com.busify.project.payment.config.VNPayConfig;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.refund.dto.response.RefundResponseDTO;
import com.busify.project.refund.entity.Refund;
import com.busify.project.refund.enums.RefundStatus;
import com.busify.project.refund.repository.RefundRepository;
import com.busify.project.refund.strategy.RefundStrategy;
import com.busify.project.refund.util.VNPayRefundUtil;
import com.busify.project.refund.util.NetworkUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class VNPayRefundStrategy implements RefundStrategy {

    private final VNPayConfig vnPayConfig;
    private final RefundRepository refundRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public RefundResponseDTO processRefund(Refund refund) {
        try {
            log.info("Processing VNPay refund for refund ID: {}", refund.getRefundId());

            // Gọi VNPay refund API với partial refund để refund exact net amount
            String vnpayResponse = VNPayRefundUtil.createRefundRequest(
                    vnPayConfig.getMerchantCode(),
                    vnPayConfig.getSecretKey(),
                    refund.getPayment().getTransactionCode(),
                    refund.getNetRefundAmount(), // Net amount after fees
                    refund.getRefundReason(),
                    refund.getPayment().getPaymentGatewayId(), // Original VNPay transaction ID
                    formatPaymentDate(refund.getPayment().getPaidAt()), // Original transaction date
                    NetworkUtil.getClientIpAddress(), // Get real IP from request context
                    "03" // Partial refund để refund exact amount
            );

            // Parse response từ VNPay
            VNPayRefundResponse response = parseVNPayResponse(vnpayResponse);

            if (response.isSuccess()) {
                // Refund thành công
                refund.setStatus(RefundStatus.COMPLETED);
                refund.setCompletedAt(Instant.now());
                refund.setGatewayResponse(vnpayResponse);
                refund.setGatewayRefundId(response.getGatewayRefundId());

                // Cập nhật payment status
                refund.getPayment().setStatus(PaymentStatus.refunded);

                log.info(
                        "VNPay refund completed successfully for refund ID: {}, gateway refund ID: {}, response code: {}",
                        refund.getRefundId(), response.getGatewayRefundId(), response.getResponseCode());

            } else {
                // Refund thất bại
                refund.setStatus(RefundStatus.FAILED);
                refund.setGatewayResponse(vnpayResponse);

                log.warn("VNPay refund failed for refund ID: {}, response code: {}, message: {}, full response: {}",
                        refund.getRefundId(), response.getResponseCode(), response.getMessage(), vnpayResponse);
            }

            refund.setProcessedAt(Instant.now());
            refund = refundRepository.save(refund);

            return mapToDTO(refund);

        } catch (Exception e) {
            log.error("Error processing VNPay refund for refund ID: {}", refund.getRefundId(), e);

            refund.setStatus(RefundStatus.FAILED);
            refund.setProcessedAt(Instant.now());
            refund.setNotes("Error: " + e.getMessage());
            refund = refundRepository.save(refund);

            return mapToDTO(refund);
        }
    }

    @Override
    public RefundResponseDTO checkRefundStatus(Refund refund) {
        try {
            log.info("Checking VNPay refund status for refund ID: {}", refund.getRefundId());

            // Gọi VNPay query API để check status
            // Tạm thời return current status
            return mapToDTO(refund);

        } catch (Exception e) {
            log.error("Error checking VNPay refund status for refund ID: {}", refund.getRefundId(), e);
            return mapToDTO(refund);
        }
    }

    @Override
    public boolean supports(String paymentMethod) {
        return PaymentMethod.VNPAY.name().equals(paymentMethod);
    }

    private RefundResponseDTO mapToDTO(Refund refund) {
        return RefundResponseDTO.builder()
                .refundId(refund.getRefundId())
                .paymentId(refund.getPayment().getPaymentId())
                .refundAmount(refund.getRefundAmount())
                .cancellationFee(refund.getCancellationFee())
                .netRefundAmount(refund.getNetRefundAmount())
                .refundReason(refund.getRefundReason())
                .status(refund.getStatus())
                .refundTransactionCode(refund.getRefundTransactionCode())
                .gatewayRefundId(refund.getGatewayRefundId())
                .requestedAt(refund.getRequestedAt())
                .processedAt(refund.getProcessedAt())
                .completedAt(refund.getCompletedAt())
                .notes(refund.getNotes())
                .build();
    }

    /**
     * Parse VNPay response JSON
     */
    private VNPayRefundResponse parseVNPayResponse(String vnpayResponse) {
        try {
            if (vnpayResponse == null || vnpayResponse.trim().isEmpty()) {
                return new VNPayRefundResponse(false, null, "Empty response", null, null);
            }

            JsonNode jsonNode = objectMapper.readTree(vnpayResponse);

            String responseCode = jsonNode.has("vnp_ResponseCode") ? jsonNode.get("vnp_ResponseCode").asText() : null;
            String message = jsonNode.has("vnp_Message") ? jsonNode.get("vnp_Message").asText() : null;
            String transactionNo = jsonNode.has("vnp_TransactionNo") ? jsonNode.get("vnp_TransactionNo").asText()
                    : null;
            String responseId = jsonNode.has("vnp_ResponseId") ? jsonNode.get("vnp_ResponseId").asText() : null;

            // VNPay success code là "00"
            boolean isSuccess = "00".equals(responseCode);

            // Ưu tiên TransactionNo, fallback to ResponseId
            String gatewayRefundId = transactionNo != null ? transactionNo : responseId;

            return new VNPayRefundResponse(isSuccess, responseCode, message, gatewayRefundId, transactionNo);

        } catch (Exception e) {
            log.error("Error parsing VNPay response: {}", e.getMessage());
            return new VNPayRefundResponse(false, null, "JSON parse error: " + e.getMessage(), null, null);
        }
    }

    /**
     * VNPay Refund Response wrapper
     */
    private static class VNPayRefundResponse {
        private final boolean success;
        private final String responseCode;
        private final String message;
        private final String gatewayRefundId;
        private final String transactionNo;

        public VNPayRefundResponse(boolean success, String responseCode, String message,
                String gatewayRefundId, String transactionNo) {
            this.success = success;
            this.responseCode = responseCode;
            this.message = message;
            this.gatewayRefundId = gatewayRefundId;
            this.transactionNo = transactionNo;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getResponseCode() {
            return responseCode;
        }

        public String getMessage() {
            return message;
        }

        public String getGatewayRefundId() {
            return gatewayRefundId;
        }

        public String getTransactionNo() {
            return transactionNo;
        }
    }

    /**
     * Format payment date theo format VNPay yêu cầu: yyyyMMddHHmmss
     */
    private String formatPaymentDate(Instant paidAt) {
        if (paidAt == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return paidAt.atZone(ZoneId.systemDefault()).format(formatter);
    }
}
