package com.busify.project.notification.repository;

import com.busify.project.notification.entity.Notification;
import com.busify.project.notification.enums.NotificationStatus;
import com.busify.project.notification.enums.NotificationType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
        Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

        // Tìm notifications theo user ID và status
        @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.status = :status ORDER BY n.createdAt DESC")
        List<Notification> findByUserIdAndStatusOrderByCreatedAtDesc(@Param("userId") Long userId,
                        @Param("status") NotificationStatus status);

        // Đếm notifications chưa đọc
        @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.status = :status")
        long countByUserIdAndStatus(Long userId, NotificationStatus status);

        List<Notification> findByUserId(Long userId);

        // Tìm notifications theo user ID và status
        @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.status = :status ORDER BY n.createdAt DESC")
        List<Notification> findByUserIdAndStatus(@Param("userId") Long userId,
                        @Param("status") NotificationStatus status);

        // Tìm notification theo ID và user ID (để bảo mật)
        Optional<Notification> findByIdAndUserId(Long id, Long userId);

        // Tìm notifications theo type
        List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, NotificationType type);

        // Tìm notifications trong khoảng thời gian
        @Query("SELECT n FROM Notification n WHERE n.userId = :userId " +
                        "AND n.createdAt BETWEEN :startDate AND :endDate " +
                        "ORDER BY n.createdAt DESC")
        List<Notification> findByUserIdAndCreatedAtBetween(
                        @Param("userId") Long userId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Xóa notifications cũ (cho cleanup job)
        @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoffDate")
        void deleteOldNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);

        // Tìm notifications theo related ID (ví dụ: báo cáo tháng cụ thể)
        List<Notification> findByUserIdAndRelatedIdOrderByCreatedAtDesc(Long userId, String relatedId);
}
