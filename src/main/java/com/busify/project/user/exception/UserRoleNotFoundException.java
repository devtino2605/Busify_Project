package com.busify.project.user.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a role is not found in user management
 */
public class UserRoleNotFoundException extends AppException {

    public UserRoleNotFoundException() {
        super(ErrorCode.USER_ROLE_NOT_FOUND);
    }

    public static UserRoleNotFoundException notFound() {
        return new UserRoleNotFoundException();
    }
}
