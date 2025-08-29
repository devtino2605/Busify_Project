package com.busify.project.notification.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a notification is not found in the system
 * <p>
 * This exception is used when attempting to retrieve, update, or delete
 * a notification that does not exist in the database.
 * </p>
 */
public class NotificationNotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new NotificationNotFoundException with default error code
     * 
     * @param notificationId The ID of the notification that was not found
     */
    public NotificationNotFoundException(Long notificationId) {
        super(ErrorCode.NOTIFICATION_NOT_FOUND, "Notification with ID " + notificationId + " not found");
    }

    /**
     * Creates a new NotificationNotFoundException with custom message
     * 
     * @param message Custom error message
     */
    public NotificationNotFoundException(String message) {
        super(ErrorCode.NOTIFICATION_NOT_FOUND, message);
    }

    /**
     * Creates a new NotificationNotFoundException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public NotificationNotFoundException(String message, Throwable cause) {
        super(ErrorCode.NOTIFICATION_NOT_FOUND, message, cause);
    }

    /**
     * Creates a new NotificationNotFoundException with specific error code
     * 
     * @param errorCode Specific error code for the notification not found scenario
     * @param message   Custom error message
     */
    public NotificationNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new NotificationNotFoundException with specific error code and
     * cause
     * 
     * @param errorCode Specific error code for the notification not found scenario
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public NotificationNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new NotificationNotFoundException for notification not found by ID
     * 
     * @param notificationId The ID of the notification that was not found
     * @return NotificationNotFoundException instance
     */
    public static NotificationNotFoundException withId(Long notificationId) {
        return new NotificationNotFoundException(notificationId);
    }

    /**
     * Creates a new NotificationNotFoundException for notification not exists
     * (Vietnamese message)
     * 
     * @return NotificationNotFoundException instance
     */
    public static NotificationNotFoundException notExists() {
        return new NotificationNotFoundException("Notification không tồn tại");
    }

    /**
     * Creates a new NotificationNotFoundException for notification by user
     * 
     * @param userId The user ID that has no notifications
     * @return NotificationNotFoundException instance
     */
    public static NotificationNotFoundException byUser(Long userId) {
        return new NotificationNotFoundException("No notifications found for user: " + userId);
    }

    /**
     * Creates a new NotificationNotFoundException for notification by user and ID
     * 
     * @param notificationId The notification ID
     * @param userId         The user ID
     * @return NotificationNotFoundException instance
     */
    public static NotificationNotFoundException byUserAndId(Long notificationId, Long userId) {
        return new NotificationNotFoundException("Notification " + notificationId + " not found for user " + userId);
    }

    /**
     * Creates a new NotificationNotFoundException for notification by status
     * 
     * @param status The notification status
     * @param userId The user ID
     * @return NotificationNotFoundException instance
     */
    public static NotificationNotFoundException byStatus(String status, Long userId) {
        return new NotificationNotFoundException(
                "No notifications found with status " + status + " for user " + userId);
    }

    /**
     * Creates a new NotificationNotFoundException for general notification search
     * 
     * @param searchCriteria The search criteria that returned no results
     * @return NotificationNotFoundException instance
     */
    public static NotificationNotFoundException noResults(String searchCriteria) {
        return new NotificationNotFoundException("No notifications found matching criteria: " + searchCriteria);
    }
}
