package com.busify.project.ai_chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.busify.project.ai_chat.entity.AIChatMessage;

import java.util.List;

@Repository
public interface AIChatMessageRepository extends JpaRepository<AIChatMessage, Long> {

    /**
     * Lấy lịch sử chat AI của user theo email, sắp xếp theo thời gian
     */
    List<AIChatMessage> findByUserEmailOrderByTimestampAsc(String userEmail);

    /**
     * Lấy lịch sử chat AI của user theo sessionId
     */
    List<AIChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId);

    /**
     * Lấy N tin nhắn gần nhất của user
     */
    @Query("SELECT m FROM AIChatMessage m WHERE m.userEmail = :userEmail ORDER BY m.timestamp DESC LIMIT :limit")
    List<AIChatMessage> findRecentMessagesByUserEmail(@Param("userEmail") String userEmail, @Param("limit") int limit);

    /**
     * Kiểm tra user có lịch sử chat AI không
     */
    boolean existsByUserEmail(String userEmail);

    /**
     * Xóa tất cả lịch sử chat AI của user
     */
    void deleteByUserEmail(String userEmail);

    /**
     * Đếm số tin nhắn của user
     */
    long countByUserEmail(String userEmail);
}
