package com.busify.project.ticket.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when ticket processing operations fail
 */
public class TicketProcessingException extends AppException {

    public TicketProcessingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TicketProcessingException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * General ticket processing failed
     */
    public static TicketProcessingException processingFailed() {
        return new TicketProcessingException(ErrorCode.TICKET_PROCESSING_FAILED);
    }

    /**
     * Ticket processing failed with cause
     */
    public static TicketProcessingException processingFailed(Throwable cause) {
        return new TicketProcessingException(ErrorCode.TICKET_PROCESSING_FAILED, cause);
    }

    /**
     * Ticket deletion failed
     */
    public static TicketProcessingException deletionFailed() {
        return new TicketProcessingException(ErrorCode.TICKET_DELETE_FAILED);
    }

    /**
     * Ticket deletion failed with cause
     */
    public static TicketProcessingException deletionFailed(Throwable cause) {
        return new TicketProcessingException(ErrorCode.TICKET_DELETE_FAILED, cause);
    }

    /**
     * Ticket creation failed
     */
    public static TicketProcessingException creationFailed() {
        return new TicketProcessingException(ErrorCode.TICKET_CREATION_FAILED);
    }

    /**
     * Ticket creation failed with cause
     */
    public static TicketProcessingException creationFailed(Throwable cause) {
        return new TicketProcessingException(ErrorCode.TICKET_CREATION_FAILED, cause);
    }

    /**
     * Ticket update failed
     */
    public static TicketProcessingException updateFailed() {
        return new TicketProcessingException(ErrorCode.TICKET_UPDATE_FAILED);
    }

    /**
     * Ticket update failed with cause
     */
    public static TicketProcessingException updateFailed(Throwable cause) {
        return new TicketProcessingException(ErrorCode.TICKET_UPDATE_FAILED, cause);
    }
}
