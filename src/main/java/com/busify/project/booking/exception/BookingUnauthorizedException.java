package com.busify.project.booking.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a user attempts to access a booking without proper
 * authorization
 * <p>
 * This exception is used when a user tries to access, modify, or cancel
 * a booking that doesn't belong to them or they don't have permission to
 * access.
 * </p>
 */
public class BookingUnauthorizedException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BookingUnauthorizedException with default error code
     */
    public BookingUnauthorizedException() {
        super(ErrorCode.ACCESS_DENIED, "You are not authorized to access this booking");
    }

    /**
     * Creates a new BookingUnauthorizedException with booking ID
     * 
     * @param bookingId The ID of the booking that user tried to access
     */
    public BookingUnauthorizedException(Long bookingId) {
        super(ErrorCode.ACCESS_DENIED, "You are not authorized to access booking with ID " + bookingId);
    }

    /**
     * Creates a new BookingUnauthorizedException with custom message
     * 
     * @param message Custom error message
     */
    public BookingUnauthorizedException(String message) {
        super(ErrorCode.ACCESS_DENIED, message);
    }

    /**
     * Creates a new BookingUnauthorizedException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BookingUnauthorizedException(String message, Throwable cause) {
        super(ErrorCode.ACCESS_DENIED, message, cause);
    }
}
