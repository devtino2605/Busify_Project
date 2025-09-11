package com.busify.project.refund.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

public class RefundProcessingException extends AppException {

    public RefundProcessingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RefundProcessingException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public static RefundProcessingException processingFailed() {
        return new RefundProcessingException(ErrorCode.REFUND_PROCESSING_FAILED);
    }

    public static RefundProcessingException processingFailed(Throwable cause) {
        return new RefundProcessingException(ErrorCode.REFUND_PROCESSING_FAILED, cause);
    }

    public static RefundProcessingException creationFailed() {
        return new RefundProcessingException(ErrorCode.REFUND_CREATION_FAILED);
    }

    public static RefundProcessingException creationFailed(Throwable cause) {
        return new RefundProcessingException(ErrorCode.REFUND_CREATION_FAILED, cause);
    }

    public static RefundProcessingException notAllowed() {
        return new RefundProcessingException(ErrorCode.REFUND_NOT_ALLOWED);
    }

    public static RefundProcessingException amountExceedsPayment() {
        return new RefundProcessingException(ErrorCode.REFUND_AMOUNT_EXCEEDS_PAYMENT);
    }

    public static RefundProcessingException paymentNotEligible() {
        return new RefundProcessingException(ErrorCode.PAYMENT_NOT_ELIGIBLE_FOR_REFUND);
    }

    public static RefundProcessingException alreadyProcessed() {
        return new RefundProcessingException(ErrorCode.REFUND_ALREADY_PROCESSED);
    }

    public static RefundProcessingException policyViolation() {
        return new RefundProcessingException(ErrorCode.REFUND_POLICY_VIOLATION);
    }
}
