package com.busify.project.chat.repository;

import com.busify.project.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Tìm tất cả tin nhắn trong một phòng chat chung.
     */
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);

    /**
     * Tìm tất cả tin nhắn giữa hai người dùng (chat 1-1).
     */
    List<ChatMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(String sender, String recipient,
            String recipient2, String sender2);
}