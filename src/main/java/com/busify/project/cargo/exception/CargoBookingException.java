package com.busify.project.cargo.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * CargoBookingException
 * 
 * General exception for cargo booking operations
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
public class CargoBookingException extends AppException {

    public CargoBookingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CargoBookingException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public CargoBookingException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Generic cargo booking failed
     */
    public static CargoBookingException failed(String reason) {
        return new CargoBookingException(ErrorCode.CARGO_BOOKING_FAILED, reason);
    }

    /**
     * Cargo booking failed with cause
     */
    public static CargoBookingException failed(String reason, Throwable cause) {
        return new CargoBookingException(ErrorCode.CARGO_BOOKING_FAILED, reason, cause);
    }
}
