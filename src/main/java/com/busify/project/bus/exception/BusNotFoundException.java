package com.busify.project.bus.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a bus is not found in the system
 * <p>
 * This exception is used when attempting to retrieve, update, or delete
 * a bus that does not exist in the database.
 * </p>
 */
public class BusNotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusNotFoundException with default error code
     * 
     * @param busId The ID of the bus that was not found
     */
    public BusNotFoundException(Long busId) {
        super(ErrorCode.BUS_NOT_FOUND, "Bus with ID " + busId + " not found");
    }

    /**
     * Creates a new BusNotFoundException with custom message
     * 
     * @param message Custom error message
     */
    public BusNotFoundException(String message) {
        super(ErrorCode.BUS_NOT_FOUND, message);
    }

    /**
     * Creates a new BusNotFoundException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusNotFoundException(String message, Throwable cause) {
        super(ErrorCode.BUS_NOT_FOUND, message, cause);
    }
}
