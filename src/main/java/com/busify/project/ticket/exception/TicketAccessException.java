package com.busify.project.ticket.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when there are unauthorized access attempts to tickets
 */
public class TicketAccessException extends AppException {

    public TicketAccessException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Unauthorized ticket access
     */
    public static TicketAccessException unauthorizedAccess() {
        return new TicketAccessException(ErrorCode.TICKET_UNAUTHORIZED_ACCESS);
    }
}
