package com.busify.project.trip.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when there are unauthorized access attempts to trip
 * resources
 */
public class TripAccessException extends AppException {

    public TripAccessException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Bus not owned by operator
     */
    public static TripAccessException busNotOwned() {
        return new TripAccessException(ErrorCode.TRIP_BUS_NOT_OWNED);
    }
}
