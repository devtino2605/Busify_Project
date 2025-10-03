package com.busify.project.contract.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when contract update fails
 * <p>
 * This exception is used when a contract cannot be updated due to
 * validation errors, status constraints, or business rule violations.
 * </p>
 */
public class ContractUpdateException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ContractUpdateException with default error code
     */
    public ContractUpdateException() {
        super(ErrorCode.CONTRACT_UPDATE_FAILED);
    }

    /**
     * Creates a new ContractUpdateException with custom message
     * 
     * @param message Custom error message
     */
    public ContractUpdateException(String message) {
        super(ErrorCode.CONTRACT_UPDATE_FAILED, message);
    }

    /**
     * Creates a new ContractUpdateException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public ContractUpdateException(String message, Throwable cause) {
        super(ErrorCode.CONTRACT_UPDATE_FAILED, message, cause);
    }

    /**
     * Creates a new ContractUpdateException with specific error code
     * 
     * @param errorCode Specific error code for the update failure
     * @param message   Custom error message
     */
    public ContractUpdateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new ContractUpdateException for contract not found
     * 
     * @param contractId The ID of the contract that was not found
     * @return ContractUpdateException instance
     */
    public static ContractUpdateException contractNotFound(Long contractId) {
        return new ContractUpdateException(ErrorCode.CONTRACT_NOT_FOUND,
                "Contract with ID " + contractId + " not found");
    }

    /**
     * Creates a new ContractUpdateException for invalid status
     * 
     * @param contractId    The ID of the contract
     * @param currentStatus Current contract status
     * @return ContractUpdateException instance
     */
    public static ContractUpdateException invalidStatus(Long contractId, String currentStatus) {
        return new ContractUpdateException(ErrorCode.CONTRACT_INVALID_STATUS,
                "Cannot update contract " + contractId + " with status: " + currentStatus);
    }

    /**
     * Creates a new ContractUpdateException for status transition not allowed
     * 
     * @param currentStatus Current contract status
     * @param newStatus     Attempted new status
     * @return ContractUpdateException instance
     */
    public static ContractUpdateException statusTransitionNotAllowed(String currentStatus, String newStatus) {
        return new ContractUpdateException(ErrorCode.CONTRACT_STATUS_TRANSITION_NOT_ALLOWED,
                "Status transition from " + currentStatus + " to " + newStatus + " is not allowed");
    }

    /**
     * Creates a new ContractUpdateException for validation failures
     * 
     * @param field The field that failed validation
     * @param value The invalid value
     * @return ContractUpdateException instance
     */
    public static ContractUpdateException validationFailed(String field, String value) {
        return new ContractUpdateException("Validation failed for field " + field + " with value: " + value);
    }

    /**
     * Creates a new ContractUpdateException for duplicate contract
     * 
     * @param email The email that already has a contract
     * @return ContractUpdateException instance
     */
    public static ContractUpdateException duplicateContract(String email) {
        return new ContractUpdateException(ErrorCode.CONTRACT_ALREADY_EXISTS,
                "Contract already exists for email: " + email);
    }
}
