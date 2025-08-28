package com.busify.project.bus_operator.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when bus operator deletion fails
 * <p>
 * This exception is used when a bus operator cannot be deleted due to
 * business constraints like active buses or ongoing operations.
 * </p>
 */
public class BusOperatorDeleteException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusOperatorDeleteException with default error code
     */
    public BusOperatorDeleteException() {
        super(ErrorCode.OPERATOR_DELETE_FAILED);
    }

    /**
     * Creates a new BusOperatorDeleteException with custom message
     * 
     * @param message Custom error message
     */
    public BusOperatorDeleteException(String message) {
        super(ErrorCode.OPERATOR_DELETE_FAILED, message);
    }

    /**
     * Creates a new BusOperatorDeleteException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusOperatorDeleteException(String message, Throwable cause) {
        super(ErrorCode.OPERATOR_DELETE_FAILED, message, cause);
    }

    /**
     * Creates a new BusOperatorDeleteException with specific error code
     * 
     * @param errorCode Specific error code for the deletion failure
     * @param message   Custom error message
     */
    public BusOperatorDeleteException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BusOperatorDeleteException for operator not found
     * 
     * @param operatorId The ID of the operator that was not found
     * @return BusOperatorDeleteException instance
     */
    public static BusOperatorDeleteException operatorNotFound(Long operatorId) {
        return new BusOperatorDeleteException(ErrorCode.OPERATOR_NOT_FOUND,
                "Bus operator with ID " + operatorId + " not found");
    }

    /**
     * Creates a new BusOperatorDeleteException for operators with active buses
     * 
     * @param operatorId       The ID of the operator
     * @param activeBusesCount Number of active buses
     * @return BusOperatorDeleteException instance
     */
    public static BusOperatorDeleteException hasActiveBuses(Long operatorId, int activeBusesCount) {
        return new BusOperatorDeleteException("Cannot delete bus operator " + operatorId +
                " because it has " + activeBusesCount + " active bus(es)");
    }

    /**
     * Creates a new BusOperatorDeleteException for unauthorized access
     * 
     * @param operatorId The ID of the operator
     * @param userId     The ID of the unauthorized user
     * @return BusOperatorDeleteException instance
     */
    public static BusOperatorDeleteException unauthorized(Long operatorId, Long userId) {
        return new BusOperatorDeleteException(ErrorCode.OPERATOR_UNAUTHORIZED,
                "User " + userId + " is not authorized to delete bus operator " + operatorId);
    }

    /**
     * Creates a new BusOperatorDeleteException for operators with ongoing trips
     * 
     * @param operatorId       The ID of the operator
     * @param activeTripsCount Number of active trips
     * @return BusOperatorDeleteException instance
     */
    public static BusOperatorDeleteException hasActiveTrips(Long operatorId, int activeTripsCount) {
        return new BusOperatorDeleteException("Cannot delete bus operator " + operatorId +
                " because it has " + activeTripsCount + " active trip(s)");
    }
}
