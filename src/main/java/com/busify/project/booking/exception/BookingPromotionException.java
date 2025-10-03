package com.busify.project.booking.exception;

import com.busify.project.common.exception.AppException;
import com.busify.project.common.exception.ErrorCode;

/**
 * Exception thrown when there are issues with promotion codes during booking
 * <p>
 * This exception is used for various promotion-related errors such as
 * invalid codes, expired promotions, usage limits, etc.
 * </p>
 */
public class BookingPromotionException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new BookingPromotionException with specific error code
     * 
     * @param errorCode Specific promotion error code
     */
    public BookingPromotionException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * Creates a new BookingPromotionException with custom message
     * 
     * @param errorCode Specific promotion error code
     * @param message   Custom error message
     */
    public BookingPromotionException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Creates a new BookingPromotionException for promotion not found
     * 
     * @param promotionCode The promotion code that was not found
     */
    public static BookingPromotionException promotionNotFound(String promotionCode) {
        return new BookingPromotionException(ErrorCode.PROMOTION_NOT_FOUND,
                "Promotion code '" + promotionCode + "' not found");
    }

    /**
     * Creates a new BookingPromotionException for expired promotion
     * 
     * @param promotionCode The expired promotion code
     */
    public static BookingPromotionException promotionExpired(String promotionCode) {
        return new BookingPromotionException(ErrorCode.PROMOTION_EXPIRED,
                "Promotion code '" + promotionCode + "' has expired");
    }

    /**
     * Creates a new BookingPromotionException for promotion not yet active
     * 
     * @param promotionCode The promotion code that is not yet active
     */
    public static BookingPromotionException promotionNotActive(String promotionCode) {
        return new BookingPromotionException(ErrorCode.PROMOTION_NOT_ACTIVE,
                "Promotion code '" + promotionCode + "' is not yet active");
    }

    /**
     * Creates a new BookingPromotionException for already used promotion
     * 
     * @param promotionCode The promotion code that was already used
     */
    public static BookingPromotionException promotionAlreadyUsed(String promotionCode) {
        return new BookingPromotionException(ErrorCode.PROMOTION_ALREADY_USED,
                "Promotion code '" + promotionCode + "' has already been used");
    }

    /**
     * Creates a new BookingPromotionException for usage limit exceeded
     * 
     * @param promotionCode The promotion code that exceeded usage limit
     */
    public static BookingPromotionException usageLimitExceeded(String promotionCode) {
        return new BookingPromotionException(ErrorCode.PROMOTION_USAGE_LIMIT_EXCEEDED,
                "Usage limit exceeded for promotion code '" + promotionCode + "'");
    }

    /**
     * Creates a new BookingPromotionException for minimum amount not met
     * 
     * @param promotionCode The promotion code
     * @param minimumAmount The minimum required amount
     * @param currentAmount The current booking amount
     */
    public static BookingPromotionException minimumAmountNotMet(String promotionCode, double minimumAmount,
            double currentAmount) {
        return new BookingPromotionException(ErrorCode.PROMOTION_MINIMUM_AMOUNT_NOT_MET,
                "Minimum amount of " + minimumAmount + " not met for promotion '" + promotionCode
                        + "'. Current amount: " + currentAmount);
    }

    /**
     * Creates a new BookingPromotionException for promotion not available for user
     * 
     * @param promotionCode The promotion code that is not available for this user
     */
    public static BookingPromotionException promotionNotAvailable(String promotionCode) {
        return new BookingPromotionException(ErrorCode.PROMOTION_NOT_AVAILABLE,
                "Promotion code '" + promotionCode + "' is not available for this user. Please claim it first or check if you have already used it.");
    }

    /**
     * Creates a new BookingPromotionException for promotion not applicable
     * 
     * @param promotionCode The promotion code
     * @param reason        The reason why it's not applicable
     */
    public static BookingPromotionException promotionNotApplicable(String promotionCode, String reason) {
        return new BookingPromotionException(ErrorCode.PROMOTION_NOT_APPLICABLE,
                "Promotion code '" + promotionCode + "' is not applicable: " + reason);
    }

    /**
     * Creates a new BookingPromotionException with custom message and cause
     * 
     * @param errorCode Specific promotion error code
     * @param message   Custom error message
     * @param cause     The underlying cause of this exception
     */
    public BookingPromotionException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}