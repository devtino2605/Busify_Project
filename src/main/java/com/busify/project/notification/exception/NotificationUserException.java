package com.busify.project.notification.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when notification user operations fail
 * <p>
 * This exception is used when operations related to notification users
 * fail, such as missing users or invalid user associations.
 * </p>
 */
public class NotificationUserException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new NotificationUserException with default error code
     */
    public NotificationUserException() {
        super(ErrorCode.NOTIFICATION_USER_NOT_FOUND);
    }

    /**
     * Creates a new NotificationUserException with custom message
     * 
     * @param message Custom error message
     */
    public NotificationUserException(String message) {
        super(ErrorCode.NOTIFICATION_USER_NOT_FOUND, message);
    }

    /**
     * Creates a new NotificationUserException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public NotificationUserException(String message, Throwable cause) {
        super(ErrorCode.NOTIFICATION_USER_NOT_FOUND, message, cause);
    }

    /**
     * Creates a new NotificationUserException with specific error code
     * 
     * @param errorCode Specific error code for the user failure
     * @param message   Custom error message
     */
    public NotificationUserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new NotificationUserException with specific error code and cause
     * 
     * @param errorCode Specific error code for the user failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public NotificationUserException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new NotificationUserException for user not found
     * 
     * @param userId The ID of the user that was not found
     * @return NotificationUserException instance
     */
    public static NotificationUserException userNotFound(Long userId) {
        return new NotificationUserException("User with ID " + userId + " not found");
    }

    /**
     * Creates a new NotificationUserException for user not exists (Vietnamese
     * message)
     * 
     * @return NotificationUserException instance
     */
    public static NotificationUserException userNotExists() {
        return new NotificationUserException("User không tồn tại");
    }

    /**
     * Creates a new NotificationUserException for user not found by email
     * 
     * @param email The email of the user that was not found
     * @return NotificationUserException instance
     */
    public static NotificationUserException userNotFoundByEmail(String email) {
        return new NotificationUserException("User with email " + email + " not found");
    }

    /**
     * Creates a new NotificationUserException for invalid user context
     * 
     * @param context The context in which user validation failed
     * @return NotificationUserException instance
     */
    public static NotificationUserException invalidUserContext(String context) {
        return new NotificationUserException("Invalid user context: " + context);
    }

    /**
     * Creates a new NotificationUserException for unauthorized user access
     * 
     * @param userId         The ID of the unauthorized user
     * @param notificationId The ID of the notification
     * @return NotificationUserException instance
     */
    public static NotificationUserException unauthorized(Long userId, Long notificationId) {
        return new NotificationUserException(ErrorCode.NOTIFICATION_UNAUTHORIZED_ACCESS,
                "User " + userId + " is not authorized to access notification " + notificationId);
    }

    /**
     * Creates a new NotificationUserException for user authentication failure
     * 
     * @param operation The operation that failed
     * @return NotificationUserException instance
     */
    public static NotificationUserException authenticationFailed(String operation) {
        return new NotificationUserException(ErrorCode.NOTIFICATION_UNAUTHORIZED_ACCESS,
                "User authentication failed for operation: " + operation);
    }

    /**
     * Creates a new NotificationUserException for user retrieval failure
     * 
     * @param email The email of the user
     * @param cause The underlying cause of the failure
     * @return NotificationUserException instance
     */
    public static NotificationUserException retrievalFailed(String email, Throwable cause) {
        return new NotificationUserException("Failed to retrieve user with email " + email, cause);
    }
}
