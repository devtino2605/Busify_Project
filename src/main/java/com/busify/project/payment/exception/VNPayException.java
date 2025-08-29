package com.busify.project.payment.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when VNPay payment operations fail
 */
public class VNPayException extends AppException {

    public VNPayException(ErrorCode errorCode) {
        super(errorCode);
    }

    public VNPayException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * VNPay payment processing failed
     */
    public static VNPayException paymentFailed() {
        return new VNPayException(ErrorCode.VNPAY_PAYMENT_FAILED);
    }

    /**
     * VNPay payment processing failed with cause
     */
    public static VNPayException paymentFailed(Throwable cause) {
        return new VNPayException(ErrorCode.VNPAY_PAYMENT_FAILED, cause);
    }

    /**
     * VNPay URL generation failed
     */
    public static VNPayException urlGenerationFailed() {
        return new VNPayException(ErrorCode.VNPAY_URL_GENERATION_FAILED);
    }

    /**
     * VNPay URL generation failed with cause
     */
    public static VNPayException urlGenerationFailed(Throwable cause) {
        return new VNPayException(ErrorCode.VNPAY_URL_GENERATION_FAILED, cause);
    }

    /**
     * VNPay hash generation failed
     */
    public static VNPayException hashGenerationFailed(Throwable cause) {
        return new VNPayException(ErrorCode.VNPAY_HASH_GENERATION_FAILED, cause);
    }
}
