package com.busify.project.booking.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when booking cancellation fails
 * <p>
 * This exception is used when a booking cannot be cancelled due to
 * business rules, timing constraints, or other restrictions.
 * </p>
 */
public class BookingCancellationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BookingCancellationException with default error code
     */
    public BookingCancellationException() {
        super(ErrorCode.BOOKING_CANCELLATION_FAILED);
    }

    /**
     * Creates a new BookingCancellationException with custom message
     * 
     * @param message Custom error message
     */
    public BookingCancellationException(String message) {
        super(ErrorCode.BOOKING_CANCELLATION_FAILED, message);
    }

    /**
     * Creates a new BookingCancellationException for specific booking
     * 
     * @param bookingId The ID of the booking that cannot be cancelled
     * @param reason    The reason why cancellation failed
     */
    public BookingCancellationException(Long bookingId, String reason) {
        super(ErrorCode.BOOKING_CANCELLATION_FAILED,
                "Cannot cancel booking " + bookingId + ": " + reason);
    }

    /**
     * Creates a new BookingCancellationException for time restriction
     * 
     * @param bookingId       The ID of the booking
     * @param hoursBeforeTrip Hours remaining before trip
     */
    public static BookingCancellationException tooLateToCancel(Long bookingId, int hoursBeforeTrip) {
        return new BookingCancellationException(bookingId,
                "Cannot cancel within " + hoursBeforeTrip + " hours of departure");
    }

    /**
     * Creates a new BookingCancellationException for already cancelled booking
     * 
     * @param bookingId The ID of the booking
     */
    public static BookingCancellationException alreadyCancelled(Long bookingId) {
        return new BookingCancellationException(bookingId, "Booking is already cancelled");
    }

    /**
     * Creates a new BookingCancellationException for completed trip
     * 
     * @param bookingId The ID of the booking
     */
    public static BookingCancellationException tripCompleted(Long bookingId) {
        return new BookingCancellationException(bookingId, "Cannot cancel completed trip");
    }

    /**
     * Creates a new BookingCancellationException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BookingCancellationException(String message, Throwable cause) {
        super(ErrorCode.BOOKING_CANCELLATION_FAILED, message, cause);
    }
}
