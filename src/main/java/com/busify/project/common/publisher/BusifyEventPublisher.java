package com.busify.project.common.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.busify.project.common.event.BusifyEvent;

@Component
@RequiredArgsConstructor
public class BusifyEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishEvent(BusifyEvent event) {
        publisher.publishEvent(event);
    }
}
