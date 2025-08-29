package com.busify.project.complaint.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when complaint creation fails
 * <p>
 * This exception is used when a complaint cannot be created due to
 * validation errors, missing dependencies, or business rule violations.
 * </p>
 */
public class ComplaintCreationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ComplaintCreationException with default error code
     */
    public ComplaintCreationException() {
        super(ErrorCode.COMPLAINT_CREATION_FAILED);
    }

    /**
     * Creates a new ComplaintCreationException with custom message
     * 
     * @param message Custom error message
     */
    public ComplaintCreationException(String message) {
        super(ErrorCode.COMPLAINT_CREATION_FAILED, message);
    }

    /**
     * Creates a new ComplaintCreationException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public ComplaintCreationException(String message, Throwable cause) {
        super(ErrorCode.COMPLAINT_CREATION_FAILED, message, cause);
    }

    /**
     * Creates a new ComplaintCreationException with specific error code
     * 
     * @param errorCode Specific error code for the creation failure
     * @param message   Custom error message
     */
    public ComplaintCreationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new ComplaintCreationException for customer not found
     * 
     * @param customerId The ID of the customer that was not found
     * @return ComplaintCreationException instance
     */
    public static ComplaintCreationException customerNotFound(Long customerId) {
        return new ComplaintCreationException(ErrorCode.COMPLAINT_CUSTOMER_NOT_FOUND,
                "Customer with ID " + customerId + " not found");
    }

    /**
     * Creates a new ComplaintCreationException for booking not found
     * 
     * @param bookingId The ID of the booking that was not found
     * @return ComplaintCreationException instance
     */
    public static ComplaintCreationException bookingNotFound(Long bookingId) {
        return new ComplaintCreationException(ErrorCode.COMPLAINT_BOOKING_NOT_FOUND,
                "Booking with ID " + bookingId + " not found");
    }

    /**
     * Creates a new ComplaintCreationException for assigned agent not found
     * 
     * @param agentId The ID of the agent that was not found
     * @return ComplaintCreationException instance
     */
    public static ComplaintCreationException agentNotFound(Long agentId) {
        return new ComplaintCreationException(ErrorCode.COMPLAINT_AGENT_NOT_FOUND,
                "Assigned agent with ID " + agentId + " not found");
    }

    /**
     * Creates a new ComplaintCreationException for validation failures
     * 
     * @param field The field that failed validation
     * @param value The invalid value
     * @return ComplaintCreationException instance
     */
    public static ComplaintCreationException validationFailed(String field, String value) {
        return new ComplaintCreationException("Validation failed for field " + field + " with value: " + value);
    }

    /**
     * Creates a new ComplaintCreationException for duplicate complaint
     * 
     * @param bookingId  The booking ID that already has a complaint
     * @param customerId The customer ID attempting to create duplicate
     * @return ComplaintCreationException instance
     */
    public static ComplaintCreationException duplicateComplaint(Long bookingId, Long customerId) {
        return new ComplaintCreationException("Customer " + customerId +
                " already has a complaint for booking " + bookingId);
    }
}
