package com.busify.project.user.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a user is not a profile type
 */
public class UserNotProfileException extends AppException {

    public UserNotProfileException() {
        super(ErrorCode.USER_NOT_PROFILE);
    }

    public static UserNotProfileException notProfile() {
        return new UserNotProfileException();
    }
}
