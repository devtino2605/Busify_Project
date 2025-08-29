package com.busify.project.auth.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when password reset operations fail
 */
public class PasswordResetException extends AppException {

    public PasswordResetException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Password reset not available for this user type
     */
    public static PasswordResetException notAvailable() {
        return new PasswordResetException(ErrorCode.PASSWORD_RESET_NOT_AVAILABLE);
    }
}
