package com.busify.project.payment.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a payment is not found in the system
 */
public class PaymentNotFoundException extends AppException {

    public PaymentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Payment not found by ID
     */
    public static PaymentNotFoundException notFound() {
        return new PaymentNotFoundException(ErrorCode.PAYMENT_NOT_FOUND);
    }

    /**
     * Payment transaction not found
     */
    public static PaymentNotFoundException transactionNotFound() {
        return new PaymentNotFoundException(ErrorCode.PAYMENT_TRANSACTION_NOT_FOUND);
    }
}
