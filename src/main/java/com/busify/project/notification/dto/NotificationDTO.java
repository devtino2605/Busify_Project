package com.busify.project.notification.dto;

import java.time.LocalDateTime;

import com.busify.project.notification.enums.NotificationStatus;
import com.busify.project.notification.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationStatus status;
    private Long userId;
    private String relatedId;
    private String actionUrl;
    private String metadata;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}
