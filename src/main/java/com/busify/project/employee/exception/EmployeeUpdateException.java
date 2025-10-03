package com.busify.project.employee.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when employee update operations fail
 * <p>
 * This exception is used when employee update operations fail due to
 * validation errors, business rule violations, or database constraints.
 * </p>
 */
public class EmployeeUpdateException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new EmployeeUpdateException with default error code
     */
    public EmployeeUpdateException() {
        super(ErrorCode.EMPLOYEE_UPDATE_FAILED);
    }

    /**
     * Creates a new EmployeeUpdateException with custom message
     * 
     * @param message Custom error message
     */
    public EmployeeUpdateException(String message) {
        super(ErrorCode.EMPLOYEE_UPDATE_FAILED, message);
    }

    /**
     * Creates a new EmployeeUpdateException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public EmployeeUpdateException(String message, Throwable cause) {
        super(ErrorCode.EMPLOYEE_UPDATE_FAILED, message, cause);
    }

    /**
     * Creates a new EmployeeUpdateException with specific error code
     * 
     * @param errorCode Specific error code for the update failure
     * @param message   Custom error message
     */
    public EmployeeUpdateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new EmployeeUpdateException with specific error code and cause
     * 
     * @param errorCode Specific error code for the update failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public EmployeeUpdateException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new EmployeeUpdateException for employee not found
     * 
     * @param employeeId The ID of the employee that was not found
     * @return EmployeeUpdateException instance
     */
    public static EmployeeUpdateException employeeNotFound(Long employeeId) {
        return new EmployeeUpdateException(ErrorCode.EMPLOYEE_NOT_FOUND,
                "Cannot update employee - employee with ID " + employeeId + " not found");
    }

    /**
     * Creates a new EmployeeUpdateException for bus operator not found
     * 
     * @param operatorId The ID of the bus operator that was not found
     * @return EmployeeUpdateException instance
     */
    public static EmployeeUpdateException busOperatorNotFound(Long operatorId) {
        return new EmployeeUpdateException(ErrorCode.EMPLOYEE_BUS_OPERATOR_NOT_FOUND,
                "Cannot update employee - bus operator with ID " + operatorId + " not found");
    }

    /**
     * Creates a new EmployeeUpdateException for validation failure
     * 
     * @param field The field that failed validation
     * @param value The invalid value
     * @return EmployeeUpdateException instance
     */
    public static EmployeeUpdateException validationFailed(String field, String value) {
        return new EmployeeUpdateException("Validation failed for " + field + ": " + value);
    }

    /**
     * Creates a new EmployeeUpdateException for unauthorized update
     * 
     * @param employeeId The ID of the employee
     * @param userId     The ID of the unauthorized user
     * @return EmployeeUpdateException instance
     */
    public static EmployeeUpdateException unauthorized(Long employeeId, Long userId) {
        return new EmployeeUpdateException(ErrorCode.EMPLOYEE_UNAUTHORIZED_ACCESS,
                "User " + userId + " is not authorized to update employee " + employeeId);
    }

    /**
     * Creates a new EmployeeUpdateException for invalid status
     * 
     * @param employeeId The ID of the employee
     * @param status     The invalid status
     * @return EmployeeUpdateException instance
     */
    public static EmployeeUpdateException invalidStatus(Long employeeId, String status) {
        return new EmployeeUpdateException(ErrorCode.EMPLOYEE_INVALID_STATUS,
                "Invalid status " + status + " for employee " + employeeId);
    }

    /**
     * Creates a new EmployeeUpdateException for duplicate email
     * 
     * @param email The duplicate email
     * @return EmployeeUpdateException instance
     */
    public static EmployeeUpdateException duplicateEmail(String email) {
        return new EmployeeUpdateException(ErrorCode.EMPLOYEE_EMAIL_EXISTS,
                "Cannot update employee - email " + email + " already exists");
    }

    /**
     * Creates a new EmployeeUpdateException for database failure
     * 
     * @param employeeId The ID of the employee
     * @param cause      The underlying cause of the failure
     * @return EmployeeUpdateException instance
     */
    public static EmployeeUpdateException databaseFailure(Long employeeId, Throwable cause) {
        return new EmployeeUpdateException("Failed to update employee " + employeeId + " due to database error", cause);
    }

    /**
     * Creates a new EmployeeUpdateException for duplicate driver license number
     *
     * @param licenseNumber The duplicate driver license number
     * @return EmployeeUpdateException instance
     */
    public static EmployeeUpdateException duplicateDriverLicense(String licenseNumber) {
        return new EmployeeUpdateException(
                ErrorCode.EMPLOYEE_DRIVER_LICENSE_EXISTS,
                "Cannot update employee - driver license number " + licenseNumber + " already exists"
        );
    }
}
