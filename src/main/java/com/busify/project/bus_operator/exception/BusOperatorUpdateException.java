package com.busify.project.bus_operator.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when bus operator update fails
 * <p>
 * This exception is used when a bus operator cannot be updated due to
 * validation errors, business rule violations, or system constraints.
 * </p>
 */
public class BusOperatorUpdateException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusOperatorUpdateException with default error code
     */
    public BusOperatorUpdateException() {
        super(ErrorCode.OPERATOR_UPDATE_FAILED);
    }

    /**
     * Creates a new BusOperatorUpdateException with custom message
     * 
     * @param message Custom error message
     */
    public BusOperatorUpdateException(String message) {
        super(ErrorCode.OPERATOR_UPDATE_FAILED, message);
    }

    /**
     * Creates a new BusOperatorUpdateException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusOperatorUpdateException(String message, Throwable cause) {
        super(ErrorCode.OPERATOR_UPDATE_FAILED, message, cause);
    }

    /**
     * Creates a new BusOperatorUpdateException with specific error code
     * 
     * @param errorCode Specific error code for the update failure
     * @param message   Custom error message
     */
    public BusOperatorUpdateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BusOperatorUpdateException for operator not found
     * 
     * @param operatorId The ID of the operator that was not found
     * @return BusOperatorUpdateException instance
     */
    public static BusOperatorUpdateException operatorNotFound(Long operatorId) {
        return new BusOperatorUpdateException(ErrorCode.OPERATOR_NOT_FOUND,
                "Bus operator with ID " + operatorId + " not found");
    }

    /**
     * Creates a new BusOperatorUpdateException for owner not found
     * 
     * @param email The owner email that was not found
     * @return BusOperatorUpdateException instance
     */
    public static BusOperatorUpdateException ownerNotFound(String email) {
        return new BusOperatorUpdateException(ErrorCode.OPERATOR_OWNER_NOT_FOUND,
                "Owner not found with email: " + email);
    }

    /**
     * Creates a new BusOperatorUpdateException for validation errors
     * 
     * @param field The field that failed validation
     * @param value The invalid value
     * @return BusOperatorUpdateException instance
     */
    public static BusOperatorUpdateException validationFailed(String field, String value) {
        return new BusOperatorUpdateException("Validation failed for field " + field + " with value: " + value);
    }

    /**
     * Creates a new BusOperatorUpdateException for unauthorized access
     * 
     * @param operatorId The ID of the operator
     * @param userId     The ID of the unauthorized user
     * @return BusOperatorUpdateException instance
     */
    public static BusOperatorUpdateException unauthorized(Long operatorId, Long userId) {
        return new BusOperatorUpdateException(ErrorCode.OPERATOR_UNAUTHORIZED,
                "User " + userId + " is not authorized to update bus operator " + operatorId);
    }
}
