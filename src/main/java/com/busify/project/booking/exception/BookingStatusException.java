package com.busify.project.booking.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when there are invalid booking status transitions
 * <p>
 * This exception is used when attempting to change a booking status
 * to an invalid state based on business rules.
 * </p>
 */
public class BookingStatusException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BookingStatusException with default error code
     */
    public BookingStatusException() {
        super(ErrorCode.INVALID_BOOKING_STATUS);
    }

    /**
     * Creates a new BookingStatusException with custom message
     * 
     * @param message Custom error message
     */
    public BookingStatusException(String message) {
        super(ErrorCode.INVALID_BOOKING_STATUS, message);
    }

    /**
     * Creates a new BookingStatusException for invalid status transition
     * 
     * @param currentStatus Current booking status
     * @param newStatus     Attempted new status
     */
    public BookingStatusException(String currentStatus, String newStatus) {
        super(ErrorCode.INVALID_BOOKING_STATUS,
                "Cannot change booking status from " + currentStatus + " to " + newStatus);
    }

    /**
     * Creates a new BookingStatusException for specific booking
     * 
     * @param bookingId     The ID of the booking
     * @param currentStatus Current booking status
     * @param newStatus     Attempted new status
     */
    public BookingStatusException(Long bookingId, String currentStatus, String newStatus) {
        super(ErrorCode.INVALID_BOOKING_STATUS,
                "Booking " + bookingId + ": Cannot change status from " + currentStatus + " to " + newStatus);
    }

    /**
     * Creates a new BookingStatusException for status update with reason
     * 
     * @param bookingId The ID of the booking
     * @param reason    The specific reason for the invalid transition
     */
    public BookingStatusException(Long bookingId, String reason) {
        super(ErrorCode.INVALID_BOOKING_STATUS,
                "Invalid status update for booking " + bookingId + ": " + reason);
    }

    /**
     * Creates a new BookingStatusException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BookingStatusException(String message, Throwable cause) {
        super(ErrorCode.INVALID_BOOKING_STATUS, message, cause);
    }
}
