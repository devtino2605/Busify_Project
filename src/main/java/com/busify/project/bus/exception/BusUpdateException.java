package com.busify.project.bus.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when bus update fails
 * <p>
 * This exception is used when a bus cannot be updated due to
 * validation errors, business rule violations, or system constraints.
 * </p>
 */
public class BusUpdateException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusUpdateException with default error code
     */
    public BusUpdateException() {
        super(ErrorCode.BUS_UPDATE_FAILED);
    }

    /**
     * Creates a new BusUpdateException with custom message
     * 
     * @param message Custom error message
     */
    public BusUpdateException(String message) {
        super(ErrorCode.BUS_UPDATE_FAILED, message);
    }

    /**
     * Creates a new BusUpdateException for bus not found
     * 
     * @param busId The ID of the bus that was not found
     */
    public static BusUpdateException busNotFound(Long busId) {
        return new BusUpdateException(ErrorCode.BUS_NOT_FOUND,
                "Bus with ID " + busId + " not found");
    }

    /**
     * Creates a new BusUpdateException for invalid model
     * 
     * @param modelId The invalid model ID
     */
    public static BusUpdateException invalidModel(Long modelId) {
        return new BusUpdateException(ErrorCode.BUS_MODEL_NOT_FOUND,
                "Bus model with ID " + modelId + " not found");
    }

    /**
     * Creates a new BusUpdateException for invalid seat layout
     * 
     * @param layoutId The invalid layout ID
     */
    public static BusUpdateException invalidSeatLayout(Long layoutId) {
        return new BusUpdateException(ErrorCode.SEAT_LAYOUT_NOT_FOUND,
                "Seat layout with ID " + layoutId + " not found");
    }

    /**
     * Creates a new BusUpdateException for invalid status transition
     * 
     * @param currentStatus Current bus status
     * @param newStatus     Attempted new status
     */
    public static BusUpdateException invalidStatusTransition(String currentStatus, String newStatus) {
        return new BusUpdateException(ErrorCode.INVALID_BUS_STATUS_TRANSITION,
                "Cannot change bus status from " + currentStatus + " to " + newStatus);
    }

    /**
     * Creates a new BusUpdateException for unauthorized operator
     * 
     * @param busId      The ID of the bus
     * @param operatorId The ID of the unauthorized operator
     */
    public static BusUpdateException unauthorizedOperator(Long busId, Long operatorId) {
        return new BusUpdateException(ErrorCode.BUS_OPERATOR_UNAUTHORIZED,
                "Operator " + operatorId + " is not authorized to update bus " + busId);
    }

    /**
     * Creates a new BusUpdateException with specific error code
     * 
     * @param errorCode Specific error code for the update failure
     * @param message   Custom error message
     */
    public BusUpdateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BusUpdateException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusUpdateException(String message, Throwable cause) {
        super(ErrorCode.BUS_UPDATE_FAILED, message, cause);
    }
}
