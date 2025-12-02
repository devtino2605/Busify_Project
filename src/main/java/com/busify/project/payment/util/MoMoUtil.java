package com.busify.project.payment.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * MoMoUtil
 * 
 * Utility class for MoMo payment signature generation and verification
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-12-01
 */
@Slf4j
public class MoMoUtil {

    private static final String HMAC_SHA256 = "HmacSHA256";

    /**
     * Generate HMAC SHA256 signature for MoMo payment request
     * 
     * Signature format for CREATE request:
     * accessKey=$accessKey&amount=$amount&extraData=$extraData&ipnUrl=$ipnUrl
     * &orderId=$orderId&orderInfo=$orderInfo&partnerCode=$partnerCode
     * &redirectUrl=$redirectUrl&requestId=$requestId&requestType=$requestType
     * 
     * @param accessKey   MoMo access key
     * @param amount      Payment amount
     * @param extraData   Extra data (JSON string or empty)
     * @param ipnUrl      IPN callback URL
     * @param orderId     Order ID
     * @param orderInfo   Order information
     * @param partnerCode Partner code
     * @param redirectUrl Redirect URL after payment
     * @param requestId   Request ID (unique)
     * @param requestType Request type (captureWallet/payWithATM)
     * @param secretKey   Secret key for signing
     * @return HMAC SHA256 signature
     */
    public static String generateSignature(
            String accessKey,
            Long amount,
            String extraData,
            String ipnUrl,
            String orderId,
            String orderInfo,
            String partnerCode,
            String redirectUrl,
            String requestId,
            String requestType,
            String secretKey) {

        try {
            // Build raw signature string (alphabetically sorted)
            String rawSignature = "accessKey=" + accessKey +
                    "&amount=" + amount +
                    "&extraData=" + extraData +
                    "&ipnUrl=" + ipnUrl +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + partnerCode +
                    "&redirectUrl=" + redirectUrl +
                    "&requestId=" + requestId +
                    "&requestType=" + requestType;

            log.debug("MoMo raw signature: {}", rawSignature);

            // Generate HMAC SHA256
            return hmacSHA256(rawSignature, secretKey);

        } catch (Exception e) {
            log.error("Error generating MoMo signature: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate MoMo signature", e);
        }
    }

    /**
     * Generate signature for MoMo IPN/redirect response verification
     * 
     * Signature format for RESPONSE:
     * accessKey=$accessKey&amount=$amount&extraData=$extraData&message=$message
     * &orderId=$orderId&orderInfo=$orderInfo&orderType=$orderType
     * &partnerCode=$partnerCode&payType=$payType&requestId=$requestId
     * &responseTime=$responseTime&resultCode=$resultCode&transId=$transId
     * 
     * @param params    Response parameters from MoMo
     * @param secretKey Secret key for verification
     * @return HMAC SHA256 signature
     */
    public static String generateResponseSignature(Map<String, String> params, String secretKey) {
        try {
            // Sort parameters alphabetically and build raw string
            TreeMap<String, String> sortedParams = new TreeMap<>(params);

            StringBuilder rawSignature = new StringBuilder();
            rawSignature.append("accessKey=").append(sortedParams.get("accessKey"));
            rawSignature.append("&amount=").append(sortedParams.get("amount"));
            rawSignature.append("&extraData=").append(sortedParams.getOrDefault("extraData", ""));
            rawSignature.append("&message=").append(sortedParams.get("message"));
            rawSignature.append("&orderId=").append(sortedParams.get("orderId"));
            rawSignature.append("&orderInfo=").append(sortedParams.get("orderInfo"));
            rawSignature.append("&orderType=").append(sortedParams.get("orderType"));
            rawSignature.append("&partnerCode=").append(sortedParams.get("partnerCode"));
            rawSignature.append("&payType=").append(sortedParams.get("payType"));
            rawSignature.append("&requestId=").append(sortedParams.get("requestId"));
            rawSignature.append("&responseTime=").append(sortedParams.get("responseTime"));
            rawSignature.append("&resultCode=").append(sortedParams.get("resultCode"));
            rawSignature.append("&transId=").append(sortedParams.get("transId"));

            log.debug("MoMo response raw signature: {}", rawSignature);

            return hmacSHA256(rawSignature.toString(), secretKey);

        } catch (Exception e) {
            log.error("Error generating MoMo response signature: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate MoMo response signature", e);
        }
    }

    /**
     * Verify MoMo callback signature
     * 
     * @param params            Response parameters from MoMo
     * @param receivedSignature Signature received from MoMo
     * @param secretKey         Secret key for verification
     * @return true if signature is valid, false otherwise
     */
    public static boolean verifySignature(Map<String, String> params, String receivedSignature, String secretKey) {
        try {
            String calculatedSignature = generateResponseSignature(params, secretKey);
            boolean isValid = calculatedSignature.equals(receivedSignature);

            if (!isValid) {
                log.warn("MoMo signature verification failed! Calculated: {}, Received: {}",
                        calculatedSignature, receivedSignature);
            }

            return isValid;
        } catch (Exception e) {
            log.error("Error verifying MoMo signature: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Generate HMAC SHA256 hash
     * 
     * @param data      Data to hash
     * @param secretKey Secret key
     * @return Hex string of HMAC SHA256 hash
     */
    private static String hmacSHA256(String data, String secretKey) throws Exception {
        Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        sha256_HMAC.init(secret_key);

        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Convert to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Generate unique request ID
     * Format: timestamp + random 6 digits
     * 
     * @return Unique request ID
     */
    public static String generateRequestId() {
        return System.currentTimeMillis() + String.format("%06d", (int) (Math.random() * 1000000));
    }

    /**
     * Generate order ID from transaction code
     * MoMo orderId must be unique and alphanumeric
     * 
     * @param transactionCode Transaction code from system
     * @return MoMo-compatible order ID
     */
    public static String generateOrderId(String transactionCode) {
        // Remove special characters and keep only alphanumeric
        return transactionCode.replaceAll("[^a-zA-Z0-9]", "");
    }
}
