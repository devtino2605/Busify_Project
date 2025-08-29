package com.busify.project.employee.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when an employee is not found in the system
 * <p>
 * This exception is used when attempting to retrieve, update, or delete
 * an employee that does not exist in the database.
 * </p>
 */
public class EmployeeNotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new EmployeeNotFoundException with default error code
     * 
     * @param employeeId The ID of the employee that was not found
     */
    public EmployeeNotFoundException(Long employeeId) {
        super(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee with ID " + employeeId + " not found");
    }

    /**
     * Creates a new EmployeeNotFoundException with custom message
     * 
     * @param message Custom error message
     */
    public EmployeeNotFoundException(String message) {
        super(ErrorCode.EMPLOYEE_NOT_FOUND, message);
    }

    /**
     * Creates a new EmployeeNotFoundException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(ErrorCode.EMPLOYEE_NOT_FOUND, message, cause);
    }

    /**
     * Creates a new EmployeeNotFoundException with specific error code
     * 
     * @param errorCode Specific error code for the employee not found scenario
     * @param message   Custom error message
     */
    public EmployeeNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new EmployeeNotFoundException with specific error code and cause
     * 
     * @param errorCode Specific error code for the employee not found scenario
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public EmployeeNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new EmployeeNotFoundException for employee not found by ID
     * 
     * @param employeeId The ID of the employee that was not found
     * @return EmployeeNotFoundException instance
     */
    public static EmployeeNotFoundException withId(Long employeeId) {
        return new EmployeeNotFoundException(employeeId);
    }

    /**
     * Creates a new EmployeeNotFoundException for driver not found by ID
     * 
     * @param driverId The ID of the driver that was not found
     * @return EmployeeNotFoundException instance
     */
    public static EmployeeNotFoundException driverNotFound(Long driverId) {
        return new EmployeeNotFoundException(ErrorCode.DRIVER_NOT_FOUND,
                "Driver with ID " + driverId + " not found");
    }

    /**
     * Creates a new EmployeeNotFoundException for employee by email
     * 
     * @param email The email that has no employees
     * @return EmployeeNotFoundException instance
     */
    public static EmployeeNotFoundException byEmail(String email) {
        return new EmployeeNotFoundException("No employees found for email: " + email);
    }

    /**
     * Creates a new EmployeeNotFoundException for employee by operator
     * 
     * @param operatorId The operator ID that has no employees
     * @return EmployeeNotFoundException instance
     */
    public static EmployeeNotFoundException byOperator(Long operatorId) {
        return new EmployeeNotFoundException("No employees found for operator: " + operatorId);
    }

    /**
     * Creates a new EmployeeNotFoundException for employee by role
     * 
     * @param roleName The role name that has no employees
     * @return EmployeeNotFoundException instance
     */
    public static EmployeeNotFoundException byRole(String roleName) {
        return new EmployeeNotFoundException("No employees found for role: " + roleName);
    }

    /**
     * Creates a new EmployeeNotFoundException for general employee search
     * 
     * @param searchCriteria The search criteria that returned no results
     * @return EmployeeNotFoundException instance
     */
    public static EmployeeNotFoundException noResults(String searchCriteria) {
        return new EmployeeNotFoundException("No employees found matching criteria: " + searchCriteria);
    }
}
