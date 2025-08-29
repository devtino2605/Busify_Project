package com.busify.project.contract.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when contract user and bus operator creation fails
 * <p>
 * This exception is used when the process of creating a user and bus operator
 * from a contract fails due to validation errors, database constraints, or
 * business rule violations.
 * </p>
 */
public class ContractUserCreationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ContractUserCreationException with default error code
     */
    public ContractUserCreationException() {
        super(ErrorCode.CONTRACT_USER_CREATION_FAILED);
    }

    /**
     * Creates a new ContractUserCreationException with custom message
     * 
     * @param message Custom error message
     */
    public ContractUserCreationException(String message) {
        super(ErrorCode.CONTRACT_USER_CREATION_FAILED, message);
    }

    /**
     * Creates a new ContractUserCreationException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public ContractUserCreationException(String message, Throwable cause) {
        super(ErrorCode.CONTRACT_USER_CREATION_FAILED, message, cause);
    }

    /**
     * Creates a new ContractUserCreationException with specific error code
     * 
     * @param errorCode Specific error code for the user creation failure
     * @param message   Custom error message
     */
    public ContractUserCreationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new ContractUserCreationException with specific error code and
     * cause
     * 
     * @param errorCode Specific error code for the user creation failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public ContractUserCreationException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new ContractUserCreationException for duplicate email
     * 
     * @param email The duplicate email address
     * @return ContractUserCreationException instance
     */
    public static ContractUserCreationException duplicateEmail(String email) {
        return new ContractUserCreationException("User with email " + email + " already exists");
    }

    /**
     * Creates a new ContractUserCreationException for validation failure
     * 
     * @param field The field that failed validation
     * @param value The invalid value
     * @return ContractUserCreationException instance
     */
    public static ContractUserCreationException validationFailed(String field, String value) {
        return new ContractUserCreationException("Validation failed for " + field + ": " + value);
    }

    /**
     * Creates a new ContractUserCreationException for user creation failure
     * 
     * @param contractId The ID of the contract
     * @param email      The email of the user being created
     * @param cause      The underlying cause of the failure
     * @return ContractUserCreationException instance
     */
    public static ContractUserCreationException userCreationFailed(Long contractId, String email, Throwable cause) {
        return new ContractUserCreationException("Failed to create user " + email + " for contract " + contractId,
                cause);
    }

    /**
     * Creates a new ContractUserCreationException for bus operator creation failure
     * 
     * @param contractId The ID of the contract
     * @param userId     The ID of the created user
     * @param cause      The underlying cause of the failure
     * @return ContractUserCreationException instance
     */
    public static ContractUserCreationException busOperatorCreationFailed(Long contractId, Long userId,
            Throwable cause) {
        return new ContractUserCreationException(
                "Failed to create bus operator for user " + userId + " from contract " + contractId, cause);
    }

    /**
     * Creates a new ContractUserCreationException for role assignment failure
     * 
     * @param userId   The ID of the user
     * @param roleName The role that failed to be assigned
     * @param cause    The underlying cause of the failure
     * @return ContractUserCreationException instance
     */
    public static ContractUserCreationException roleAssignmentFailed(Long userId, String roleName, Throwable cause) {
        return new ContractUserCreationException("Failed to assign role " + roleName + " to user " + userId, cause);
    }

    /**
     * Creates a new ContractUserCreationException for database transaction failure
     * 
     * @param contractId The ID of the contract
     * @param operation  The operation that failed
     * @param cause      The underlying cause of the failure
     * @return ContractUserCreationException instance
     */
    public static ContractUserCreationException transactionFailed(Long contractId, String operation, Throwable cause) {
        return new ContractUserCreationException(
                "Transaction failed during " + operation + " for contract " + contractId, cause);
    }

    /**
     * Creates a new ContractUserCreationException for missing required data
     * 
     * @param contractId   The ID of the contract
     * @param missingField The field that is missing or invalid
     * @return ContractUserCreationException instance
     */
    public static ContractUserCreationException missingData(Long contractId, String missingField) {
        return new ContractUserCreationException(
                "Missing required data for contract " + contractId + ": " + missingField);
    }
}
