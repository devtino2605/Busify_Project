package com.busify.project.payment.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when PayPal payment operations fail
 */
public class PayPalException extends AppException {

    public PayPalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PayPalException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * PayPal payment processing failed
     */
    public static PayPalException paymentFailed() {
        return new PayPalException(ErrorCode.PAYPAL_PAYMENT_FAILED);
    }

    /**
     * PayPal payment processing failed with cause
     */
    public static PayPalException paymentFailed(Throwable cause) {
        return new PayPalException(ErrorCode.PAYPAL_PAYMENT_FAILED, cause);
    }

    /**
     * PayPal approval URL not found
     */
    public static PayPalException approvalUrlNotFound() {
        return new PayPalException(ErrorCode.PAYPAL_APPROVAL_URL_NOT_FOUND);
    }
}
