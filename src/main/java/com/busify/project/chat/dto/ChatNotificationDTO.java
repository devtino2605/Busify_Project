package com.busify.project.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotificationDTO {
    private String roomId; // ID phòng chat
    private String sender; // Người gửi (email)
    private String contentPreview; // Preview nội dung (ví dụ: 50 ký tự đầu)
    private String type; // Loại tin nhắn (CHAT, SYSTEM_ASSIGN, etc.)
    private String timestamp; // Thời gian (ISO string)
}
