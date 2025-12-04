package com.busify.project.ai_chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIChatResponseDTO {
    private Long id;
    private String message;
    private String role; // USER, ASSISTANT, SYSTEM
    private LocalDateTime timestamp;
    private boolean shouldTransferToHuman;
    private String transferReason;

    /**
     * Nguồn gốc của message AI
     * - GEMINI_AI: Response từ Google Gemini API thực sự
     * - FALLBACK: Response mặc định khi AI không khả dụng
     * - WELCOME: Tin nhắn chào mừng tự động
     * - ERROR: Response khi có lỗi xảy ra
     */
    private String aiSource;
}
