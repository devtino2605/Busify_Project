package com.busify.project.ai_chat.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration cho Google Gemini AI Chat service
 */
@Configuration
@ConfigurationProperties(prefix = "gemini.api")
@Data
@Slf4j
public class GeminiConfig {
    private String key;
    private String model = "gemini-2.0-flash";
    private int timeout = 30;
    private int maxTokens = 1000;
    private double temperature = 0.7;

    /**
     * Kiểm tra xem API key có được cấu hình không
     */
    public boolean isConfigured() {
        return key != null && !key.trim().isEmpty();
    }
}
