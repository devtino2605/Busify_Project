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
public class AIChatResponseDTO {
    private String message;
    private String sender;
    private LocalDateTime timestamp;
    private boolean shouldTransferToHuman; // Có cần chuyển cho nhân viên không
    private String transferReason; // Lý do chuyển
}