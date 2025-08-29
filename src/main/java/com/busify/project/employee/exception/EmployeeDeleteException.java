package com.busify.project.employee.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when employee deletion operations fail
 * <p>
 * This exception is used when employee deletion operations fail due to
 * business constraints, active trips, or authorization issues.
 * </p>
 */
public class EmployeeDeleteException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new EmployeeDeleteException with default error code
     */
    public EmployeeDeleteException() {
        super(ErrorCode.EMPLOYEE_DELETE_FAILED);
    }

    /**
     * Creates a new EmployeeDeleteException with custom message
     * 
     * @param message Custom error message
     */
    public EmployeeDeleteException(String message) {
        super(ErrorCode.EMPLOYEE_DELETE_FAILED, message);
    }

    /**
     * Creates a new EmployeeDeleteException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public EmployeeDeleteException(String message, Throwable cause) {
        super(ErrorCode.EMPLOYEE_DELETE_FAILED, message, cause);
    }

    /**
     * Creates a new EmployeeDeleteException with specific error code
     * 
     * @param errorCode Specific error code for the deletion failure
     * @param message   Custom error message
     */
    public EmployeeDeleteException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new EmployeeDeleteException with specific error code and cause
     * 
     * @param errorCode Specific error code for the deletion failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public EmployeeDeleteException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new EmployeeDeleteException for employee not found
     * 
     * @param employeeId The ID of the employee that was not found
     * @return EmployeeDeleteException instance
     */
    public static EmployeeDeleteException employeeNotFound(Long employeeId) {
        return new EmployeeDeleteException(ErrorCode.EMPLOYEE_NOT_FOUND,
                "Cannot delete employee - employee with ID " + employeeId + " not found");
    }

    /**
     * Creates a new EmployeeDeleteException for employee with active trips
     * 
     * @param employeeId The ID of the employee with active trips
     * @return EmployeeDeleteException instance
     */
    public static EmployeeDeleteException hasActiveTrips(Long employeeId) {
        return new EmployeeDeleteException(ErrorCode.EMPLOYEE_HAS_ACTIVE_TRIPS,
                "Cannot delete employee " + employeeId + " - employee has active trips");
    }

    /**
     * Creates a new EmployeeDeleteException for self-deletion attempt
     * 
     * @param employeeId The ID of the employee trying to delete themselves
     * @return EmployeeDeleteException instance
     */
    public static EmployeeDeleteException cannotDeleteSelf(Long employeeId) {
        return new EmployeeDeleteException(ErrorCode.EMPLOYEE_CANNOT_DELETE_SELF,
                "Employee " + employeeId + " cannot delete their own account");
    }

    /**
     * Creates a new EmployeeDeleteException for unauthorized deletion
     * 
     * @param employeeId The ID of the employee
     * @param userId     The ID of the unauthorized user
     * @return EmployeeDeleteException instance
     */
    public static EmployeeDeleteException unauthorized(Long employeeId, Long userId) {
        return new EmployeeDeleteException(ErrorCode.EMPLOYEE_UNAUTHORIZED_ACCESS,
                "User " + userId + " is not authorized to delete employee " + employeeId);
    }

    /**
     * Creates a new EmployeeDeleteException for database failure
     * 
     * @param employeeId The ID of the employee
     * @param cause      The underlying cause of the failure
     * @return EmployeeDeleteException instance
     */
    public static EmployeeDeleteException databaseFailure(Long employeeId, Throwable cause) {
        return new EmployeeDeleteException("Failed to delete employee " + employeeId + " due to database error", cause);
    }

    /**
     * Creates a new EmployeeDeleteException for validation failure
     * 
     * @param employeeId The ID of the employee
     * @param reason     The reason for validation failure
     * @return EmployeeDeleteException instance
     */
    public static EmployeeDeleteException validationFailed(Long employeeId, String reason) {
        return new EmployeeDeleteException("Cannot delete employee " + employeeId + ": " + reason);
    }

    /**
     * Creates a new EmployeeDeleteException for business constraint violation
     * 
     * @param employeeId The ID of the employee
     * @param constraint The business constraint that was violated
     * @return EmployeeDeleteException instance
     */
    public static EmployeeDeleteException constraintViolation(Long employeeId, String constraint) {
        return new EmployeeDeleteException("Cannot delete employee " + employeeId + " - " + constraint);
    }
}
