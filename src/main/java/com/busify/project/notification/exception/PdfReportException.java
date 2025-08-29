package com.busify.project.notification.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when PDF report generation operations fail
 * <p>
 * This exception is used when PDF report generation operations fail due to
 * template errors, data processing issues, or file generation problems.
 * </p>
 */
public class PdfReportException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new PdfReportException with default error code
     */
    public PdfReportException() {
        super(ErrorCode.PDF_REPORT_GENERATION_FAILED);
    }

    /**
     * Creates a new PdfReportException with custom message
     * 
     * @param message Custom error message
     */
    public PdfReportException(String message) {
        super(ErrorCode.PDF_REPORT_GENERATION_FAILED, message);
    }

    /**
     * Creates a new PdfReportException with custom message and cause
     * 
     * @param message Custom error message
     * @param cause   The underlying cause of this exception
     */
    public PdfReportException(String message, Throwable cause) {
        super(ErrorCode.PDF_REPORT_GENERATION_FAILED, message, cause);
    }

    /**
     * Creates a new PdfReportException with specific error code
     * 
     * @param errorCode Specific error code for the PDF generation failure
     * @param message   Custom error message
     */
    public PdfReportException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new PdfReportException with specific error code and cause
     * 
     * @param errorCode Specific error code for the PDF generation failure
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public PdfReportException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Creates a new PdfReportException for general PDF generation failure
     * 
     * @param cause The underlying cause of the failure
     * @return PdfReportException instance
     */
    public static PdfReportException generationFailed(Throwable cause) {
        return new PdfReportException("Không thể tạo PDF báo cáo", cause);
    }

    /**
     * Creates a new PdfReportException for HTML template processing failure
     * 
     * @param templateName The name of the template that failed
     * @param cause        The underlying cause of the failure
     * @return PdfReportException instance
     */
    public static PdfReportException templateProcessingFailed(String templateName, Throwable cause) {
        return new PdfReportException("Failed to process HTML template: " + templateName, cause);
    }

    /**
     * Creates a new PdfReportException for data formatting failure
     * 
     * @param dataType The type of data that failed formatting
     * @param cause    The underlying cause of the failure
     * @return PdfReportException instance
     */
    public static PdfReportException dataFormattingFailed(String dataType, Throwable cause) {
        return new PdfReportException("Failed to format data for PDF: " + dataType, cause);
    }

    /**
     * Creates a new PdfReportException for file output failure
     * 
     * @param filename The filename that failed to be written
     * @param cause    The underlying cause of the failure
     * @return PdfReportException instance
     */
    public static PdfReportException fileOutputFailed(String filename, Throwable cause) {
        return new PdfReportException("Failed to write PDF file: " + filename, cause);
    }

    /**
     * Creates a new PdfReportException for invalid report data
     * 
     * @param reason The reason why the data is invalid
     * @return PdfReportException instance
     */
    public static PdfReportException invalidReportData(String reason) {
        return new PdfReportException("Invalid report data: " + reason);
    }

    /**
     * Creates a new PdfReportException for missing report data
     * 
     * @param missingData The data that is missing
     * @return PdfReportException instance
     */
    public static PdfReportException missingReportData(String missingData) {
        return new PdfReportException("Missing report data: " + missingData);
    }

    /**
     * Creates a new PdfReportException for conversion failure
     * 
     * @param fromFormat The source format
     * @param toFormat   The target format
     * @param cause      The underlying cause of the failure
     * @return PdfReportException instance
     */
    public static PdfReportException conversionFailed(String fromFormat, String toFormat, Throwable cause) {
        return new PdfReportException("Failed to convert from " + fromFormat + " to " + toFormat, cause);
    }

    /**
     * Creates a new PdfReportException for resource loading failure
     * 
     * @param resourceName The name of the resource that failed to load
     * @param cause        The underlying cause of the failure
     * @return PdfReportException instance
     */
    public static PdfReportException resourceLoadingFailed(String resourceName, Throwable cause) {
        return new PdfReportException("Failed to load resource: " + resourceName, cause);
    }
}
