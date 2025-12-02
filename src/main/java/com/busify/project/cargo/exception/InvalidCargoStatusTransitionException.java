package com.busify.project.cargo.exception;

import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * InvalidCargoStatusTransitionException
 * 
 * Exception thrown when attempting an invalid status transition
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
public class InvalidCargoStatusTransitionException extends AppException {

    private final CargoStatus currentStatus;
    private final CargoStatus newStatus;

    public InvalidCargoStatusTransitionException(CargoStatus currentStatus, CargoStatus newStatus) {
        super(ErrorCode.CARGO_INVALID_STATUS_TRANSITION,
                String.format(
                        "Không thể chuyển trạng thái từ '%s' sang '%s'",
                        currentStatus.getDisplayName(),
                        newStatus.getDisplayName()));
        this.currentStatus = currentStatus;
        this.newStatus = newStatus;
    }

    public InvalidCargoStatusTransitionException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.currentStatus = null;
        this.newStatus = null;
    }

    public CargoStatus getCurrentStatus() {
        return currentStatus;
    }

    public CargoStatus getNewStatus() {
        return newStatus;
    }

    /**
     * Create invalid transition exception
     */
    public static InvalidCargoStatusTransitionException transition(CargoStatus current, CargoStatus target) {
        return new InvalidCargoStatusTransitionException(current, target);
    }
}
