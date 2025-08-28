package com.busify.project.booking.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when attempting to book seats that are not available
 * <p>
 * This exception is used when seats are already booked, trip is full,
 * or the selected seats are invalid.
 * </p>
 */
public class BookingSeatUnavailableException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BookingSeatUnavailableException with default message
     */
    public BookingSeatUnavailableException() {
        super(ErrorCode.SEAT_ALREADY_BOOKED, "Selected seats are not available");
    }

    /**
     * Creates a new BookingSeatUnavailableException with seat numbers
     * 
     * @param seatNumbers The seat numbers that are unavailable
     */
    public BookingSeatUnavailableException(String seatNumbers) {
        super(ErrorCode.SEAT_ALREADY_BOOKED, "Seats " + seatNumbers + " are not available");
    }

    /**
     * Creates a new BookingSeatUnavailableException for trip full scenario
     * 
     * @param tripId The ID of the trip that is full
     */
    public BookingSeatUnavailableException(Long tripId) {
        super(ErrorCode.TRIP_FULL, "Trip with ID " + tripId + " is full - no available seats");
    }

    /**
     * Creates a new BookingSeatUnavailableException with custom message
     * 
     * @param errorCode Specific error code for the seat unavailability
     * @param message   Custom error message
     */
    public BookingSeatUnavailableException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BookingSeatUnavailableException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BookingSeatUnavailableException(String message, Throwable cause) {
        super(ErrorCode.SEAT_ALREADY_BOOKED, message, cause);
    }
}
