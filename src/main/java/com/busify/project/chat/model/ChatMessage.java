package com.busify.project.chat.model;

import com.busify.project.chat.dto.ChatMessageDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String sender;

    private String recipient; // Dùng cho chat 1-1

    @Enumerated(EnumType.STRING)
    @Column(length = 20) // Thêm annotation này để đảm bảo độ dài cột đủ lớn
    private ChatMessageDTO.MessageType type;

    private String roomId; // Dùng cho chat chung (n-n)

    private LocalDateTime timestamp;
}