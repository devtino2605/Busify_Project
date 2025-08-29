package com.busify.project.employee.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when employee bus operator operations fail
 * <p>
 * This exception is used when operations related to employee-bus operator
 * associations fail, such as missing operators or invalid operator assignments.
 * </p>
 */
public class EmployeeBusOperatorException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new EmployeeBusOperatorException with default error code
     */
    public EmployeeBusOperatorException() {
        super(ErrorCode.EMPLOYEE_BUS_OPERATOR_NOT_FOUND);
    }

    /**
     * Creates a new EmployeeBusOperatorException with custom message
     * 
     * @param message Custom error message
     */
    public EmployeeBusOperatorException(String message) {
        super(ErrorCode.EMPLOYEE_BUS_OPERATOR_NOT_FOUND, message);
    }

    /**
     * Creates a new EmployeeBusOperatorException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public EmployeeBusOperatorException(String message, Throwable cause) {
        super(ErrorCode.EMPLOYEE_BUS_OPERATOR_NOT_FOUND, message, cause);
    }

    /**
     * Creates a new EmployeeBusOperatorException with specific error code
     * 
     * @param errorCode Specific error code for the bus operator failure
     * @param message   Custom error message
     */
    public EmployeeBusOperatorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new EmployeeBusOperatorException with specific error code and cause
     * 
     * @param errorCode Specific error code for the bus operator failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public EmployeeBusOperatorException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new EmployeeBusOperatorException for operator not found
     * 
     * @param operatorId The ID of the bus operator that was not found
     * @return EmployeeBusOperatorException instance
     */
    public static EmployeeBusOperatorException operatorNotFound(Long operatorId) {
        return new EmployeeBusOperatorException("Bus operator with ID " + operatorId + " not found");
    }

    /**
     * Creates a new EmployeeBusOperatorException for operator not found (Vietnamese
     * message)
     * 
     * @return EmployeeBusOperatorException instance
     */
    public static EmployeeBusOperatorException operatorNotExists() {
        return new EmployeeBusOperatorException("Bus Operator không tồn tại");
    }

    /**
     * Creates a new EmployeeBusOperatorException for employee not associated with
     * operator
     * 
     * @param employeeId The ID of the employee
     * @param operatorId The ID of the bus operator
     * @return EmployeeBusOperatorException instance
     */
    public static EmployeeBusOperatorException employeeNotAssociated(Long employeeId, Long operatorId) {
        return new EmployeeBusOperatorException(
                "Employee " + employeeId + " is not associated with bus operator " + operatorId);
    }

    /**
     * Creates a new EmployeeBusOperatorException for invalid operator assignment
     * 
     * @param employeeId The ID of the employee
     * @param operatorId The ID of the bus operator
     * @param reason     The reason for invalid assignment
     * @return EmployeeBusOperatorException instance
     */
    public static EmployeeBusOperatorException invalidAssignment(Long employeeId, Long operatorId, String reason) {
        return new EmployeeBusOperatorException(
                "Cannot assign employee " + employeeId + " to operator " + operatorId + ": " + reason);
    }

    /**
     * Creates a new EmployeeBusOperatorException for operator status issues
     * 
     * @param operatorId The ID of the bus operator
     * @param status     The invalid status
     * @return EmployeeBusOperatorException instance
     */
    public static EmployeeBusOperatorException invalidOperatorStatus(Long operatorId, String status) {
        return new EmployeeBusOperatorException("Bus operator " + operatorId + " has invalid status: " + status);
    }

    /**
     * Creates a new EmployeeBusOperatorException for operator authorization issues
     * 
     * @param employeeId The ID of the employee
     * @param operatorId The ID of the bus operator
     * @return EmployeeBusOperatorException instance
     */
    public static EmployeeBusOperatorException unauthorized(Long employeeId, Long operatorId) {
        return new EmployeeBusOperatorException(ErrorCode.EMPLOYEE_UNAUTHORIZED_ACCESS,
                "Employee " + employeeId + " is not authorized to access operator " + operatorId);
    }

    /**
     * Creates a new EmployeeBusOperatorException for operator data retrieval
     * failure
     * 
     * @param operatorId The ID of the bus operator
     * @param cause      The underlying cause of the failure
     * @return EmployeeBusOperatorException instance
     */
    public static EmployeeBusOperatorException retrievalFailed(Long operatorId, Throwable cause) {
        return new EmployeeBusOperatorException("Failed to retrieve bus operator " + operatorId, cause);
    }
}
