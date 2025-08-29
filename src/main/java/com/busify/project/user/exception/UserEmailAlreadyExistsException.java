package com.busify.project.user.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a user already exists with the same email
 */
public class UserEmailAlreadyExistsException extends AppException {

    public UserEmailAlreadyExistsException() {
        super(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
    }

    public static UserEmailAlreadyExistsException alreadyExists() {
        return new UserEmailAlreadyExistsException();
    }
}
