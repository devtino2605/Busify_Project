package com.busify.project.notification.service;

import com.busify.project.common.event.PaymentSuccessEvent;
import com.busify.project.notification.dto.NotificationDTO;
import com.busify.project.notification.entity.Notification;
import com.busify.project.notification.enums.NotificationStatus;

import java.util.List;

public interface NotificationService {

    public void handlePaymentSuccessEvent(PaymentSuccessEvent event);

    NotificationDTO createNotification(Notification notification);

    // Lấy notifications của user
    List<NotificationDTO> getNotificationsByUser();

    List<NotificationDTO> getUnreadNotifications();

    // Lấy notification theo ID
    NotificationDTO getNotificationById(Long notificationId);

    // Đếm số notifications chưa đọc
    long countUnreadNotifications();

    // Đánh dấu đã đọc
    NotificationDTO markAsRead(Long notificationId);

    // Đánh dấu tất cả đã đọc
    void markAllAsRead();

    // Xóa notification
    void deleteNotification(Long notificationId);

    // Cập nhật status
    NotificationDTO updateStatus(Long notificationId, NotificationStatus status);
}
