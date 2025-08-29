package com.busify.project.bus.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when bus deletion fails
 * <p>
 * This exception is used when a bus cannot be deleted due to
 * business constraints like active trips or bookings.
 * </p>
 */
public class BusDeleteException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusDeleteException with default error code
     */
    public BusDeleteException() {
        super(ErrorCode.BUS_DELETE_FAILED_ACTIVE_TRIPS);
    }

    /**
     * Creates a new BusDeleteException with custom message
     * 
     * @param message Custom error message
     */
    public BusDeleteException(String message) {
        super(ErrorCode.BUS_DELETE_FAILED_ACTIVE_TRIPS, message);
    }

    /**
     * Creates a new BusDeleteException for bus not found
     * 
     * @param busId The ID of the bus that was not found
     */
    public static BusDeleteException busNotFound(Long busId) {
        return new BusDeleteException(ErrorCode.BUS_NOT_FOUND,
                "Bus with ID " + busId + " not found");
    }

    /**
     * Creates a new BusDeleteException for active trips
     * 
     * @param busId            The ID of the bus with active trips
     * @param activeTripsCount Number of active trips
     */
    public static BusDeleteException hasActiveTrips(Long busId, int activeTripsCount) {
        return new BusDeleteException("Cannot delete bus " + busId +
                " because it has " + activeTripsCount + " active trip(s)");
    }

    /**
     * Creates a new BusDeleteException for unauthorized operator
     * 
     * @param busId      The ID of the bus
     * @param operatorId The ID of the unauthorized operator
     */
    public static BusDeleteException unauthorizedOperator(Long busId, Long operatorId) {
        return new BusDeleteException(ErrorCode.BUS_OPERATOR_UNAUTHORIZED,
                "Operator " + operatorId + " is not authorized to delete bus " + busId);
    }

    /**
     * Creates a new BusDeleteException with specific error code
     * 
     * @param errorCode Specific error code for the deletion failure
     * @param message   Custom error message
     */
    public BusDeleteException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BusDeleteException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusDeleteException(String message, Throwable cause) {
        super(ErrorCode.BUS_DELETE_FAILED_ACTIVE_TRIPS, message, cause);
    }
}
