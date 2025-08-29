package com.busify.project.complaint.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a complaint is not found in the system
 * <p>
 * This exception is used when attempting to retrieve, update, or delete
 * a complaint that does not exist in the database.
 * </p>
 */
public class ComplaintNotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ComplaintNotFoundException with default error code
     * 
     * @param complaintId The ID of the complaint that was not found
     */
    public ComplaintNotFoundException(Long complaintId) {
        super(ErrorCode.COMPLAINT_NOT_FOUND, "Complaint with ID " + complaintId + " not found");
    }

    /**
     * Creates a new ComplaintNotFoundException with custom message
     * 
     * @param message Custom error message
     */
    public ComplaintNotFoundException(String message) {
        super(ErrorCode.COMPLAINT_NOT_FOUND, message);
    }

    /**
     * Creates a new ComplaintNotFoundException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public ComplaintNotFoundException(String message, Throwable cause) {
        super(ErrorCode.COMPLAINT_NOT_FOUND, message, cause);
    }

    /**
     * Creates a new ComplaintNotFoundException for complaint ID
     * 
     * @param complaintId The ID of the complaint that was not found
     * @return ComplaintNotFoundException instance
     */
    public static ComplaintNotFoundException withId(Long complaintId) {
        return new ComplaintNotFoundException(complaintId);
    }

    /**
     * Creates a new ComplaintNotFoundException for complaint by booking ID
     * 
     * @param bookingId The booking ID that has no complaints
     * @return ComplaintNotFoundException instance
     */
    public static ComplaintNotFoundException byBookingId(Long bookingId) {
        return new ComplaintNotFoundException("No complaints found for booking ID " + bookingId);
    }

    /**
     * Creates a new ComplaintNotFoundException for complaint by customer ID
     * 
     * @param customerId The customer ID that has no complaints
     * @return ComplaintNotFoundException instance
     */
    public static ComplaintNotFoundException byCustomerId(Long customerId) {
        return new ComplaintNotFoundException("No complaints found for customer ID " + customerId);
    }

    /**
     * Creates a new ComplaintNotFoundException for complaint by agent ID
     * 
     * @param agentId The agent ID that has no assigned complaints
     * @return ComplaintNotFoundException instance
     */
    public static ComplaintNotFoundException byAgentId(Long agentId) {
        return new ComplaintNotFoundException("No complaints found for agent ID " + agentId);
    }
}
