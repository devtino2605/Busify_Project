package com.busify.project.booking.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a booking is not found in the system
 * <p>
 * This exception is used when attempting to retrieve, update, or delete
 * a booking that does not exist in the database.
 * </p>
 */
public class BookingNotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BookingNotFoundException with default error code
     * 
     * @param bookingId The ID of the booking that was not found
     */
    public BookingNotFoundException(Long bookingId) {
        super(ErrorCode.BOOKING_NOT_FOUND, "Booking with ID " + bookingId + " not found");
    }

    /**
     * Creates a new BookingNotFoundException with custom message
     * 
     * @param message Custom error message
     */
    public BookingNotFoundException(String message) {
        super(ErrorCode.BOOKING_NOT_FOUND, message);
    }

    /**
     * Creates a new BookingNotFoundException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BookingNotFoundException(String message, Throwable cause) {
        super(ErrorCode.BOOKING_NOT_FOUND, message, cause);
    }
}
