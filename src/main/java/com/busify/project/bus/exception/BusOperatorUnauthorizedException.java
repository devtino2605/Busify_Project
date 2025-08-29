package com.busify.project.bus.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when a bus operator attempts to access buses they don't own
 * <p>
 * This exception is used when a bus operator tries to access, modify, or delete
 * buses that don't belong to their company/operator.
 * </p>
 */
public class BusOperatorUnauthorizedException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusOperatorUnauthorizedException with default error code
     */
    public BusOperatorUnauthorizedException() {
        super(ErrorCode.BUS_OPERATOR_UNAUTHORIZED, "Bus operator not authorized for this operation");
    }

    /**
     * Creates a new BusOperatorUnauthorizedException with custom message
     * 
     * @param message Custom error message
     */
    public BusOperatorUnauthorizedException(String message) {
        super(ErrorCode.BUS_OPERATOR_UNAUTHORIZED, message);
    }

    /**
     * Creates a new BusOperatorUnauthorizedException for specific bus access
     * 
     * @param operatorId The ID of the unauthorized operator
     * @param busId      The ID of the bus they tried to access
     */
    public BusOperatorUnauthorizedException(Long operatorId, Long busId) {
        super(ErrorCode.BUS_OPERATOR_UNAUTHORIZED,
                "Operator " + operatorId + " is not authorized to access bus " + busId);
    }

    /**
     * Creates a new BusOperatorUnauthorizedException for operation on bus
     * 
     * @param operatorId The ID of the unauthorized operator
     * @param busId      The ID of the bus
     * @param operation  The operation they tried to perform
     */
    public BusOperatorUnauthorizedException(Long operatorId, Long busId, String operation) {
        super(ErrorCode.BUS_OPERATOR_UNAUTHORIZED,
                "Operator " + operatorId + " is not authorized to " + operation + " bus " + busId);
    }

    /**
     * Creates a new BusOperatorUnauthorizedException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusOperatorUnauthorizedException(String message, Throwable cause) {
        super(ErrorCode.BUS_OPERATOR_UNAUTHORIZED, message, cause);
    }
}
