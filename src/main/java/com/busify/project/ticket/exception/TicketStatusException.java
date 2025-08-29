package com.busify.project.ticket.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when there are issues with ticket status operations
 */
public class TicketStatusException extends AppException {

    public TicketStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Invalid ticket status
     */
    public static TicketStatusException invalidStatus() {
        return new TicketStatusException(ErrorCode.TICKET_INVALID_STATUS);
    }

    /**
     * Ticket already cancelled
     */
    public static TicketStatusException alreadyCancelled() {
        return new TicketStatusException(ErrorCode.TICKET_ALREADY_CANCELLED);
    }
}
