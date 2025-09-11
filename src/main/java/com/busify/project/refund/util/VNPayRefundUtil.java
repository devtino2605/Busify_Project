package com.busify.project.refund.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class VNPayRefundUtil {

    /**
     * Tạo request refund tới VNPay
     */
    public static String createRefundRequest(String merchantCode, String secretKey,
            String transactionCode, BigDecimal refundAmount,
            String refundReason, String originalTransactionNo,
            String originalTransactionDate, String ipAddress) {
        return createRefundRequest(merchantCode, secretKey, transactionCode, refundAmount,
                refundReason, originalTransactionNo, originalTransactionDate,
                ipAddress, "02"); // Default: toàn phần
    }

    /**
     * Tạo request refund tới VNPay với loại refund tùy chọn
     */
    public static String createRefundRequest(String merchantCode, String secretKey,
            String transactionCode, BigDecimal refundAmount,
            String refundReason, String originalTransactionNo,
            String originalTransactionDate, String ipAddress,
            String transactionType) {
        try {
            // Tạo parameters cho VNPay refund API
            Map<String, String> params = new TreeMap<>();

            params.put("vnp_RequestId", generateRequestId());
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "refund");
            params.put("vnp_TmnCode", merchantCode);
            params.put("vnp_TransactionType", transactionType); // 02: toàn phần, 03: một phần
            params.put("vnp_TxnRef", transactionCode);
            params.put("vnp_Amount", String.valueOf(refundAmount.multiply(new BigDecimal("100")).longValue()));
            params.put("vnp_OrderInfo", refundReason);

            // Chỉ thêm vnp_TransactionNo nếu có giá trị
            if (originalTransactionNo != null && !originalTransactionNo.isEmpty()) {
                params.put("vnp_TransactionNo", originalTransactionNo);
            }

            // vnp_TransactionDate phải có format chính xác
            if (originalTransactionDate != null && !originalTransactionDate.isEmpty()) {
                params.put("vnp_TransactionDate", originalTransactionDate);
            } else {
                // Nếu không có transaction date gốc, bỏ qua field này
                log.warn("Missing original transaction date for refund");
            }

            params.put("vnp_CreateDate", getCurrentDateTimeFormatted());
            params.put("vnp_CreateBy", "system");
            params.put("vnp_IpAddr", ipAddress != null ? ipAddress : "127.0.0.1");

            // Tạo secure hash
            String hashData = buildHashData(params);
            String secureHash = hmacSHA512(secretKey, hashData);
            params.put("vnp_SecureHash", secureHash);

            // Debug logging
            log.info("=== VNPay Refund Debug Info ===");
            log.info("Secret Key: {}...", secretKey.substring(0, Math.min(secretKey.length(), 8)));
            log.info("Hash Data: {}", hashData);
            log.info("Generated Secure Hash: {}", secureHash);
            log.info("All Parameters: {}", params);
            log.info("=== End Debug Info ===");

            // Gọi VNPay Refund API thật
            log.info("VNPay refund request created for transaction: {}", transactionCode);

            // Call real VNPay API
            String response = callVNPayRefundAPI(params);
            return response;

        } catch (Exception e) {
            log.error("Error creating VNPay refund request", e);
            return createErrorResponse(e.getMessage());
        }
    }

    /**
     * Gọi VNPay Refund API thật
     */
    private static String callVNPayRefundAPI(Map<String, String> params) {
        try {
            // VNPay Refund API endpoint
            String apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

            // Tạo JSON request body
            StringBuilder jsonBody = new StringBuilder();
            jsonBody.append("{");
            boolean isFirst = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!isFirst) {
                    jsonBody.append(",");
                }
                jsonBody.append("\"").append(entry.getKey()).append("\":")
                        .append("\"").append(entry.getValue()).append("\"");
                isFirst = false;
            }
            jsonBody.append("}");

            // Tạo HTTP client
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(30))
                    .build();

            // Tạo HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                    .timeout(Duration.ofSeconds(60))
                    .build();

            log.info("Calling VNPay refund API: {}", apiUrl);
            log.info("Request body: {}", jsonBody.toString());

            // Gửi request và nhận response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("VNPay API response status: {}", response.statusCode());
            log.info("VNPay API response body: {}", response.body());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                log.error("VNPay API call failed with status: {}", response.statusCode());
                return createErrorResponse("API call failed with status: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            log.error("Error calling VNPay refund API", e);
            return createErrorResponse("Network error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error calling VNPay refund API", e);
            return createErrorResponse("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Tạo error response khi API call thất bại
     */
    private static String createErrorResponse(String errorMessage) {
        return "{\n" +
                "  \"vnp_ResponseId\": \"" + generateRequestId() + "\",\n" +
                "  \"vnp_Command\": \"refund\",\n" +
                "  \"vnp_ResponseCode\": \"99\",\n" +
                "  \"vnp_Message\": \"" + errorMessage + "\",\n" +
                "  \"vnp_TmnCode\": \"ERROR\",\n" +
                "  \"vnp_TxnRef\": \"\",\n" +
                "  \"vnp_TransactionNo\": \"\",\n" +
                "  \"vnp_TransactionStatus\": \"99\"\n" +
                "}";
    }

    private static String generateRequestId() {
        return "REF" + System.currentTimeMillis();
    }

    private static String getCurrentDateTimeFormatted() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private static String buildHashData(Map<String, String> params) {
        // Theo VNPay documentation:
        // data = vnp_RequestId + "|" + vnp_Version + "|" + vnp_Command + "|" +
        // vnp_TmnCode + "|" +
        // vnp_TransactionType + "|" + vnp_TxnRef + "|" + vnp_Amount + "|" +
        // vnp_TransactionNo + "|" +
        // vnp_TransactionDate + "|" + vnp_CreateBy + "|" + vnp_CreateDate + "|" +
        // vnp_IpAddr + "|" + vnp_OrderInfo

        StringBuilder hashData = new StringBuilder();

        // Theo thứ tự chính xác từ VNPay docs
        hashData.append(params.get("vnp_RequestId")).append("|")
                .append(params.get("vnp_Version")).append("|")
                .append(params.get("vnp_Command")).append("|")
                .append(params.get("vnp_TmnCode")).append("|")
                .append(params.get("vnp_TransactionType")).append("|")
                .append(params.get("vnp_TxnRef")).append("|")
                .append(params.get("vnp_Amount")).append("|")
                .append(params.getOrDefault("vnp_TransactionNo", "")).append("|")
                .append(params.getOrDefault("vnp_TransactionDate", "")).append("|")
                .append(params.get("vnp_CreateBy")).append("|")
                .append(params.get("vnp_CreateDate")).append("|")
                .append(params.get("vnp_IpAddr")).append("|")
                .append(params.get("vnp_OrderInfo"));

        log.info("VNPay hash data string (with pipes): {}", hashData.toString());
        return hashData.toString();
    }

    private static String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }

            String hashResult = result.toString();
            log.info("Generated hash: {}", hashResult);
            return hashResult;

        } catch (Exception e) {
            log.error("Error generating HMAC SHA512", e);
            return null;
        }
    }
}
