package com.busify.project.payment.enums;

public enum PaymentMethod {
    PAYPAL("PayPal"),
    VNPAY("VNPay"),
    CREDIT_CARD("Credit Card"),
    BANK_TRANSFER("Bank Transfer");
    // PAY_LATER("Pay Later");

    private final String method;

    PaymentMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
