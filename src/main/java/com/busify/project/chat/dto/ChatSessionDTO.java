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
public class ChatSessionDTO {
    private String id; // roomId
    private String customerName; // fullName của người còn lại
    private String customerEmail; // email của người còn lại
    private String avatar; // avatar của người còn lại (nếu có, giả sử từ Profile)
    private String lastMessage; // nội dung tin nhắn cuối
    private LocalDateTime lastMessageTime; // thời gian tin nhắn cuối
}