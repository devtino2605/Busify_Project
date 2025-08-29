package com.busify.project.trip.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a trip or trip-related entity is not found
 */
public class TripNotFoundException extends AppException {

    public TripNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Trip not found by ID
     */
    public static TripNotFoundException tripNotFound() {
        return new TripNotFoundException(ErrorCode.TRIP_NOT_FOUND);
    }

    /**
     * Route not found for trip
     */
    public static TripNotFoundException routeNotFound() {
        return new TripNotFoundException(ErrorCode.TRIP_ROUTE_NOT_FOUND);
    }

    /**
     * Bus not found for trip
     */
    public static TripNotFoundException busNotFound() {
        return new TripNotFoundException(ErrorCode.TRIP_BUS_NOT_FOUND);
    }

    /**
     * Driver not found for trip
     */
    public static TripNotFoundException driverNotFound() {
        return new TripNotFoundException(ErrorCode.TRIP_DRIVER_NOT_FOUND);
    }

    /**
     * Seat layout not found for trip
     */
    public static TripNotFoundException seatLayoutNotFound() {
        return new TripNotFoundException(ErrorCode.TRIP_SEAT_LAYOUT_NOT_FOUND);
    }
}
