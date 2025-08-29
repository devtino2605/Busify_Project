package com.busify.project.bus_operator.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when bus operator creation fails
 * <p>
 * This exception is used when a bus operator cannot be created due to
 * validation errors, business rule violations, or system constraints.
 * </p>
 */
public class BusOperatorCreationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusOperatorCreationException with default error code
     */
    public BusOperatorCreationException() {
        super(ErrorCode.OPERATOR_CREATION_FAILED);
    }

    /**
     * Creates a new BusOperatorCreationException with custom message
     * 
     * @param message Custom error message
     */
    public BusOperatorCreationException(String message) {
        super(ErrorCode.OPERATOR_CREATION_FAILED, message);
    }

    /**
     * Creates a new BusOperatorCreationException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusOperatorCreationException(String message, Throwable cause) {
        super(ErrorCode.OPERATOR_CREATION_FAILED, message, cause);
    }

    /**
     * Creates a new BusOperatorCreationException with specific error code
     * 
     * @param errorCode Specific error code for the creation failure
     * @param message   Custom error message
     */
    public BusOperatorCreationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BusOperatorCreationException for duplicate operator
     * 
     * @param email The email that already exists
     * @return BusOperatorCreationException instance
     */
    public static BusOperatorCreationException duplicateEmail(String email) {
        return new BusOperatorCreationException(ErrorCode.OPERATOR_ALREADY_EXISTS,
                "Bus operator with email " + email + " already exists");
    }

    /**
     * Creates a new BusOperatorCreationException for validation errors
     * 
     * @param field The field that failed validation
     * @param value The invalid value
     * @return BusOperatorCreationException instance
     */
    public static BusOperatorCreationException validationFailed(String field, String value) {
        return new BusOperatorCreationException("Validation failed for field " + field + " with value: " + value);
    }

    /**
     * Creates a new BusOperatorCreationException for owner not found
     * 
     * @param email The owner email that was not found
     * @return BusOperatorCreationException instance
     */
    public static BusOperatorCreationException ownerNotFound(String email) {
        return new BusOperatorCreationException(ErrorCode.OPERATOR_OWNER_NOT_FOUND,
                "Owner not found with email: " + email);
    }

    /**
     * Creates a new BusOperatorCreationException for default role not found
     * 
     * @return BusOperatorCreationException instance
     */
    public static BusOperatorCreationException defaultRoleNotFound() {
        return new BusOperatorCreationException(ErrorCode.OPERATOR_DEFAULT_ROLE_NOT_FOUND,
                "Default BUS_OPERATOR role not found");
    }
}
