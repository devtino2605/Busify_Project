package com.busify.project.common.event;

import org.springframework.context.ApplicationEvent;

public abstract class BusifyEvent extends ApplicationEvent {

    private String message;

    public BusifyEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
