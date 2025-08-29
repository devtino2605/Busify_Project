package com.busify.project.user.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a user profile is not found
 */
public class UserProfileNotFoundException extends AppException {

    public UserProfileNotFoundException() {
        super(ErrorCode.USER_PROFILE_NOT_FOUND);
    }

    public static UserProfileNotFoundException notFound() {
        return new UserProfileNotFoundException();
    }
}
