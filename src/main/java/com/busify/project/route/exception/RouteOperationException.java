package com.busify.project.route.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

public class RouteOperationException extends AppException {
    public RouteOperationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static RouteOperationException sameStartEndLocation() {
        return new RouteOperationException(ErrorCode.SAME_START_END_LOCATION);
    }

    public static RouteOperationException routeAlreadyExists() {
        return new RouteOperationException(ErrorCode.ROUTE_ALREADY_EXISTS);
    }

    public static RouteOperationException routeNotFound() {
        return new RouteOperationException(ErrorCode.ROUTE_NOT_FOUND);
    }
}
