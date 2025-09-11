package com.busify.project.refund.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

public class RefundNotFoundException extends AppException {

    public RefundNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RefundNotFoundException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public static RefundNotFoundException notFound() {
        return new RefundNotFoundException(ErrorCode.REFUND_NOT_FOUND);
    }

    public static RefundNotFoundException notFound(Long refundId) {
        return new RefundNotFoundException(ErrorCode.REFUND_NOT_FOUND);
    }
}
