package com.busify.project.notification.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when notification creation operations fail
 * <p>
 * This exception is used when notification creation operations fail due to
 * validation errors, database constraints, or processing issues.
 * </p>
 */
public class NotificationCreationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new NotificationCreationException with default error code
     */
    public NotificationCreationException() {
        super(ErrorCode.NOTIFICATION_CREATION_FAILED);
    }

    /**
     * Creates a new NotificationCreationException with custom message
     * 
     * @param message Custom error message
     */
    public NotificationCreationException(String message) {
        super(ErrorCode.NOTIFICATION_CREATION_FAILED, message);
    }

    /**
     * Creates a new NotificationCreationException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public NotificationCreationException(String message, Throwable cause) {
        super(ErrorCode.NOTIFICATION_CREATION_FAILED, message, cause);
    }

    /**
     * Creates a new NotificationCreationException with specific error code
     * 
     * @param errorCode Specific error code for the creation failure
     * @param message   Custom error message
     */
    public NotificationCreationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new NotificationCreationException with specific error code and
     * cause
     * 
     * @param errorCode Specific error code for the creation failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public NotificationCreationException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new NotificationCreationException for general creation failure
     * 
     * @param cause The underlying cause of the failure
     * @return NotificationCreationException instance
     */
    public static NotificationCreationException creationFailed(Throwable cause) {
        return new NotificationCreationException("Không thể tạo notification", cause);
    }

    /**
     * Creates a new NotificationCreationException for user not found
     * 
     * @param userId The ID of the user that was not found
     * @return NotificationCreationException instance
     */
    public static NotificationCreationException userNotFound(Long userId) {
        return new NotificationCreationException(ErrorCode.NOTIFICATION_USER_NOT_FOUND,
                "Cannot create notification - user with ID " + userId + " not found");
    }

    /**
     * Creates a new NotificationCreationException for invalid notification type
     * 
     * @param type The invalid notification type
     * @return NotificationCreationException instance
     */
    public static NotificationCreationException invalidType(String type) {
        return new NotificationCreationException(ErrorCode.NOTIFICATION_INVALID_TYPE,
                "Invalid notification type: " + type);
    }

    /**
     * Creates a new NotificationCreationException for validation failure
     * 
     * @param field The field that failed validation
     * @param value The invalid value
     * @return NotificationCreationException instance
     */
    public static NotificationCreationException validationFailed(String field, String value) {
        return new NotificationCreationException("Validation failed for " + field + ": " + value);
    }

    /**
     * Creates a new NotificationCreationException for database failure
     * 
     * @param userId The user ID for the notification
     * @param cause  The underlying cause of the failure
     * @return NotificationCreationException instance
     */
    public static NotificationCreationException databaseFailure(Long userId, Throwable cause) {
        return new NotificationCreationException(
                "Failed to create notification for user " + userId + " due to database error", cause);
    }

    /**
     * Creates a new NotificationCreationException for missing required data
     * 
     * @param missingField The field that is missing
     * @return NotificationCreationException instance
     */
    public static NotificationCreationException missingRequiredData(String missingField) {
        return new NotificationCreationException("Missing required data: " + missingField);
    }

    /**
     * Creates a new NotificationCreationException for unauthorized creation
     * 
     * @param userId The ID of the unauthorized user
     * @return NotificationCreationException instance
     */
    public static NotificationCreationException unauthorized(Long userId) {
        return new NotificationCreationException(ErrorCode.NOTIFICATION_UNAUTHORIZED_ACCESS,
                "User " + userId + " is not authorized to create notifications");
    }

    /**
     * Creates a new NotificationCreationException for processing failure
     * 
     * @param operation The operation that failed
     * @param cause     The underlying cause of the failure
     * @return NotificationCreationException instance
     */
    public static NotificationCreationException processingFailed(String operation, Throwable cause) {
        return new NotificationCreationException(ErrorCode.NOTIFICATION_PROCESSING_FAILED,
                "Failed to process notification during " + operation, cause);
    }
}
