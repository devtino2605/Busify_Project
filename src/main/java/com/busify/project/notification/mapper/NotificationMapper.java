package com.busify.project.notification.mapper;

import com.busify.project.notification.dto.NotificationDTO;
import com.busify.project.notification.entity.Notification;

public class NotificationMapper {

    public static NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setActionUrl(notification.getActionUrl());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setStatus(notification.getStatus());
        dto.setUserId(notification.getUserId());
        dto.setRelatedId(notification.getRelatedId());
        dto.setMetadata(notification.getMetaData());
        return dto;
    }

    public static Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setType(dto.getType());
        notification.setActionUrl(dto.getActionUrl());
        notification.setCreatedAt(dto.getCreatedAt());
        notification.setStatus(dto.getStatus());
        notification.setUserId(dto.getUserId());
        return notification;
    }
}
