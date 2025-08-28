package com.busify.project.bus.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when bus creation fails
 * <p>
 * This exception is used when a new bus cannot be created due to
 * validation errors, business rule violations, or system constraints.
 * </p>
 */
public class BusCreationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusCreationException with default error code
     */
    public BusCreationException() {
        super(ErrorCode.BUS_CREATION_FAILED);
    }

    /**
     * Creates a new BusCreationException with custom message
     * 
     * @param message Custom error message
     */
    public BusCreationException(String message) {
        super(ErrorCode.BUS_CREATION_FAILED, message);
    }

    /**
     * Creates a new BusCreationException for license plate conflict
     * 
     * @param licensePlate The conflicting license plate
     */
    public static BusCreationException licensePlateExists(String licensePlate) {
        return new BusCreationException(ErrorCode.BUS_LICENSE_PLATE_EXISTS,
                "Bus with license plate '" + licensePlate + "' already exists");
    }

    /**
     * Creates a new BusCreationException for invalid model
     * 
     * @param modelId The invalid model ID
     */
    public static BusCreationException invalidModel(Long modelId) {
        return new BusCreationException(ErrorCode.BUS_MODEL_NOT_FOUND,
                "Bus model with ID " + modelId + " not found");
    }

    /**
     * Creates a new BusCreationException for invalid seat layout
     * 
     * @param layoutId The invalid layout ID
     */
    public static BusCreationException invalidSeatLayout(Long layoutId) {
        return new BusCreationException(ErrorCode.SEAT_LAYOUT_NOT_FOUND,
                "Seat layout with ID " + layoutId + " not found");
    }

    /**
     * Creates a new BusCreationException for invalid seat layout data
     * 
     * @param reason The reason for invalid layout data
     */
    public static BusCreationException invalidSeatLayoutData(String reason) {
        return new BusCreationException(ErrorCode.INVALID_SEAT_LAYOUT_DATA,
                "Invalid seat layout data: " + reason);
    }

    /**
     * Creates a new BusCreationException with specific error code
     * 
     * @param errorCode Specific error code for the creation failure
     * @param message   Custom error message
     */
    public BusCreationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BusCreationException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusCreationException(String message, Throwable cause) {
        super(ErrorCode.BUS_CREATION_FAILED, message, cause);
    }
}
