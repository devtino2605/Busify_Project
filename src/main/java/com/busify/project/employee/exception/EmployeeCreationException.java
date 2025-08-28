package com.busify.project.employee.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when employee creation operations fail
 * <p>
 * This exception is used when employee creation operations fail due to
 * validation errors, duplicate data, or database constraints.
 * </p>
 */
public class EmployeeCreationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new EmployeeCreationException with default error code
     */
    public EmployeeCreationException() {
        super(ErrorCode.EMPLOYEE_CREATION_FAILED);
    }

    /**
     * Creates a new EmployeeCreationException with custom message
     * 
     * @param message Custom error message
     */
    public EmployeeCreationException(String message) {
        super(ErrorCode.EMPLOYEE_CREATION_FAILED, message);
    }

    /**
     * Creates a new EmployeeCreationException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public EmployeeCreationException(String message, Throwable cause) {
        super(ErrorCode.EMPLOYEE_CREATION_FAILED, message, cause);
    }

    /**
     * Creates a new EmployeeCreationException with specific error code
     * 
     * @param errorCode Specific error code for the creation failure
     * @param message   Custom error message
     */
    public EmployeeCreationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new EmployeeCreationException with specific error code and cause
     * 
     * @param errorCode Specific error code for the creation failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public EmployeeCreationException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new EmployeeCreationException for duplicate email
     * 
     * @param email The duplicate email address
     * @return EmployeeCreationException instance
     */
    public static EmployeeCreationException duplicateEmail(String email) {
        return new EmployeeCreationException(ErrorCode.EMPLOYEE_EMAIL_EXISTS,
                "Cannot create employee - email " + email + " already exists");
    }

    /**
     * Creates a new EmployeeCreationException for role not found
     * 
     * @param roleName The role name that was not found
     * @return EmployeeCreationException instance
     */
    public static EmployeeCreationException roleNotFound(String roleName) {
        return new EmployeeCreationException(ErrorCode.EMPLOYEE_ROLE_NOT_FOUND,
                "Cannot create employee - role " + roleName + " not found");
    }

    /**
     * Creates a new EmployeeCreationException for bus operator not found
     * 
     * @param operatorId The ID of the bus operator that was not found
     * @return EmployeeCreationException instance
     */
    public static EmployeeCreationException busOperatorNotFound(Long operatorId) {
        return new EmployeeCreationException(ErrorCode.EMPLOYEE_BUS_OPERATOR_NOT_FOUND,
                "Cannot create employee - bus operator with ID " + operatorId + " not found");
    }

    /**
     * Creates a new EmployeeCreationException for validation failure
     * 
     * @param field The field that failed validation
     * @param value The invalid value
     * @return EmployeeCreationException instance
     */
    public static EmployeeCreationException validationFailed(String field, String value) {
        return new EmployeeCreationException("Validation failed for " + field + ": " + value);
    }

    /**
     * Creates a new EmployeeCreationException for invalid status
     * 
     * @param status The invalid status
     * @return EmployeeCreationException instance
     */
    public static EmployeeCreationException invalidStatus(String status) {
        return new EmployeeCreationException(ErrorCode.EMPLOYEE_INVALID_STATUS,
                "Invalid employee status: " + status);
    }

    /**
     * Creates a new EmployeeCreationException for database failure
     * 
     * @param email The email of the employee being created
     * @param cause The underlying cause of the failure
     * @return EmployeeCreationException instance
     */
    public static EmployeeCreationException databaseFailure(String email, Throwable cause) {
        return new EmployeeCreationException("Failed to create employee " + email + " due to database error", cause);
    }

    /**
     * Creates a new EmployeeCreationException for password encoding failure
     * 
     * @param email The email of the employee being created
     * @param cause The underlying cause of the failure
     * @return EmployeeCreationException instance
     */
    public static EmployeeCreationException passwordEncodingFailed(String email, Throwable cause) {
        return new EmployeeCreationException("Failed to encode password for employee " + email, cause);
    }

    /**
     * Creates a new EmployeeCreationException for missing required data
     * 
     * @param missingField The field that is missing
     * @return EmployeeCreationException instance
     */
    public static EmployeeCreationException missingRequiredData(String missingField) {
        return new EmployeeCreationException("Missing required data: " + missingField);
    }

    /**
     * Creates a new EmployeeCreationException for unauthorized creation
     * 
     * @param userId The ID of the unauthorized user
     * @return EmployeeCreationException instance
     */
    public static EmployeeCreationException unauthorized(Long userId) {
        return new EmployeeCreationException(ErrorCode.EMPLOYEE_UNAUTHORIZED_ACCESS,
                "User " + userId + " is not authorized to create employees");
    }
}
