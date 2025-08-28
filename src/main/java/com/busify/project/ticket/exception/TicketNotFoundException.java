package com.busify.project.ticket.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a ticket is not found in the system
 */
public class TicketNotFoundException extends AppException {

    public TicketNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Ticket not found by ID
     */
    public static TicketNotFoundException notFound() {
        return new TicketNotFoundException(ErrorCode.TICKET_NOT_FOUND);
    }
}
