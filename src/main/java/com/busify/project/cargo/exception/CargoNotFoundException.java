package com.busify.project.cargo.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * CargoNotFoundException
 * 
 * Exception thrown when a cargo booking is not found
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
public class CargoNotFoundException extends AppException {

    public CargoNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CargoNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Cargo booking not found by ID
     */
    public static CargoNotFoundException notFound() {
        return new CargoNotFoundException(ErrorCode.CARGO_NOT_FOUND);
    }

    /**
     * Create exception for cargo code not found with custom message
     */
    public static CargoNotFoundException byCargoCode(String cargoCode) {
        return new CargoNotFoundException(
                ErrorCode.CARGO_NOT_FOUND,
                "Không tìm thấy đơn hàng với mã: " + cargoCode);
    }

    /**
     * Create exception for cargo ID not found with custom message
     */
    public static CargoNotFoundException byId(Long cargoBookingId) {
        return new CargoNotFoundException(
                ErrorCode.CARGO_NOT_FOUND,
                "Không tìm thấy đơn hàng với ID: " + cargoBookingId);
    }
}
