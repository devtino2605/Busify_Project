package com.busify.project.complaint.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when complaint update fails
 * <p>
 * This exception is used when a complaint cannot be updated due to
 * validation errors, business rule violations, or system constraints.
 * </p>
 */
public class ComplaintUpdateException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ComplaintUpdateException with default error code
     */
    public ComplaintUpdateException() {
        super(ErrorCode.COMPLAINT_UPDATE_FAILED);
    }

    /**
     * Creates a new ComplaintUpdateException with custom message
     * 
     * @param message Custom error message
     */
    public ComplaintUpdateException(String message) {
        super(ErrorCode.COMPLAINT_UPDATE_FAILED, message);
    }

    /**
     * Creates a new ComplaintUpdateException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public ComplaintUpdateException(String message, Throwable cause) {
        super(ErrorCode.COMPLAINT_UPDATE_FAILED, message, cause);
    }

    /**
     * Creates a new ComplaintUpdateException with specific error code
     * 
     * @param errorCode Specific error code for the update failure
     * @param message   Custom error message
     */
    public ComplaintUpdateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new ComplaintUpdateException for complaint not found
     * 
     * @param complaintId The ID of the complaint that was not found
     * @return ComplaintUpdateException instance
     */
    public static ComplaintUpdateException complaintNotFound(Long complaintId) {
        return new ComplaintUpdateException(ErrorCode.COMPLAINT_NOT_FOUND,
                "Complaint with ID " + complaintId + " not found");
    }

    /**
     * Creates a new ComplaintUpdateException for assigned agent not found
     * 
     * @param agentId The ID of the agent that was not found
     * @return ComplaintUpdateException instance
     */
    public static ComplaintUpdateException agentNotFound(Long agentId) {
        return new ComplaintUpdateException(ErrorCode.COMPLAINT_AGENT_NOT_FOUND,
                "Assigned agent with ID " + agentId + " not found");
    }

    /**
     * Creates a new ComplaintUpdateException for invalid status transition
     * 
     * @param currentStatus Current complaint status
     * @param newStatus     Attempted new status
     * @return ComplaintUpdateException instance
     */
    public static ComplaintUpdateException invalidStatusTransition(String currentStatus, String newStatus) {
        return new ComplaintUpdateException(ErrorCode.COMPLAINT_INVALID_STATUS_TRANSITION,
                "Invalid status transition from " + currentStatus + " to " + newStatus);
    }

    /**
     * Creates a new ComplaintUpdateException for unauthorized access
     * 
     * @param complaintId The ID of the complaint
     * @param userId      The ID of the unauthorized user
     * @return ComplaintUpdateException instance
     */
    public static ComplaintUpdateException unauthorized(Long complaintId, Long userId) {
        return new ComplaintUpdateException(ErrorCode.COMPLAINT_UNAUTHORIZED_ACCESS,
                "User " + userId + " is not authorized to update complaint " + complaintId);
    }

    /**
     * Creates a new ComplaintUpdateException for validation failures
     * 
     * @param field The field that failed validation
     * @param value The invalid value
     * @return ComplaintUpdateException instance
     */
    public static ComplaintUpdateException validationFailed(String field, String value) {
        return new ComplaintUpdateException("Validation failed for field " + field + " with value: " + value);
    }

    /**
     * Creates a new ComplaintUpdateException for already assigned complaint
     * 
     * @param complaintId    The ID of the complaint
     * @param currentAgentId The ID of the currently assigned agent
     * @return ComplaintUpdateException instance
     */
    public static ComplaintUpdateException alreadyAssigned(Long complaintId, Long currentAgentId) {
        return new ComplaintUpdateException(ErrorCode.COMPLAINT_ALREADY_ASSIGNED,
                "Complaint " + complaintId + " is already assigned to agent " + currentAgentId);
    }
}
