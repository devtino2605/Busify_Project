package com.busify.project.bus_operator.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when bus operator license file operations fail
 * <p>
 * This exception is used when license file upload, download, or deletion
 * operations fail due to file system or cloud storage issues.
 * </p>
 */
public class BusOperatorLicenseException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BusOperatorLicenseException with default error code
     */
    public BusOperatorLicenseException() {
        super(ErrorCode.OPERATOR_LICENSE_UPLOAD_FAILED);
    }

    /**
     * Creates a new BusOperatorLicenseException with custom message
     * 
     * @param message Custom error message
     */
    public BusOperatorLicenseException(String message) {
        super(ErrorCode.OPERATOR_LICENSE_UPLOAD_FAILED, message);
    }

    /**
     * Creates a new BusOperatorLicenseException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public BusOperatorLicenseException(String message, Throwable cause) {
        super(ErrorCode.OPERATOR_LICENSE_UPLOAD_FAILED, message, cause);
    }

    /**
     * Creates a new BusOperatorLicenseException with specific error code
     * 
     * @param errorCode Specific error code for the license operation failure
     * @param message   Custom error message
     */
    public BusOperatorLicenseException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BusOperatorLicenseException for upload failures
     * 
     * @param fileName The name of the file that failed to upload
     * @param cause    The underlying cause of the upload failure
     * @return BusOperatorLicenseException instance
     */
    public static BusOperatorLicenseException uploadFailed(String fileName, Throwable cause) {
        return new BusOperatorLicenseException("Failed to upload license file: " + fileName, cause);
    }

    /**
     * Creates a new BusOperatorLicenseException for upload failures with message
     * 
     * @param message The error message from the upload service
     * @return BusOperatorLicenseException instance
     */
    public static BusOperatorLicenseException uploadFailed(String message) {
        return new BusOperatorLicenseException("Failed to upload license file: " + message);
    }

    /**
     * Creates a new BusOperatorLicenseException for delete failures
     * 
     * @param publicId The public ID of the file that failed to delete
     * @param cause    The underlying cause of the deletion failure
     * @return BusOperatorLicenseException instance
     */
    public static BusOperatorLicenseException deleteFailed(String publicId, Throwable cause) {
        return new BusOperatorLicenseException("Failed to delete license file with ID: " + publicId, cause);
    }

    /**
     * Creates a new BusOperatorLicenseException for invalid file format
     * 
     * @param fileName        The name of the invalid file
     * @param expectedFormats The expected file formats
     * @return BusOperatorLicenseException instance
     */
    public static BusOperatorLicenseException invalidFormat(String fileName, String expectedFormats) {
        return new BusOperatorLicenseException("Invalid license file format for " + fileName +
                ". Expected formats: " + expectedFormats);
    }

    /**
     * Creates a new BusOperatorLicenseException for file size limit exceeded
     * 
     * @param fileName The name of the oversized file
     * @param maxSize  The maximum allowed file size
     * @return BusOperatorLicenseException instance
     */
    public static BusOperatorLicenseException fileTooLarge(String fileName, String maxSize) {
        return new BusOperatorLicenseException("License file " + fileName +
                " exceeds maximum size limit of " + maxSize);
    }
}
