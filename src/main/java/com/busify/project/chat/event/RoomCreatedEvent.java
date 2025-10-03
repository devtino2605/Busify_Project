package com.busify.project.chat.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RoomCreatedEvent extends ApplicationEvent {
    private final String roomId;
    private final String userEmail;

    public RoomCreatedEvent(Object source, String roomId, String userEmail) {
        super(source);
        this.roomId = roomId;
        this.userEmail = userEmail;
    }
}