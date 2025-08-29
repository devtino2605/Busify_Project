package com.busify.project.booking.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when booking creation fails
 * <p>
 * This exception is used when a new booking cannot be created due to
 * validation errors, business rule violations, or system constraints.
 * </p>
 */
public class BookingCreationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BookingCreationException with default error code
     */
    public BookingCreationException() {
        super(ErrorCode.BOOKING_CREATION_FAILED);
    }

    /**
     * Creates a new BookingCreationException with custom message
     * 
     * @param message Custom error message
     */
    public BookingCreationException(String message) {
        super(ErrorCode.BOOKING_CREATION_FAILED, message);
    }

    /**
     * Creates a new BookingCreationException for trip-related issues
     * 
     * @param tripId The ID of the trip
     * @param reason The reason for creation failure
     */
    public BookingCreationException(Long tripId, String reason) {
        super(ErrorCode.BOOKING_CREATION_FAILED,
                "Cannot create booking for trip " + tripId + ": " + reason);
    }

    /**
     * Creates a new BookingCreationException for invalid passenger data
     * 
     * @param field The invalid field name
     * @param value The invalid value
     */
    public static BookingCreationException invalidPassengerData(String field, String value) {
        return new BookingCreationException("Invalid passenger " + field + ": " + value);
    }

    /**
     * Creates a new BookingCreationException for seat selection issues
     * 
     * @param seatNumbers The problematic seat numbers
     */
    public static BookingCreationException invalidSeatSelection(String seatNumbers) {
        return new BookingCreationException("Invalid seat selection: " + seatNumbers);
    }

    /**
     * Creates a new BookingCreationException for trip capacity issues
     * 
     * @param tripId         The ID of the trip
     * @param requestedSeats Number of requested seats
     * @param availableSeats Number of available seats
     */
    public static BookingCreationException insufficientCapacity(Long tripId, int requestedSeats, int availableSeats) {
        return new BookingCreationException(tripId,
                "Requested " + requestedSeats + " seats but only " + availableSeats + " available");
    }

    /**
     * Creates a new BookingCreationException for booking time restrictions
     * 
     * @param tripId        The ID of the trip
     * @param departureTime The trip departure time
     */
    public static BookingCreationException bookingDeadlinePassed(Long tripId, String departureTime) {
        return new BookingCreationException(tripId,
                "Booking deadline passed for trip departing at " + departureTime);
    }

    /**
     * Creates a new BookingCreationException with specific error code
     * 
     * @param errorCode Specific error code for the creation failure
     * @param message   Custom error message
     */
    public BookingCreationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BookingCreationException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BookingCreationException(String message, Throwable cause) {
        super(ErrorCode.BOOKING_CREATION_FAILED, message, cause);
    }
}
