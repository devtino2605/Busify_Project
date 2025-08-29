package com.busify.project.contract.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when contract review fails
 * <p>
 * This exception is used when a contract review operation fails due to
 * invalid actions, status constraints, or business rule violations.
 * </p>
 */
public class ContractReviewException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ContractReviewException with default error code
     */
    public ContractReviewException() {
        super(ErrorCode.CONTRACT_REVIEW_FAILED);
    }

    /**
     * Creates a new ContractReviewException with custom message
     * 
     * @param message Custom error message
     */
    public ContractReviewException(String message) {
        super(ErrorCode.CONTRACT_REVIEW_FAILED, message);
    }

    /**
     * Creates a new ContractReviewException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public ContractReviewException(String message, Throwable cause) {
        super(ErrorCode.CONTRACT_REVIEW_FAILED, message, cause);
    }

    /**
     * Creates a new ContractReviewException with specific error code
     * 
     * @param errorCode Specific error code for the review failure
     * @param message   Custom error message
     */
    public ContractReviewException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new ContractReviewException with specific error code and cause
     * 
     * @param errorCode Specific error code for the review failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public ContractReviewException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new ContractReviewException for contract not found
     * 
     * @param contractId The ID of the contract that was not found
     * @return ContractReviewException instance
     */
    public static ContractReviewException contractNotFound(Long contractId) {
        return new ContractReviewException(ErrorCode.CONTRACT_NOT_FOUND,
                "Contract with ID " + contractId + " not found");
    }

    /**
     * Creates a new ContractReviewException for invalid action
     * 
     * @param action The invalid action attempted
     * @return ContractReviewException instance
     */
    public static ContractReviewException invalidAction(String action) {
        return new ContractReviewException(ErrorCode.CONTRACT_INVALID_ACTION,
                "Invalid action: " + action);
    }

    /**
     * Creates a new ContractReviewException for user creation failure
     * 
     * @param contractId The ID of the contract
     * @param cause      The underlying cause of the failure
     * @return ContractReviewException instance
     */
    public static ContractReviewException userCreationFailed(Long contractId, Throwable cause) {
        return new ContractReviewException(ErrorCode.CONTRACT_USER_CREATION_FAILED,
                "Failed to create user and bus operator for contract " + contractId, cause);
    }

    /**
     * Creates a new ContractReviewException for user creation failure with message
     * 
     * @param contractId The ID of the contract
     * @param message    The error message from user creation
     * @param cause      The underlying cause of the failure
     * @return ContractReviewException instance
     */
    public static ContractReviewException userCreationFailed(Long contractId, String message, Throwable cause) {
        return new ContractReviewException(ErrorCode.CONTRACT_USER_CREATION_FAILED,
                "Failed to create user and bus operator for contract " + contractId + ": " + message, cause);
    }

    /**
     * Creates a new ContractReviewException for invalid status for review
     * 
     * @param contractId    The ID of the contract
     * @param currentStatus Current contract status
     * @return ContractReviewException instance
     */
    public static ContractReviewException invalidStatusForReview(Long contractId, String currentStatus) {
        return new ContractReviewException(ErrorCode.CONTRACT_INVALID_STATUS,
                "Cannot review contract " + contractId + " with status: " + currentStatus);
    }

    /**
     * Creates a new ContractReviewException for unauthorized review
     * 
     * @param contractId The ID of the contract
     * @param userId     The ID of the unauthorized user
     * @return ContractReviewException instance
     */
    public static ContractReviewException unauthorized(Long contractId, Long userId) {
        return new ContractReviewException("User " + userId + " is not authorized to review contract " + contractId);
    }
}
