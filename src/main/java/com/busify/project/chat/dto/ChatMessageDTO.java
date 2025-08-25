package com.busify.project.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageDTO {
    private String content;
    private String sender;
    private String recipient; // Dùng cho chat 1-1
    private MessageType type;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

}