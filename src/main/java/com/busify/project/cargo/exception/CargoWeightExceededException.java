package com.busify.project.cargo.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

import java.math.BigDecimal;

/**
 * CargoWeightExceededException
 * 
 * Exception thrown when cargo weight exceeds trip capacity
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
public class CargoWeightExceededException extends AppException {

    private final BigDecimal requestedWeight;
    private final BigDecimal availableWeight;
    private final Long tripId;

    public CargoWeightExceededException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.requestedWeight = null;
        this.availableWeight = null;
        this.tripId = null;
    }

    public CargoWeightExceededException(Long tripId, BigDecimal requestedWeight, BigDecimal availableWeight) {
        super(ErrorCode.CARGO_WEIGHT_EXCEEDED,
                String.format(
                        "Chuyến xe (ID: %d) không đủ chỗ cho hàng. " +
                                "Yêu cầu: %.2f kg, Còn lại: %.2f kg",
                        tripId, requestedWeight, availableWeight));
        this.tripId = tripId;
        this.requestedWeight = requestedWeight;
        this.availableWeight = availableWeight;
    }

    public BigDecimal getRequestedWeight() {
        return requestedWeight;
    }

    public BigDecimal getAvailableWeight() {
        return availableWeight;
    }

    public Long getTripId() {
        return tripId;
    }

    /**
     * Create weight exceeded exception
     */
    public static CargoWeightExceededException exceeded(Long tripId, BigDecimal requested, BigDecimal available) {
        return new CargoWeightExceededException(tripId, requested, available);
    }
}
