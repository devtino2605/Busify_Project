package com.busify.project.booking.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when booking operations require authentication
 * <p>
 * This exception is used when a user must be logged in to perform
 * booking operations but their authentication is invalid or missing.
 * </p>
 */
public class BookingAuthenticationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BookingAuthenticationException with default message
     */
    public BookingAuthenticationException() {
        super(ErrorCode.AUTHENTICATION_FAILED, "Authentication required for booking operations");
    }

    /**
     * Creates a new BookingAuthenticationException with custom message
     * 
     * @param message Custom error message
     */
    public BookingAuthenticationException(String message) {
        super(ErrorCode.AUTHENTICATION_FAILED, message);
    }

    /**
     * Creates a new BookingAuthenticationException for token issues
     * 
     * @param tokenError Specific token-related error message
     */
    public BookingAuthenticationException(String tokenError, boolean isTokenIssue) {
        super(isTokenIssue ? ErrorCode.TOKEN_INVALID : ErrorCode.AUTHENTICATION_FAILED, tokenError);
    }

    /**
     * Creates a new BookingAuthenticationException for session expiry
     */
    public static BookingAuthenticationException sessionExpired() {
        return new BookingAuthenticationException(ErrorCode.SESSION_EXPIRED,
                "Session expired, please login again to continue booking");
    }

    /**
     * Creates a new BookingAuthenticationException with specific error code
     * 
     * @param errorCode Specific authentication error code
     * @param message   Custom error message
     */
    public BookingAuthenticationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BookingAuthenticationException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BookingAuthenticationException(String message, Throwable cause) {
        super(ErrorCode.AUTHENTICATION_FAILED, message, cause);
    }
}
