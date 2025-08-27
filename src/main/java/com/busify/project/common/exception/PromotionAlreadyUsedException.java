package com.busify.project.common.exception;

public class PromotionAlreadyUsedException extends RuntimeException {
    public PromotionAlreadyUsedException(String message) {
        super(message);
    }
}
