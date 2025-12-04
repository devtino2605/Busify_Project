package com.busify.project.ai_chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity riêng cho AI Chat messages
 * Tách biệt hoàn toàn với Customer Service Chat
 */
@Entity
@Table(name = "ai_chat_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MessageRole role;

    private LocalDateTime timestamp;

    @Column(length = 100)
    private String sessionId;

    /**
     * Role của tin nhắn trong cuộc hội thoại AI
     */
    public enum MessageRole {
        USER, // Tin nhắn từ người dùng
        ASSISTANT, // Tin nhắn từ AI Bot
        SYSTEM // Tin nhắn hệ thống (thông báo, chuyển nhân viên, v.v.)
    }
}
