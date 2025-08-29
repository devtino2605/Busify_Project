package com.busify.project.contract.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a contract is not found in the system
 * <p>
 * This exception is used when attempting to retrieve, update, or review
 * a contract that does not exist in the database.
 * </p>
 */
public class ContractNotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ContractNotFoundException with default error code
     * 
     * @param contractId The ID of the contract that was not found
     */
    public ContractNotFoundException(Long contractId) {
        super(ErrorCode.CONTRACT_NOT_FOUND, "Contract with ID " + contractId + " not found");
    }

    /**
     * Creates a new ContractNotFoundException with custom message
     * 
     * @param message Custom error message
     */
    public ContractNotFoundException(String message) {
        super(ErrorCode.CONTRACT_NOT_FOUND, message);
    }

    /**
     * Creates a new ContractNotFoundException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public ContractNotFoundException(String message, Throwable cause) {
        super(ErrorCode.CONTRACT_NOT_FOUND, message, cause);
    }

    /**
     * Creates a new ContractNotFoundException for contract ID
     * 
     * @param contractId The ID of the contract that was not found
     * @return ContractNotFoundException instance
     */
    public static ContractNotFoundException withId(Long contractId) {
        return new ContractNotFoundException(contractId);
    }

    /**
     * Creates a new ContractNotFoundException for contract by email
     * 
     * @param email The email that has no contracts
     * @return ContractNotFoundException instance
     */
    public static ContractNotFoundException byEmail(String email) {
        return new ContractNotFoundException("No contracts found for email: " + email);
    }

    /**
     * Creates a new ContractNotFoundException for contract by VAT code
     * 
     * @param vatCode The VAT code that has no contracts
     * @return ContractNotFoundException instance
     */
    public static ContractNotFoundException byVatCode(String vatCode) {
        return new ContractNotFoundException("No contracts found for VAT code: " + vatCode);
    }

    /**
     * Creates a new ContractNotFoundException for contract by operation area
     * 
     * @param operationArea The operation area that has no contracts
     * @return ContractNotFoundException instance
     */
    public static ContractNotFoundException byOperationArea(String operationArea) {
        return new ContractNotFoundException("No contracts found for operation area: " + operationArea);
    }
}
