package com.busify.project.contract.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when contract attachment operations fail
 * <p>
 * This exception is used when contract attachment operations such as
 * file upload, processing, or validation fail.
 * </p>
 */
public class ContractAttachmentException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ContractAttachmentException with default error code
     */
    public ContractAttachmentException() {
        super(ErrorCode.CONTRACT_ATTACHMENT_UPLOAD_FAILED);
    }

    /**
     * Creates a new ContractAttachmentException with custom message
     * 
     * @param message Custom error message
     */
    public ContractAttachmentException(String message) {
        super(ErrorCode.CONTRACT_ATTACHMENT_UPLOAD_FAILED, message);
    }

    /**
     * Creates a new ContractAttachmentException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public ContractAttachmentException(String message, Throwable cause) {
        super(ErrorCode.CONTRACT_ATTACHMENT_UPLOAD_FAILED, message, cause);
    }

    /**
     * Creates a new ContractAttachmentException with specific error code
     * 
     * @param errorCode Specific error code for the attachment failure
     * @param message   Custom error message
     */
    public ContractAttachmentException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new ContractAttachmentException with specific error code and cause
     * 
     * @param errorCode Specific error code for the attachment failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public ContractAttachmentException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new ContractAttachmentException for file upload failure
     * 
     * @param filename The name of the file that failed to upload
     * @param cause    The underlying cause of the failure
     * @return ContractAttachmentException instance
     */
    public static ContractAttachmentException uploadFailed(String filename, Throwable cause) {
        return new ContractAttachmentException(ErrorCode.CONTRACT_ATTACHMENT_UPLOAD_FAILED,
                "Failed to upload attachment: " + filename, cause);
    }

    /**
     * Creates a new ContractAttachmentException for invalid file format
     * 
     * @param filename       The name of the invalid file
     * @param expectedFormat The expected file format
     * @return ContractAttachmentException instance
     */
    public static ContractAttachmentException invalidFormat(String filename, String expectedFormat) {
        return new ContractAttachmentException(ErrorCode.CONTRACT_ATTACHMENT_INVALID_FORMAT,
                "Invalid file format for " + filename + ". Expected: " + expectedFormat);
    }

    /**
     * Creates a new ContractAttachmentException for file size limit exceeded
     * 
     * @param filename The name of the file
     * @param maxSize  The maximum allowed file size
     * @return ContractAttachmentException instance
     */
    public static ContractAttachmentException sizeExceeded(String filename, String maxSize) {
        return new ContractAttachmentException(ErrorCode.CONTRACT_ATTACHMENT_SIZE_EXCEEDED,
                "File size exceeded for " + filename + ". Maximum allowed: " + maxSize);
    }

    /**
     * Creates a new ContractAttachmentException for attachment not found
     * 
     * @param attachmentId The ID of the attachment that was not found
     * @return ContractAttachmentException instance
     */
    public static ContractAttachmentException attachmentNotFound(String attachmentId) {
        return new ContractAttachmentException(ErrorCode.CONTRACT_ATTACHMENT_NOT_FOUND,
                "Attachment with ID " + attachmentId + " not found");
    }

    /**
     * Creates a new ContractAttachmentException for processing failure
     * 
     * @param filename  The name of the file that failed to process
     * @param operation The operation that failed
     * @param cause     The underlying cause of the failure
     * @return ContractAttachmentException instance
     */
    public static ContractAttachmentException processingFailed(String filename, String operation, Throwable cause) {
        return new ContractAttachmentException(ErrorCode.CONTRACT_ATTACHMENT_PROCESSING_FAILED,
                "Failed to " + operation + " attachment: " + filename, cause);
    }

    /**
     * Creates a new ContractAttachmentException for cloudinary upload failure
     * 
     * @param filename The name of the file that failed to upload to cloudinary
     * @param cause    The underlying cause of the failure
     * @return ContractAttachmentException instance
     */
    public static ContractAttachmentException cloudinaryUploadFailed(String filename, Throwable cause) {
        return new ContractAttachmentException(ErrorCode.CONTRACT_ATTACHMENT_UPLOAD_FAILED,
                "Failed to upload attachment to cloudinary: " + filename, cause);
    }
}
