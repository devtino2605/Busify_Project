package com.busify.project.bus.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when there are issues with bus seat layouts
 * <p>
 * This exception is used when seat layout data is invalid, missing,
 * or cannot be processed correctly.
 * </p>
 */
public class BusSeatLayoutException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusSeatLayoutException with default error code
     */
    public BusSeatLayoutException() {
        super(ErrorCode.SEAT_LAYOUT_NOT_FOUND, "Seat layout not found");
    }

    /**
     * Creates a new BusSeatLayoutException with custom message
     * 
     * @param message Custom error message
     */
    public BusSeatLayoutException(String message) {
        super(ErrorCode.SEAT_LAYOUT_NOT_FOUND, message);
    }

    /**
     * Creates a new BusSeatLayoutException for layout not found
     * 
     * @param layoutId The ID of the layout that was not found
     */
    public static BusSeatLayoutException layoutNotFound(Long layoutId) {
        return new BusSeatLayoutException(ErrorCode.SEAT_LAYOUT_NOT_FOUND,
                "Seat layout with ID " + layoutId + " not found");
    }

    /**
     * Creates a new BusSeatLayoutException for invalid layout data
     * 
     * @param reason The reason why the layout data is invalid
     */
    public static BusSeatLayoutException invalidLayoutData(String reason) {
        return new BusSeatLayoutException(ErrorCode.INVALID_SEAT_LAYOUT_DATA,
                "Invalid seat layout data: " + reason);
    }

    /**
     * Creates a new BusSeatLayoutException for missing layout for bus
     * 
     * @param busId The ID of the bus missing seat layout
     */
    public static BusSeatLayoutException missingLayoutForBus(Long busId) {
        return new BusSeatLayoutException(ErrorCode.SEAT_LAYOUT_NOT_FOUND,
                "Seat layout is missing for bus ID: " + busId);
    }

    /**
     * Creates a new BusSeatLayoutException for invalid layout JSON
     * 
     * @param layoutId  The ID of the layout with invalid JSON
     * @param jsonError The JSON parsing error
     */
    public static BusSeatLayoutException invalidLayoutJson(Long layoutId, String jsonError) {
        return new BusSeatLayoutException(ErrorCode.INVALID_SEAT_LAYOUT_DATA,
                "Invalid JSON in seat layout " + layoutId + ": " + jsonError);
    }

    /**
     * Creates a new BusSeatLayoutException with specific error code
     * 
     * @param errorCode Specific error code for the layout issue
     * @param message   Custom error message
     */
    public BusSeatLayoutException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BusSeatLayoutException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusSeatLayoutException(String message, Throwable cause) {
        super(ErrorCode.INVALID_SEAT_LAYOUT_DATA, message, cause);
    }
}
