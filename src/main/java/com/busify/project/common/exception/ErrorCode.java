package com.busify.project.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // ================= SYSTEM ERROR (9000 - 9999) =================

    /**
     * General uncategorized exception for unexpected errors
     * <p>
     * Used when an unexpected error occurs that doesn't fit into other categories.
     * This should be used sparingly and specific error codes should be preferred.
     * </p>
     */
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Invalid key or parameter provided in request
     * <p>
     * Used when a request contains invalid keys, parameters, or malformed data
     * that cannot be processed by the system.
     * </p>
     */
    INVALID_KEY(9001, "Invalid key provided", HttpStatus.BAD_REQUEST),

    /**
     * Database connection or transaction error
     * <p>
     * Used when database operations fail due to connection issues or
     * transaction rollback scenarios.
     * </p>
     */
    DATABASE_ERROR(9002, "Database operation failed", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * External service communication error
     * <p>
     * Used when communication with external services fails or times out.
     * </p>
     */
    EXTERNAL_SERVICE_ERROR(9003, "External service communication failed", HttpStatus.SERVICE_UNAVAILABLE),

    // ================= AUTHENTICATION & AUTHORIZATION (1000 - 1099)
    // ========================

    /**
     * Authentication failed due to invalid credentials
     * <p>
     * Used when user provides incorrect username or password during login.
     * </p>
     */
    AUTHENTICATION_FAILED(1001, "Authentication failed - invalid credentials", HttpStatus.UNAUTHORIZED),

    /**
     * Access token is invalid or expired
     * <p>
     * Used when JWT token validation fails or token has expired.
     * </p>
     */
    TOKEN_INVALID(1002, "Invalid or expired token", HttpStatus.UNAUTHORIZED),

    /**
     * User does not have permission to access the resource
     * <p>
     * Used when authenticated user lacks sufficient privileges for the requested
     * operation.
     * </p>
     */
    ACCESS_DENIED(1003, "Access denied - insufficient permissions", HttpStatus.FORBIDDEN),

    /**
     * User session has expired
     * <p>
     * Used when user session times out and requires re-authentication.
     * </p>
     */
    SESSION_EXPIRED(1004, "Session expired - please login again", HttpStatus.UNAUTHORIZED),

    /**
     * Email not verified
     */
    EMAIL_NOT_VERIFIED(1005, "Email not verified", HttpStatus.UNAUTHORIZED),

    /**
     * Email already exists
     */
    EMAIL_ALREADY_EXISTS(1006, "Email already exists", HttpStatus.CONFLICT),

    /**
     * User not found
     */
    USER_NOT_FOUND(1007, "User not found", HttpStatus.NOT_FOUND),

    /**
     * Default role not found
     */
    DEFAULT_ROLE_NOT_FOUND(1008, "Default role not found", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Password reset not available
     */
    PASSWORD_RESET_NOT_AVAILABLE(1009, "Password reset not available for this user type", HttpStatus.BAD_REQUEST),

    /**
     * Invalid password reset token
     */
    INVALID_PASSWORD_RESET_TOKEN(1010, "Invalid password reset token", HttpStatus.BAD_REQUEST),

    // ================= BOOKING MANAGEMENT (1100 - 1199) ========================

    /**
     * Booking with specified ID does not exist
     */
    BOOKING_NOT_FOUND(1101, "Booking not found", HttpStatus.NOT_FOUND),

    /**
     * Booking creation failed
     */
    BOOKING_CREATION_FAILED(1102, "Booking creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Booking cancellation failed
     */
    BOOKING_CANCELLATION_FAILED(1103, "Booking cancellation failed", HttpStatus.BAD_REQUEST),

    /**
     * Invalid booking status transition
     */
    INVALID_BOOKING_STATUS(1104, "Invalid booking status transition", HttpStatus.BAD_REQUEST),

    // ================= ROUTE MANAGEMENT (1200 - 1299) =====================

    /**
     * Route with specified ID does not exist
     */
    ROUTE_NOT_FOUND(1201, "Route not found", HttpStatus.NOT_FOUND),

    /**
     * Route creation failed
     */
    ROUTE_CREATION_FAILED(1202, "Route creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Invalid route data
     */
    INVALID_ROUTE_DATA(1203, "Invalid route data", HttpStatus.BAD_REQUEST),

    // ================= BUS MANAGEMENT (1210 - 1259) =====================

    /**
     * Bus with specified ID does not exist
     */
    BUS_NOT_FOUND(1210, "Bus not found", HttpStatus.NOT_FOUND),

    /**
     * Bus creation failed
     */
    BUS_CREATION_FAILED(1211, "Bus creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Bus update failed
     */
    BUS_UPDATE_FAILED(1212, "Bus update failed", HttpStatus.BAD_REQUEST),

    /**
     * Bus deletion failed due to active trips
     */
    BUS_DELETE_FAILED_ACTIVE_TRIPS(1213, "Cannot delete bus with active trips", HttpStatus.CONFLICT),

    /**
     * Bus model not found
     */
    BUS_MODEL_NOT_FOUND(1214, "Bus model not found", HttpStatus.NOT_FOUND),

    /**
     * Seat layout not found
     */
    SEAT_LAYOUT_NOT_FOUND(1215, "Seat layout not found", HttpStatus.NOT_FOUND),

    /**
     * Invalid seat layout data
     */
    INVALID_SEAT_LAYOUT_DATA(1216, "Invalid seat layout data", HttpStatus.BAD_REQUEST),

    /**
     * Bus license plate already exists
     */
    BUS_LICENSE_PLATE_EXISTS(1217, "Bus license plate already exists", HttpStatus.CONFLICT),

    /**
     * Bus operator unauthorized to access this bus
     */
    BUS_OPERATOR_UNAUTHORIZED(1218, "Bus operator not authorized for this bus", HttpStatus.FORBIDDEN),

    /**
     * Bus status transition invalid
     */
    INVALID_BUS_STATUS_TRANSITION(1219, "Invalid bus status transition", HttpStatus.BAD_REQUEST),

    // ================= BUS OPERATOR MANAGEMENT (1300 - 1399)
    // ========================

    /**
     * Bus operator with specified ID does not exist
     */
    OPERATOR_NOT_FOUND(1301, "Bus operator not found", HttpStatus.NOT_FOUND),

    /**
     * Bus operator already exists
     */
    OPERATOR_ALREADY_EXISTS(1302, "Bus operator already exists", HttpStatus.CONFLICT),

    /**
     * Bus operator update failed
     */
    OPERATOR_UPDATE_FAILED(1303, "Bus operator update failed", HttpStatus.BAD_REQUEST),

    /**
     * Bus operator creation failed
     */
    OPERATOR_CREATION_FAILED(1304, "Bus operator creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Bus operator deletion failed
     */
    OPERATOR_DELETE_FAILED(1305, "Bus operator deletion failed", HttpStatus.BAD_REQUEST),

    /**
     * Owner user not found for bus operator
     */
    OPERATOR_OWNER_NOT_FOUND(1306, "Owner user not found for bus operator", HttpStatus.NOT_FOUND),

    /**
     * License file upload failed for bus operator
     */
    OPERATOR_LICENSE_UPLOAD_FAILED(1307, "License file upload failed", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Default role not found for bus operator
     */
    OPERATOR_DEFAULT_ROLE_NOT_FOUND(1308, "Default BUS_OPERATOR role not found", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Bus operator unauthorized action
     */
    OPERATOR_UNAUTHORIZED(1309, "Bus operator not authorized for this action", HttpStatus.FORBIDDEN),

    // ================= TRIP MANAGEMENT (1400 - 1499) =================

    /**
     * Trip with specified ID does not exist
     */
    TRIP_NOT_FOUND(1401, "Trip not found", HttpStatus.NOT_FOUND),

    /**
     * Trip creation failed
     */
    TRIP_CREATION_FAILED(1402, "Trip creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Trip is full - no available seats
     */
    TRIP_FULL(1403, "No available seats for this trip", HttpStatus.BAD_REQUEST),

    /**
     * Invalid trip schedule
     */
    INVALID_TRIP_SCHEDULE(1404, "Invalid trip schedule", HttpStatus.BAD_REQUEST),

    /**
     * Route not found for trip
     */
    TRIP_ROUTE_NOT_FOUND(1405, "Route not found", HttpStatus.NOT_FOUND),

    /**
     * Bus not found for trip
     */
    TRIP_BUS_NOT_FOUND(1406, "Bus not found", HttpStatus.NOT_FOUND),

    /**
     * Bus not owned by operator
     */
    TRIP_BUS_NOT_OWNED(1407, "Bus not owned by operator", HttpStatus.FORBIDDEN),

    /**
     * Driver not found for trip
     */
    TRIP_DRIVER_NOT_FOUND(1408, "Driver not found", HttpStatus.NOT_FOUND),

    /**
     * Seat layout not found for trip
     */
    TRIP_SEAT_LAYOUT_NOT_FOUND(1409, "Seat layout not found", HttpStatus.NOT_FOUND),

    /**
     * Trip seat generation failed
     */
    TRIP_SEAT_GENERATION_FAILED(1410, "Failed to generate trip seats", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Trip update failed
     */
    TRIP_UPDATE_FAILED(1411, "Trip update failed", HttpStatus.BAD_REQUEST),

    /**
     * Trip processing failed
     */
    TRIP_PROCESSING_FAILED(1412, "Trip processing failed", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Bus is not ACTIVE
     */
    TRIP_BUS_NOT_ACTIVE(1413, "Bus is not ACTIVE", HttpStatus.BAD_REQUEST),

    /**
     * Driver is not ACTIVE
     */
    TRIP_DRIVER_NOT_ACTIVE(1414, "Driver is not ACTIVE", HttpStatus.BAD_REQUEST),

    /**
     * Driver is already assigned to another trip
     */
    DRIVER_ALREADY_ASSIGNED(1415, "Driver is already assigned to another trip", HttpStatus.CONFLICT),

    /**
     * Bus is already assigned to another trip
     */
    BUS_ALREADY_ASSIGNED(1415, "Bus is already assigned to another trip", HttpStatus.CONFLICT),

    // ================= SEAT MANAGEMENT (1500 - 1599) ===================

    /**
     * Seat with specified ID does not exist
     */
    SEAT_NOT_FOUND(1501, "Seat not found", HttpStatus.NOT_FOUND),

    /**
     * Seat already booked
     */
    SEAT_ALREADY_BOOKED(1502, "Seat already booked", HttpStatus.CONFLICT),

    /**
     * Invalid seat selection
     */
    INVALID_SEAT_SELECTION(1503, "Invalid seat selection", HttpStatus.BAD_REQUEST),

    // ================= PAYMENT MANAGEMENT (1600 - 1699) ===================

    /**
     * Payment failed
     */
    PAYMENT_FAILED(1601, "Payment processing failed", HttpStatus.BAD_REQUEST),

    /**
     * Invalid payment amount
     */
    INVALID_PAYMENT_AMOUNT(1602, "Invalid payment amount", HttpStatus.BAD_REQUEST),

    /**
     * Payment timeout
     */
    PAYMENT_TIMEOUT(1603, "Payment session timed out", HttpStatus.REQUEST_TIMEOUT),

    /**
     * Payment not found
     */
    PAYMENT_NOT_FOUND(1604, "Payment not found", HttpStatus.NOT_FOUND),

    /**
     * Booking not found for payment
     */
    PAYMENT_BOOKING_NOT_FOUND(1605, "Booking not found", HttpStatus.NOT_FOUND),

    /**
     * Booking already paid
     */
    BOOKING_ALREADY_PAID(1606, "This booking has been successfully paid", HttpStatus.CONFLICT),

    /**
     * Payment method not supported
     */
    PAYMENT_METHOD_NOT_SUPPORTED(1607, "Payment method not supported", HttpStatus.BAD_REQUEST),

    /**
     * Payment creation failed
     */
    PAYMENT_CREATION_FAILED(1608, "Error creating payment", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * VNPay payment processing failed
     */
    VNPAY_PAYMENT_FAILED(1609, "VNPay payment processing failed", HttpStatus.BAD_REQUEST),

    /**
     * PayPal payment processing failed
     */
    PAYPAL_PAYMENT_FAILED(1610, "PayPal payment processing failed", HttpStatus.BAD_REQUEST),

    /**
     * Credit card payment not implemented
     */
    CREDIT_CARD_NOT_IMPLEMENTED(1611, "Credit Card payment not implemented", HttpStatus.NOT_IMPLEMENTED),

    /**
     * Payment transaction not found
     */
    PAYMENT_TRANSACTION_NOT_FOUND(1612, "Payment transaction not found", HttpStatus.NOT_FOUND),

    /**
     * VNPay URL generation failed
     */
    VNPAY_URL_GENERATION_FAILED(1613, "Failed to generate VNPay payment URL", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * PayPal approval URL not found
     */
    PAYPAL_APPROVAL_URL_NOT_FOUND(1614, "PayPal approval URL not found", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * VNPay hash generation failed
     */
    VNPAY_HASH_GENERATION_FAILED(1615, "Failed to generate VNPay hash", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================= PROMOTION MANAGEMENT (1700 - 1799) ===================

    /**
     * Promotion code not found
     */
    PROMOTION_NOT_FOUND(1701, "Promotion code not found", HttpStatus.NOT_FOUND),

    /**
     * Promotion code has expired
     */
    PROMOTION_EXPIRED(1702, "Promotion code has expired", HttpStatus.BAD_REQUEST),

    /**
     * Promotion code is not yet active
     */
    PROMOTION_NOT_ACTIVE(1703, "Promotion code is not yet active", HttpStatus.BAD_REQUEST),

    /**
     * Promotion usage limit exceeded
     */
    PROMOTION_USAGE_LIMIT_EXCEEDED(1704, "Promotion usage limit exceeded", HttpStatus.BAD_REQUEST),

    /**
     * Promotion already used by this user
     */
    PROMOTION_ALREADY_USED(1705, "Promotion code already used", HttpStatus.BAD_REQUEST),

    /**
     * Promotion not applicable for this booking
     */
    PROMOTION_NOT_APPLICABLE(1706, "Promotion is not applicable for this booking", HttpStatus.BAD_REQUEST),

    /**
     * Invalid promotion code format
     */
    INVALID_PROMOTION_CODE(1707, "Invalid promotion code format", HttpStatus.BAD_REQUEST),

    /**
     * Minimum order amount not met for promotion
     */
    PROMOTION_MINIMUM_AMOUNT_NOT_MET(1708, "Minimum order amount not met for this promotion", HttpStatus.BAD_REQUEST),

    /**
     * Promotion not available for user (not claimed or already used)
     */
    PROMOTION_NOT_AVAILABLE(1709, "Promotion not available for this user", HttpStatus.BAD_REQUEST),

    // ================= COMPLAINT MANAGEMENT (1800 - 1899) ===================

    /**
     * Complaint with specified ID does not exist
     */
    COMPLAINT_NOT_FOUND(1801, "Complaint not found", HttpStatus.NOT_FOUND),

    /**
     * Complaint creation failed
     */
    COMPLAINT_CREATION_FAILED(1802, "Complaint creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Complaint update failed
     */
    COMPLAINT_UPDATE_FAILED(1803, "Complaint update failed", HttpStatus.BAD_REQUEST),

    /**
     * Complaint deletion failed
     */
    COMPLAINT_DELETE_FAILED(1804, "Complaint deletion failed", HttpStatus.BAD_REQUEST),

    /**
     * Customer not found for complaint
     */
    COMPLAINT_CUSTOMER_NOT_FOUND(1805, "Customer not found for complaint", HttpStatus.NOT_FOUND),

    /**
     * Booking not found for complaint
     */
    COMPLAINT_BOOKING_NOT_FOUND(1806, "Booking not found for complaint", HttpStatus.NOT_FOUND),

    /**
     * Assigned agent not found for complaint
     */
    COMPLAINT_AGENT_NOT_FOUND(1807, "Assigned agent not found for complaint", HttpStatus.NOT_FOUND),

    /**
     * Invalid complaint status transition
     */
    COMPLAINT_INVALID_STATUS_TRANSITION(1808, "Invalid complaint status transition", HttpStatus.BAD_REQUEST),

    /**
     * Complaint already assigned to agent
     */
    COMPLAINT_ALREADY_ASSIGNED(1809, "Complaint is already assigned to an agent", HttpStatus.CONFLICT),

    /**
     * Unauthorized to access complaint
     */
    COMPLAINT_UNAUTHORIZED_ACCESS(1810, "Unauthorized to access this complaint", HttpStatus.FORBIDDEN),

    // ================= CONTRACT MANAGEMENT (1900 - 1999) ===================

    /**
     * Contract with specified ID does not exist
     */
    CONTRACT_NOT_FOUND(1901, "Contract not found", HttpStatus.NOT_FOUND),

    /**
     * Contract creation failed
     */
    CONTRACT_CREATION_FAILED(1902, "Contract creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Contract update failed
     */
    CONTRACT_UPDATE_FAILED(1903, "Contract update failed", HttpStatus.BAD_REQUEST),

    /**
     * Contract review failed
     */
    CONTRACT_REVIEW_FAILED(1904, "Contract review failed", HttpStatus.BAD_REQUEST),

    /**
     * Invalid contract status for operation
     */
    CONTRACT_INVALID_STATUS(1905, "Invalid contract status for this operation", HttpStatus.BAD_REQUEST),

    /**
     * Contract attachment upload failed
     */
    CONTRACT_ATTACHMENT_UPLOAD_FAILED(1906, "Contract attachment upload failed", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Invalid contract review action
     */
    CONTRACT_INVALID_ACTION(1907, "Invalid contract review action", HttpStatus.BAD_REQUEST),

    /**
     * Contract user creation failed
     */
    CONTRACT_USER_CREATION_FAILED(1908, "Failed to create user and bus operator from contract",
            HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Contract status transition not allowed
     */
    CONTRACT_STATUS_TRANSITION_NOT_ALLOWED(1909, "Contract status transition is not allowed", HttpStatus.BAD_REQUEST),

    /**
     * Contract already exists for this email
     */
    CONTRACT_ALREADY_EXISTS(1910, "Contract already exists for this email", HttpStatus.CONFLICT),

    /**
     * Contract attachment failed to process
     */
    CONTRACT_ATTACHMENT_FAILED(1911, "Failed to process contract attachment", HttpStatus.BAD_REQUEST),

    /**
     * Contract attachment invalid format
     */
    CONTRACT_ATTACHMENT_INVALID_FORMAT(1912, "Invalid contract attachment format", HttpStatus.BAD_REQUEST),

    /**
     * Contract attachment size exceeded
     */
    CONTRACT_ATTACHMENT_SIZE_EXCEEDED(1913, "Contract attachment size exceeded", HttpStatus.BAD_REQUEST),

    /**
     * Contract attachment not found
     */
    CONTRACT_ATTACHMENT_NOT_FOUND(1914, "Contract attachment not found", HttpStatus.NOT_FOUND),

    /**
     * Contract attachment processing failed
     */
    CONTRACT_ATTACHMENT_PROCESSING_FAILED(1915, "Failed to process contract attachment",
            HttpStatus.INTERNAL_SERVER_ERROR),

    // ================= FILE UPLOAD (1400 - 1499) =================

    /**
     * Uploaded file is empty or null
     * <p>
     * Used when file upload request contains no file data or empty file.
     * </p>
     */
    FILE_EMPTY(1401, "File is empty - please upload a valid file", HttpStatus.BAD_REQUEST),

    /**
     * File extension is not allowed
     * <p>
     * Used when uploaded file has an extension that is not permitted by the system.
     * </p>
     */
    FILE_INVALID_EXTENSION(1402, "Invalid file extension - only allowed formats are permitted", HttpStatus.BAD_REQUEST),

    /**
     * File upload operation failed
     * <p>
     * Used when file upload fails due to server issues, storage problems, or I/O
     * errors.
     * </p>
     */
    FILE_UPLOAD_FAILED(1403, "File upload failed - please try again", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * File size exceeds maximum allowed limit
     * <p>
     * Used when uploaded file exceeds the maximum size limit configured in the
     * system.
     * </p>
     */
    FILE_TOO_LARGE(1404, "File size exceeds maximum allowed limit", HttpStatus.BAD_REQUEST),

    /**
     * File download failed
     * <p>
     * Used when file download operation fails due to file not found or I/O errors.
     * </p>
     */
    FILE_DOWNLOAD_FAILED(1405, "File download failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================= INTERVIEW MANAGEMENT (1500 - 1599) ===================

    /**
     * Interview with specified ID does not exist
     * <p>
     * Used when attempting to retrieve or modify an interview that cannot be found
     * in the database.
     * </p>
     */
    INTERVIEW_NOT_FOUND(1501, "Interview not found", HttpStatus.NOT_FOUND),

    /**
     * Interviewer with specified ID does not exist
     * <p>
     * Used when attempting to assign an interviewer that cannot be found in the
     * system.
     * </p>
     */
    INTERVIEWER_NOT_FOUND(1502, "Interviewer not found", HttpStatus.NOT_FOUND),

    /**
     * Interviewee with specified ID does not exist
     * <p>
     * Used when attempting to assign an interviewee that cannot be found in the
     * system.
     * </p>
     */
    INTERVIEWEE_NOT_FOUND(1503, "Interviewee not found", HttpStatus.NOT_FOUND),

    /**
     * Interview data provided is invalid or incomplete
     * <p>
     * Used when interview creation or update fails due to invalid or missing data.
     * </p>
     */
    INVALID_INTERVIEW_DATA(1504, "Invalid interview data provided", HttpStatus.BAD_REQUEST),

    /**
     * Interviewee type is invalid or not supported
     * <p>
     * Used when interviewee type is not one of the supported types in the system.
     * </p>
     */
    INVALID_INTERVIEWEE_TYPE(1505, "Invalid interviewee type", HttpStatus.BAD_REQUEST),

    /**
     * Interview creation failed due to scheduling conflicts
     * <p>
     * Used when interview creation fails due to scheduling conflicts or resource
     * unavailability.
     * </p>
     */
    INTERVIEW_SCHEDULING_CONFLICT(1506, "Interview scheduling conflict", HttpStatus.CONFLICT),

    // ================= EVIDENCE MANAGEMENT (1600 - 1699) ===================

    /**
     * Evidence with specified ID does not exist
     * <p>
     * Used when attempting to retrieve or modify evidence that cannot be found
     * in the database.
     * </p>
     */
    EVIDENCE_NOT_FOUND(1601, "Evidence not found", HttpStatus.NOT_FOUND),

    /**
     * Evidence creation failed due to invalid data
     * <p>
     * Used when evidence creation fails due to missing required fields or invalid
     * data.
     * </p>
     */
    EVIDENCE_CREATION_FAILED(1602, "Evidence creation failed - invalid data provided", HttpStatus.BAD_REQUEST),

    /**
     * Evidence update failed due to invalid data or permissions
     * <p>
     * Used when evidence update operation fails due to invalid data or insufficient
     * permissions.
     * </p>
     */
    EVIDENCE_UPDATE_FAILED(1603, "Evidence update failed", HttpStatus.BAD_REQUEST),

    /**
     * Evidence deletion failed due to constraints or permissions
     * <p>
     * Used when evidence deletion fails due to existing dependencies or
     * insufficient permissions.
     * </p>
     */
    EVIDENCE_DELETE_FAILED(1604, "Evidence deletion failed", HttpStatus.BAD_REQUEST),

    // ================= INVESTIGATION PLAN (1700 - 1799) ===================

    /**
     * Investigation plan with specified ID does not exist
     * <p>
     * Used when attempting to retrieve or modify an investigation plan that cannot
     * be found
     * in the database.
     * </p>
     */
    INVESTIGATION_PLAN_NOT_FOUND(1701, "Investigation plan not found", HttpStatus.NOT_FOUND),

    /**
     * Investigation plan creation failed due to invalid data
     * <p>
     * Used when investigation plan creation fails due to missing required fields or
     * invalid data.
     * </p>
     */
    INVESTIGATION_PLAN_CREATION_FAILED(1702, "Investigation plan creation failed - invalid data provided",
            HttpStatus.BAD_REQUEST),

    /**
     * Investigation plan update failed due to invalid data or permissions
     * <p>
     * Used when investigation plan update operation fails due to invalid data or
     * insufficient permissions.
     * </p>
     */
    INVESTIGATION_PLAN_UPDATE_FAILED(1703, "Investigation plan update failed", HttpStatus.BAD_REQUEST),

    // ================= VALIDATION ERRORS (1800 - 1899) ===================

    /**
     * Invalid data format or structure provided
     * <p>
     * Used when request data has invalid format or structure that cannot be
     * processed.
     * </p>
     */
    INVALID_DATA_FORMAT(1801, "Invalid data format provided", HttpStatus.BAD_REQUEST),

    /**
     * Invalid request body or missing required data
     * <p>
     * Used when request body is missing, null, or contains invalid structure.
     * </p>
     */
    INVALID_REQUEST_BODY(1802, "Invalid request body provided", HttpStatus.BAD_REQUEST),

    /**
     * Invalid content type for the request
     * <p>
     * Used when request content type is not supported by the endpoint.
     * </p>
     */
    INVALID_CONTENT_TYPE(1803, "Invalid content type - unsupported format", HttpStatus.BAD_REQUEST),

    /**
     * Invalid time range provided
     * <p>
     * Used when start time is after end time or time values are invalid.
     * </p>
     */
    INVALID_TIME_RANGE(1804, "Invalid time range - end time must be after start time", HttpStatus.BAD_REQUEST),

    /**
     * Invalid location data provided
     * <p>
     * Used when location field is missing or contains invalid data.
     * </p>
     */
    INVALID_LOCATION(1805, "Invalid location data provided", HttpStatus.BAD_REQUEST),

    /**
     * Invalid parameters provided in request
     * <p>
     * Used when request parameters are missing, invalid, or out of range.
     * </p>
     */
    INVALID_PARAMETERS(1806, "Invalid parameters provided", HttpStatus.BAD_REQUEST),

    /**
     * Invalid level of trust value
     * <p>
     * Used when level of trust is not one of the accepted values.
     * </p>
     */
    INVALID_LEVEL_OF_TRUST(1807, "Invalid level of trust - must be 'a', 'b', or 'c'", HttpStatus.BAD_REQUEST),

    /**
     * Invalid question data provided
     * <p>
     * Used when question data is missing, null, or contains invalid structure.
     * </p>
     */
    INVALID_QUESTION_DATA(1808, "Invalid question data provided", HttpStatus.BAD_REQUEST),

    /**
     * Invalid file name provided
     * <p>
     * Used when file name is null, empty, or contains invalid characters.
     * </p>
     */
    INVALID_FILE_NAME(1809, "Invalid file name provided", HttpStatus.BAD_REQUEST),

    // ================= EMPLOYEE MANAGEMENT (2000 - 2099) ===================

    /**
     * Employee with specified ID does not exist
     */
    EMPLOYEE_NOT_FOUND(2001, "Employee not found", HttpStatus.NOT_FOUND),

    /**
     * Employee creation failed
     */
    EMPLOYEE_CREATION_FAILED(2002, "Employee creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Employee update failed
     */
    EMPLOYEE_UPDATE_FAILED(2003, "Employee update failed", HttpStatus.BAD_REQUEST),

    /**
     * Employee deletion failed
     */
    EMPLOYEE_DELETE_FAILED(2004, "Employee deletion failed", HttpStatus.BAD_REQUEST),

    /**
     * Employee is currently assigned to active trips
     */
    EMPLOYEE_HAS_ACTIVE_TRIPS(2005, "Employee has active trips and cannot be deleted", HttpStatus.CONFLICT),

    /**
     * Employee cannot delete themselves
     */
    EMPLOYEE_CANNOT_DELETE_SELF(2006, "Employee cannot delete their own account", HttpStatus.CONFLICT),

    /**
     * Driver with specified ID does not exist
     */
    DRIVER_NOT_FOUND(2007, "Driver not found", HttpStatus.NOT_FOUND),

    /**
     * Bus operator associated with employee not found
     */
    EMPLOYEE_BUS_OPERATOR_NOT_FOUND(2008, "Bus operator not found for employee", HttpStatus.NOT_FOUND),

    /**
     * Employee email already exists
     */
    EMPLOYEE_EMAIL_EXISTS(2009, "Employee email already exists", HttpStatus.CONFLICT),

    /**
     * Employee role not found
     */
    EMPLOYEE_ROLE_NOT_FOUND(2010, "Employee role not found", HttpStatus.NOT_FOUND),

    /**
     * Employee status is invalid
     */
    EMPLOYEE_INVALID_STATUS(2011, "Invalid employee status", HttpStatus.BAD_REQUEST),

    /**
     * Employee unauthorized access
     */
    EMPLOYEE_UNAUTHORIZED_ACCESS(2012, "Unauthorized employee access", HttpStatus.FORBIDDEN),

    // ================= NOTIFICATION MANAGEMENT (2100 - 2199) ===================

    /**
     * Notification with specified ID does not exist
     */
    NOTIFICATION_NOT_FOUND(2101, "Notification not found", HttpStatus.NOT_FOUND),

    /**
     * Notification creation failed
     */
    NOTIFICATION_CREATION_FAILED(2102, "Notification creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Notification update failed
     */
    NOTIFICATION_UPDATE_FAILED(2103, "Notification update failed", HttpStatus.BAD_REQUEST),

    /**
     * Notification deletion failed
     */
    NOTIFICATION_DELETE_FAILED(2104, "Notification deletion failed", HttpStatus.BAD_REQUEST),

    /**
     * User not found for notification
     */
    NOTIFICATION_USER_NOT_FOUND(2105, "User not found for notification", HttpStatus.NOT_FOUND),

    /**
     * Invalid notification status
     */
    NOTIFICATION_INVALID_STATUS(2106, "Invalid notification status", HttpStatus.BAD_REQUEST),

    /**
     * Notification unauthorized access
     */
    NOTIFICATION_UNAUTHORIZED_ACCESS(2107, "Unauthorized notification access", HttpStatus.FORBIDDEN),

    /**
     * PDF report generation failed
     */
    PDF_REPORT_GENERATION_FAILED(2108, "PDF report generation failed", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Notification already read
     */
    NOTIFICATION_ALREADY_READ(2109, "Notification already marked as read", HttpStatus.CONFLICT),

    /**
     * Notification already deleted
     */
    NOTIFICATION_ALREADY_DELETED(2110, "Notification already deleted", HttpStatus.CONFLICT),

    /**
     * Invalid notification type
     */
    NOTIFICATION_INVALID_TYPE(2111, "Invalid notification type", HttpStatus.BAD_REQUEST),

    /**
     * Notification processing failed
     */
    NOTIFICATION_PROCESSING_FAILED(2112, "Notification processing failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================= TICKET MANAGEMENT (2200 - 2299) ===================

    /**
     * Ticket not found
     */
    TICKET_NOT_FOUND(2201, "Ticket not found", HttpStatus.NOT_FOUND),

    /**
     * Ticket creation failed
     */
    TICKET_CREATION_FAILED(2202, "Ticket creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Ticket update failed
     */
    TICKET_UPDATE_FAILED(2203, "Ticket update failed", HttpStatus.BAD_REQUEST),

    /**
     * Ticket deletion failed
     */
    TICKET_DELETE_FAILED(2204, "Ticket deletion failed", HttpStatus.BAD_REQUEST),

    /**
     * Invalid ticket status
     */
    TICKET_INVALID_STATUS(2205, "Invalid ticket status", HttpStatus.BAD_REQUEST),

    /**
     * Ticket unauthorized access
     */
    TICKET_UNAUTHORIZED_ACCESS(2206, "Unauthorized ticket access", HttpStatus.FORBIDDEN),

    /**
     * Ticket already cancelled
     */
    TICKET_ALREADY_CANCELLED(2207, "Ticket already cancelled", HttpStatus.CONFLICT),

    /**
     * Ticket processing failed
     */
    TICKET_PROCESSING_FAILED(2208, "Ticket processing failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // ================= USER MANAGEMENT (2300 - 2399) ===================

    /**
     * User not found in user management
     */
    USER_PROFILE_NOT_FOUND(2301, "User profile not found", HttpStatus.NOT_FOUND),

    /**
     * User is not a profile type
     */
    USER_NOT_PROFILE(2302, "User is not a profile type", HttpStatus.BAD_REQUEST),

    /**
     * Role not found in user management
     */
    USER_ROLE_NOT_FOUND(2303, "Role not found", HttpStatus.NOT_FOUND),

    /**
     * User already exists with email
     */
    USER_EMAIL_ALREADY_EXISTS(2304, "User already exists with email", HttpStatus.CONFLICT),

    // ================= REFUND MANAGEMENT (2400 - 2499) ===================

    /**
     * Refund with specified ID does not exist
     */
    REFUND_NOT_FOUND(2401, "Refund not found", HttpStatus.NOT_FOUND),

    /**
     * Refund creation failed
     */
    REFUND_CREATION_FAILED(2402, "Refund creation failed", HttpStatus.BAD_REQUEST),

    /**
     * Refund processing failed
     */
    REFUND_PROCESSING_FAILED(2403, "Refund processing failed", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * Refund strategy not found for payment method
     */
    REFUND_STRATEGY_NOT_FOUND(2404, "Refund strategy not found for payment method", HttpStatus.NOT_IMPLEMENTED),

    /**
     * Refund not allowed for this payment
     */
    REFUND_NOT_ALLOWED(2405, "Refund not allowed for this payment", HttpStatus.BAD_REQUEST),

    /**
     * Refund amount exceeds payment amount
     */
    REFUND_AMOUNT_EXCEEDS_PAYMENT(2406, "Refund amount exceeds payment amount", HttpStatus.BAD_REQUEST),

    /**
     * Payment not eligible for refund
     */
    PAYMENT_NOT_ELIGIBLE_FOR_REFUND(2407, "Payment is not eligible for refund", HttpStatus.BAD_REQUEST),

    /**
     * Refund already processed
     */
    REFUND_ALREADY_PROCESSED(2408, "Refund has already been processed", HttpStatus.CONFLICT),

    /**
     * Refund policy violation
     */
    REFUND_POLICY_VIOLATION(2409, "Refund request violates refund policy", HttpStatus.BAD_REQUEST),

    ;

    /**
     * Unique numeric error code
     */
    private final int code;

    /**
     * Human-readable error message
     */
    private final String message;

    /**
     * HTTP status code for REST API responses
     */
    private final HttpStatus statusCode;

    /**
     * Constructs an ErrorCode enum value
     * <p>
     * Creates an error code with specified numeric code, message, and HTTP status.
     * </p>
     *
     * @param code       Unique numeric identifier for the error
     * @param message    Human-readable error message
     * @param statusCode HTTP status code for REST API responses
     */
    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    /**
     * Retrieves the numeric error code
     * <p>
     * Returns the unique numeric identifier associated with this error code.
     * </p>
     *
     * @return The numeric error code
     */
    public int getCode() {
        return code;
    }

    /**
     * Retrieves the error message
     * <p>
     * Returns the human-readable error message associated with this error code.
     * </p>
     *
     * @return The error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieves the HTTP status code
     * <p>
     * Returns the HTTP status code that should be used in REST API responses
     * for this error condition.
     * </p>
     *
     * @return The HTTP status code
     */
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
