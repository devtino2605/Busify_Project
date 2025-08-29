package com.busify.project.payment.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when there are issues with payment method operations
 */
public class PaymentMethodException extends AppException {

    public PaymentMethodException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Payment method not supported
     */
    public static PaymentMethodException methodNotSupported() {
        return new PaymentMethodException(ErrorCode.PAYMENT_METHOD_NOT_SUPPORTED);
    }

    /**
     * Credit card payment not implemented
     */
    public static PaymentMethodException creditCardNotImplemented() {
        return new PaymentMethodException(ErrorCode.CREDIT_CARD_NOT_IMPLEMENTED);
    }
}
