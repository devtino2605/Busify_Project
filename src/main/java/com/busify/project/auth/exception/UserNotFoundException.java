package com.busify.project.auth.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a user is not found
 */
public class UserNotFoundException extends AppException {

    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * User not found by email or ID
     */
    public static UserNotFoundException notFound() {
        return new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
    }
}
