package com.busify.project.ai_chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper chứa response từ AI service kèm thông tin nguồn gốc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResponseWrapper {
    private String message;
    private AISource source;

    /**
     * Nguồn gốc của AI response
     */
    public enum AISource {
        GEMINI_AI, // Response từ Google Gemini API thực sự
        FALLBACK, // Response mặc định khi AI không khả dụng
        WELCOME, // Tin nhắn chào mừng tự động
        ERROR // Response khi có lỗi xảy ra
    }
}
