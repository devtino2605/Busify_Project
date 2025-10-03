package com.busify.project.bus_operator.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a bus operator is not found in the system
 * <p>
 * This exception is used when attempting to retrieve, update, or delete
 * a bus operator that does not exist in the database.
 * </p>
 */
public class BusOperatorNotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusOperatorNotFoundException with default error code
     * 
     * @param operatorId The ID of the bus operator that was not found
     */
    public BusOperatorNotFoundException(Long operatorId) {
        super(ErrorCode.OPERATOR_NOT_FOUND, "Bus operator with ID " + operatorId + " not found");
    }

    /**
     * Creates a new BusOperatorNotFoundException with custom message
     * 
     * @param message Custom error message
     */
    public BusOperatorNotFoundException(String message) {
        super(ErrorCode.OPERATOR_NOT_FOUND, message);
    }

    /**
     * Creates a new BusOperatorNotFoundException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusOperatorNotFoundException(String message, Throwable cause) {
        super(ErrorCode.OPERATOR_NOT_FOUND, message, cause);
    }

    /**
     * Creates a new BusOperatorNotFoundException for operator ID
     * 
     * @param operatorId The ID of the bus operator that was not found
     * @return BusOperatorNotFoundException instance
     */
    public static BusOperatorNotFoundException withId(Long operatorId) {
        return new BusOperatorNotFoundException(operatorId);
    }

    /**
     * Creates a new BusOperatorNotFoundException for operator email
     * 
     * @param email The email of the bus operator that was not found
     * @return BusOperatorNotFoundException instance
     */
    public static BusOperatorNotFoundException withEmail(String email) {
        return new BusOperatorNotFoundException("Bus operator with email " + email + " not found");
    }
}
