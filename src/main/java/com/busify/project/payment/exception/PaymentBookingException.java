package com.busify.project.payment.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when there are issues with booking-related payment
 * operations
 */
public class PaymentBookingException extends AppException {

    public PaymentBookingException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Booking not found for payment
     */
    public static PaymentBookingException bookingNotFound() {
        return new PaymentBookingException(ErrorCode.PAYMENT_BOOKING_NOT_FOUND);
    }

    /**
     * Booking already paid
     */
    public static PaymentBookingException bookingAlreadyPaid() {
        return new PaymentBookingException(ErrorCode.BOOKING_ALREADY_PAID);
    }
}
