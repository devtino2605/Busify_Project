package com.busify.project.trip.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when trip operations fail
 */
public class TripOperationException extends AppException {

    public TripOperationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TripOperationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * Trip creation failed
     */
    public static TripOperationException creationFailed() {
        return new TripOperationException(ErrorCode.TRIP_CREATION_FAILED);
    }

    /**
     * Trip creation failed with cause
     */
    public static TripOperationException creationFailed(Throwable cause) {
        return new TripOperationException(ErrorCode.TRIP_CREATION_FAILED, cause);
    }

    /**
     * Trip update failed
     */
    public static TripOperationException updateFailed() {
        return new TripOperationException(ErrorCode.TRIP_UPDATE_FAILED);
    }

    /**
     * Trip update failed with cause
     */
    public static TripOperationException updateFailed(Throwable cause) {
        return new TripOperationException(ErrorCode.TRIP_UPDATE_FAILED, cause);
    }

    /**
     * Trip processing failed
     */
    public static TripOperationException processingFailed() {
        return new TripOperationException(ErrorCode.TRIP_PROCESSING_FAILED);
    }

    /**
     * Trip processing failed with cause
     */
    public static TripOperationException processingFailed(Throwable cause) {
        return new TripOperationException(ErrorCode.TRIP_PROCESSING_FAILED, cause);
    }

    /**
     * Trip seat generation failed
     */
    public static TripOperationException seatGenerationFailed(Throwable cause) {
        return new TripOperationException(ErrorCode.TRIP_SEAT_GENERATION_FAILED, cause);
    }
}
