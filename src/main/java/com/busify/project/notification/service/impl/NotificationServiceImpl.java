package com.busify.project.notification.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.busify.project.notification.dto.NotificationDTO;
import com.busify.project.notification.entity.Notification;
import com.busify.project.notification.entity.NotificationData;
import com.busify.project.notification.enums.NotificationStatus;
import com.busify.project.notification.exception.NotificationCreationException;
import com.busify.project.notification.exception.NotificationNotFoundException;
import com.busify.project.notification.exception.NotificationUserException;
import com.busify.project.notification.mapper.NotificationMapper;
import com.busify.project.notification.repository.NotificationRepo;
import com.busify.project.notification.repository.NotificationRepository;
import com.busify.project.notification.service.NotificationService;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import org.springframework.context.event.EventListener;
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
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @EventListener
    @Override
    public void handlePaymentSuccessEvent(PaymentSuccessEvent event) {
        Logger logger = Logger.getLogger(NotificationService.class.getName());
        final NotificationData data = new NotificationData();
        final String uid = UUID.randomUUID().toString();
        data.setId(uid);

        // Guard for cargo payments which have booking == null
        if (event.getPayment().getBooking() != null) {
            String guestEmail = event.getPayment().getBooking().getGuestEmail();
            logger.info("Handling payment success event for booking payment with guest email: " + guestEmail);
            final String message = "User " + guestEmail + " has made a payment of " + event.getPayment().getAmount();
            data.setMessage(message);
            data.setTitle("A User has made a payment");
            data.setData(Map.of(
                    "paymentId", event.getPayment().getPaymentId(),
                    "amount", event.getPayment().getAmount()));
            if (event.getPayment().getBooking().getTrip() != null
                    && event.getPayment().getBooking().getTrip().getBus() != null
                    && event.getPayment().getBooking().getTrip().getBus().getOperator() != null) {
                data.setSub("operator/" + event.getPayment().getBooking().getTrip().getBus().getOperator().getId());
            }
            notificationRepo.save(data);
            notificationController.sendMessage(data);
        } else if (event.getPayment().getCargoBooking() != null) {
            // Cargo payment - use cargo sender/receiver info for notification
            var cargo = event.getPayment().getCargoBooking();
            String contact = cargo.getSenderEmail() != null ? cargo.getSenderEmail() : cargo.getReceiverEmail();
            logger.info("Handling payment success event for cargo payment, contact: " + contact);
            final String message = "Cargo payment received for cargoCode " + cargo.getCargoCode() + " amount "
                    + event.getPayment().getAmount();
            data.setMessage(message);
            data.setTitle("Cargo payment received");
            data.setData(Map.of(
                    "paymentId", event.getPayment().getPaymentId(),
                    "cargoBookingId", cargo.getCargoBookingId(),
                    "amount", event.getPayment().getAmount()));
            // For cargo we may not have an operator to target; use a generic topic
            data.setSub("operator/all");
            notificationRepo.save(data);
            notificationController.sendMessage(data);
        } else {
            logger.info("Received PaymentSuccessEvent for paymentId " + event.getPayment().getPaymentId()
                    + " without booking or cargoBooking, skipping notification.");
        }
    }

    @Override
    public NotificationDTO createNotification(Notification notification) {
        try {
            Notification saved = notificationRepository.save(notification);
            log.info("✅ Đã tạo notification: {} cho user ID: {}", saved.getTitle(), saved.getUserId());
            return NotificationMapper.toDTO(saved);
        } catch (Exception e) {
            log.error("❌ Lỗi khi tạo notification: {}", e.getMessage(), e);
            throw NotificationCreationException.creationFailed(e);
        }
    }

    @Override
    public List<NotificationDTO> getNotificationsByUser() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> NotificationUserException.userNotExists());
        List<Notification> notifications = notificationRepository.findByUserId(user.getId());
        return notifications.stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> NotificationUserException.userNotExists());
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
        User user = userRepository.findByEmail(email).orElseThrow(() -> NotificationUserException.userNotExists());
        return notificationRepository.countByUserIdAndStatus(user.getId(), NotificationStatus.UNREAD);
    }

    @Override
    public NotificationDTO markAsRead(Long notificationId) {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> NotificationUserException.userNotExists());
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> NotificationNotFoundException.notExists());

        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);
        return NotificationMapper.toDTO(saved);

    }

    @Override
    public void markAllAsRead() {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> NotificationUserException.userNotExists());
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
        User user = userRepository.findByEmail(email).orElseThrow(() -> NotificationUserException.userNotExists());
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> NotificationNotFoundException.notExists());

        notification.setIsDeleted(true);
        notificationRepository.save(notification);
        log.info("✅ Đã xóa notification ID: {} cho user ID: {}", notificationId, user.getId());
    }

    @Override
    public NotificationDTO updateStatus(Long notificationId, NotificationStatus status) {
        String email = jwtUtils.getCurrentUserLogin().isPresent() ? jwtUtils.getCurrentUserLogin().get() : null;
        User user = userRepository.findByEmail(email).orElseThrow(() -> NotificationUserException.userNotExists());
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> NotificationNotFoundException.notExists());

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
        User user = userRepository.findByEmail(email).orElseThrow(() -> NotificationUserException.userNotExists());
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> NotificationNotFoundException.notExists());
        return NotificationMapper.toDTO(notification);
    }
}
