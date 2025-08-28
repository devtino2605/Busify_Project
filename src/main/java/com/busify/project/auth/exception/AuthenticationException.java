package com.busify.project.auth.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when authentication operations fail
 */
public class AuthenticationException extends AppException {

    public AuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Authentication failed with invalid credentials
     */
    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException(ErrorCode.AUTHENTICATION_FAILED);
    }

    /**
     * Email not verified
     */
    public static AuthenticationException emailNotVerified() {
        return new AuthenticationException(ErrorCode.EMAIL_NOT_VERIFIED);
    }

    /**
     * Invalid password reset token
     */
    public static AuthenticationException invalidPasswordResetToken() {
        return new AuthenticationException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN);
    }
}
