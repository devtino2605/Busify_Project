package com.busify.project.payment.util;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VNPayUtilTest {
    @Test
    void testCreatePaymentUrl() throws UnsupportedEncodingException {
        String vnpUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        String merchantCode = "ZSO520R7";
        String secretKey = "D32I52PQHVSEFET6SG9WDJNGZZM4QSYI";
        String returnUrl = "http://localhost:8080/api/payments/vnpay/callback";
        String orderId = "TXN123456";
        String amount = "50000000";
        String orderInfo = "Test payment";

        String url = VNPayUtil.createPaymentUrl(vnpUrl, merchantCode, secretKey, returnUrl, orderId, amount, orderInfo,
                null);
        assertNotNull(url);
        assertTrue(url.contains("vnp_SecureHash="));
    }

    @Test
    void testVerifyCallback() {
        String secretKey = "D32I52PQHVSEFET6SG9WDJNGZZM4QSYI";
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Amount", "50000000");
        params.put("vnp_Command", "pay");
        params.put("vnp_CreateDate", "20250804220107");
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_ExpireDate", "20250804221607");
        params.put("vnp_IpAddr", "127.0.0.1");
        params.put("vnp_Locale", "vn");
        params.put("vnp_OrderInfo", "Test payment");
        params.put("vnp_OrderType", "other");
        params.put("vnp_ReturnUrl", "http://localhost:8080/api/payments/vnpay/callback");
        params.put("vnp_TmnCode", "ZSO520R7");
        params.put("vnp_TxnRef", "TXN123456");
        params.put("vnp_Version", "2.1.0");
        // Tạo hashData giống logic thực tế
        StringBuilder hashData = new StringBuilder();
        params.keySet().stream().sorted().forEach(key -> {
            hashData.append(key).append("=").append(params.get(key)).append("&");
        });
        hashData.setLength(hashData.length() - 1); // remove last &
        String secureHash = VNPayUtilTestHelper.hmacSHA512(secretKey, hashData.toString());
        params.put("vnp_SecureHash", secureHash);
        assertTrue(VNPayUtil.verifyCallback(params, secretKey));
    }
}

// Helper để test hmacSHA512
class VNPayUtilTestHelper {
    public static String hmacSHA512(String key, String data) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(),
                    "HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC SHA512", e);
        }
    }
}
