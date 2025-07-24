package com.busify.project.common.exception;

import lombok.Getter;

@Getter
public class BusifyException extends RuntimeException{
    private final int statusCode;
    public BusifyException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
