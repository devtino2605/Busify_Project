package com.busify.project.payment.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when payment processing fails
 */
public class PaymentProcessingException extends AppException {

    public PaymentProcessingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PaymentProcessingException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * General payment processing failed
     */
    public static PaymentProcessingException processingFailed() {
        return new PaymentProcessingException(ErrorCode.PAYMENT_FAILED);
    }

    /**
     * Payment processing failed with cause
     */
    public static PaymentProcessingException processingFailed(Throwable cause) {
        return new PaymentProcessingException(ErrorCode.PAYMENT_FAILED, cause);
    }

    /**
     * Payment creation failed
     */
    public static PaymentProcessingException creationFailed() {
        return new PaymentProcessingException(ErrorCode.PAYMENT_CREATION_FAILED);
    }

    /**
     * Payment creation failed with cause
     */
    public static PaymentProcessingException creationFailed(Throwable cause) {
        return new PaymentProcessingException(ErrorCode.PAYMENT_CREATION_FAILED, cause);
    }
}
