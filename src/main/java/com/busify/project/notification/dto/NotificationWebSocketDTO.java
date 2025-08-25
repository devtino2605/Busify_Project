package com.busify.project.notification.dto;

import com.busify.project.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationWebSocketDTO {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private String actionUrl;
    private LocalDateTime createdAt;
}