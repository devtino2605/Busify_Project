package com.busify.project.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String content;
    private String sender;
    private String recipient; // Dùng cho chat 1-1
    private MessageType type;
    private String roomId;
    private LocalDateTime timestamp;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        SYSTEM_ASSIGN // Thêm loại tin nhắn mới
    }

}