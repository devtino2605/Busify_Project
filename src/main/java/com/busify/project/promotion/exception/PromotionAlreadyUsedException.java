package com.busify.project.promotion.exception;

public class PromotionAlreadyUsedException extends RuntimeException {
    public PromotionAlreadyUsedException(String message) {
        super(message);
    }
}
