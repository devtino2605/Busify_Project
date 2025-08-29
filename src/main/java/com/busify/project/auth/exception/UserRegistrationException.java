package com.busify.project.auth.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when user registration operations fail
 */
public class UserRegistrationException extends AppException {

    public UserRegistrationException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Email already exists during registration
     */
    public static UserRegistrationException emailAlreadyExists() {
        return new UserRegistrationException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    /**
     * Default role not found during registration
     */
    public static UserRegistrationException defaultRoleNotFound() {
        return new UserRegistrationException(ErrorCode.DEFAULT_ROLE_NOT_FOUND);
    }
}
