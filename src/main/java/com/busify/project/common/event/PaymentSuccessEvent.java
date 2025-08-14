package com.busify.project.common.event;

import com.busify.project.payment.entity.Payment;

public class PaymentSuccessEvent extends BusifyEvent {

    private Payment payment;

    public PaymentSuccessEvent(Object source, String message, Payment payment) {
        super(source, message);
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }
}
