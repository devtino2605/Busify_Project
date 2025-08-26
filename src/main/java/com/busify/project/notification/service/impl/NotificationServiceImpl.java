package com.busify.project.notification.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.busify.project.notification.dto.NotificationDTO;
import com.busify.project.notification.dto.NotificationWebSocketDTO;
import com.busify.project.notification.entity.Notification;
import com.busify.project.notification.entity.NotificationData;
import com.busify.project.notification.enums.NotificationStatus;
import com.busify.project.notification.mapper.NotificationMapper;
import com.busify.project.notification.repository.NotificationRepo;
import com.busify.project.notification.repository.NotificationRepository;
import com.busify.project.notification.service.NotificationService;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busify.project.common.event.PaymentSuccessEvent;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.notification.controller.NotificationController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;
    private final NotificationController notificationController;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @EventListener
    @Override
    public void handlePaymentSuccessEvent(PaymentSuccessEvent event) {
        Logger logger = Logger.getLogger(NotificationService.class.getName());
        logger.info(
                "Handling payment success event for payment ID: " + event.getPayment().getBooking().getGuestEmail());
        final NotificationData data = new NotificationData();
        final Long uid = UUID.randomUUID().timestamp();
        data.setId(uid);
        final String message = "User " + event.getPayment().getBooking().getGuestEmail() + " has made a payment of "
                + event.getPayment().getAmount();
        data.setMessage(message);
        data.setTitle("A User has made a payment");
        data.setData(Map.of(
                "paymentId", event.getPayment().getPaymentId(),
                "amount", event.getPayment().getAmount()));
        data.setSub("operator/" + event.getPayment().getBooking().getTrip().getBus().getOperator().getId());
        notificationRepo.save(data);
        notificationController.sendMessage(data);
    }

    @Override
    public NotificationDTO createNotification(Notification notification) {
        try {
            Notification saved = notificationRepository.save(notification);
            log.info("✅ Đã tạo notification: {} cho user ID: {}", saved.getTitle(), saved.getUserId());
            return NotificationMapper.toDTO(saved);
        } catch (Exception e) {
            log.error("❌ Lỗi khi tạo notification: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể tạo notification", e);
        }
    }

    @Override
    public void sendRealTimeNotification(Long userId, Notification notification) {
        try {
            // Kiểm tra userId có null không
            if (userId == null) {
                log.warn("⚠️ UserId null, không thể gửi real-time notification");
                return;
            }

            // Gửi notification qua WebSocket tới user specific topic
            String destination = "/topic/notifications/" + userId;

            // Tạo DTO để gửi qua WebSocket
            NotificationWebSocketDTO dto = NotificationWebSocketDTO.builder()
                    .id(notification.getId())
                    .title(notification.getTitle())
                    .message(notification.getMessage())
                    .type(notification.getType())
                    .actionUrl(notification.getActionUrl())
                    .createdAt(notification.getCreatedAt())
                    .build();

            messagingTemplate.convertAndSend(destination, dto);

            log.info("🔔 Đã gửi real-time notification tới user ID: {} qua WebSocket", userId);
        } catch (Exception e) {
            log.error("❌ Lỗi khi gửi real-time notification: {}", e.getMessage(), e);
            // Không throw exception vì đây chỉ là bonus feature
        }
    }

    // Overload method để hỗ trợ gửi bằng email
    public void sendRealTimeNotificationByEmail(String email, Notification notification) {
        try {
            if (email == null || email.trim().isEmpty()) {
                log.warn("⚠️ Email null hoặc rỗng, không thể gửi real-time notification");
                return;
            }

            // Tìm user theo email
            User user = userRepository.findByEmail(email)
                    .orElse(null);

            if (user == null) {
                log.warn("⚠️ Không tìm thấy user với email: {}", email);
                return;
            }

            // Gọi method chính với userId
            sendRealTimeNotification(user.getId(), notification);

        } catch (Exception e) {
            log.error("❌ Lỗi khi gửi real-time notification bằng email: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<NotificationDTO> getNotificationsByUser() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        List<Notification> notifications = notificationRepository.findByUserId(user.getId());
        return notifications.stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Long userId = user.getId();
        List<Notification> notifications = notificationRepository.findByUserIdAndStatus(userId,
                NotificationStatus.UNREAD);
        return notifications.stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadNotifications() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        return notificationRepository.countByUserIdAndStatus(user.getId(), NotificationStatus.UNREAD);
    }

    @Override
    public NotificationDTO markAsRead(Long notificationId) {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));

        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        return NotificationMapper.toDTO(saved);

    }

    @Override
    public void markAllAsRead() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        List<NotificationDTO> unreadNotifications = getUnreadNotifications();

        unreadNotifications.forEach(notification -> {
            notification.setStatus(NotificationStatus.READ);
            notification.setReadAt(LocalDateTime.now());
        });

        List<Notification> notifications = unreadNotifications.stream()
                .map(NotificationMapper::toEntity)
                .collect(Collectors.toList());

        notificationRepository.saveAll(notifications);
        log.info("✅ Đã đánh dấu {} notifications là đã đọc cho user ID: {}",
                unreadNotifications.size(), user.getId());
    }

    @Override
    public void deleteNotification(Long notificationId) {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));

        notificationRepository.delete(notification);
        log.info("✅ Đã xóa notification ID: {} cho user ID: {}", notificationId, user.getId());
    }

    @Override
    public NotificationDTO updateStatus(Long notificationId, NotificationStatus status) {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));

        notification.setStatus(status);
        if (status == NotificationStatus.READ && notification.getReadAt() == null) {
            notification.setReadAt(LocalDateTime.now());
        }
        Notification saved = notificationRepository.save(notification);
        return NotificationMapper.toDTO(saved);
    }

    @Override
    public NotificationDTO getNotificationById(Long notificationId) {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> new RuntimeException("Notification không tồn tại"));
        return NotificationMapper.toDTO(notification);
    }
}
