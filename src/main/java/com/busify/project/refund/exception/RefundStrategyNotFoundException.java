package com.busify.project.refund.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

public class RefundStrategyNotFoundException extends AppException {

    public RefundStrategyNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RefundStrategyNotFoundException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public static RefundStrategyNotFoundException strategyNotFound(String paymentMethod) {
        return new RefundStrategyNotFoundException(ErrorCode.REFUND_STRATEGY_NOT_FOUND);
    }
}
