package com.busify.project.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIChatRequestDTO {
    private String message;
    private String context; // Ngữ cảnh cuộc hội thoại nếu cần
}