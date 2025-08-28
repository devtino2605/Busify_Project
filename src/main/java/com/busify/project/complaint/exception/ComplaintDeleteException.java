package com.busify.project.complaint.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when complaint deletion fails
 * <p>
 * This exception is used when a complaint cannot be deleted due to
 * business constraints or authorization issues.
 * </p>
 */
public class ComplaintDeleteException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ComplaintDeleteException with default error code
     */
    public ComplaintDeleteException() {
        super(ErrorCode.COMPLAINT_DELETE_FAILED);
    }

    /**
     * Creates a new ComplaintDeleteException with custom message
     * 
     * @param message Custom error message
     */
    public ComplaintDeleteException(String message) {
        super(ErrorCode.COMPLAINT_DELETE_FAILED, message);
    }

    /**
     * Creates a new ComplaintDeleteException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public ComplaintDeleteException(String message, Throwable cause) {
        super(ErrorCode.COMPLAINT_DELETE_FAILED, message, cause);
    }

    /**
     * Creates a new ComplaintDeleteException with specific error code
     * 
     * @param errorCode Specific error code for the deletion failure
     * @param message   Custom error message
     */
    public ComplaintDeleteException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new ComplaintDeleteException for complaint not found
     * 
     * @param complaintId The ID of the complaint that was not found
     * @return ComplaintDeleteException instance
     */
    public static ComplaintDeleteException complaintNotFound(Long complaintId) {
        return new ComplaintDeleteException(ErrorCode.COMPLAINT_NOT_FOUND,
                "Complaint with ID " + complaintId + " not found");
    }

    /**
     * Creates a new ComplaintDeleteException for unauthorized access
     * 
     * @param complaintId The ID of the complaint
     * @param userId      The ID of the unauthorized user
     * @return ComplaintDeleteException instance
     */
    public static ComplaintDeleteException unauthorized(Long complaintId, Long userId) {
        return new ComplaintDeleteException(ErrorCode.COMPLAINT_UNAUTHORIZED_ACCESS,
                "User " + userId + " is not authorized to delete complaint " + complaintId);
    }

    /**
     * Creates a new ComplaintDeleteException for complaints in progress
     * 
     * @param complaintId The ID of the complaint in progress
     * @return ComplaintDeleteException instance
     */
    public static ComplaintDeleteException inProgress(Long complaintId) {
        return new ComplaintDeleteException("Cannot delete complaint " + complaintId +
                " because it is currently in progress");
    }

    /**
     * Creates a new ComplaintDeleteException for resolved complaints
     * 
     * @param complaintId The ID of the resolved complaint
     * @return ComplaintDeleteException instance
     */
    public static ComplaintDeleteException resolved(Long complaintId) {
        return new ComplaintDeleteException("Cannot delete complaint " + complaintId +
                " because it has been resolved");
    }

    /**
     * Creates a new ComplaintDeleteException for complaints with references
     * 
     * @param complaintId     The ID of the complaint with references
     * @param referencesCount Number of references
     * @return ComplaintDeleteException instance
     */
    public static ComplaintDeleteException hasReferences(Long complaintId, int referencesCount) {
        return new ComplaintDeleteException("Cannot delete complaint " + complaintId +
                " because it has " + referencesCount + " reference(s)");
    }
}
