package com.busify.project.chat.repository;

import com.busify.project.chat.dto.ChatMessageDTO;
import com.busify.project.chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

        /**
         * Tìm tất cả tin nhắn trong một phòng chat chung.
         */
        List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);

        /**
         * Tìm tất cả tin nhắn giữa hai người dùng (chat 1-1).
         */
        List<ChatMessage> findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(String sender1,
                        String recipient1,
                        String sender2, String recipient2);

        /**
         * Tìm tất cả các roomId (phòng chat nhóm) mà một người dùng đã tham gia.
         * 
         * @param username Tên người dùng (thường là email)
         * @return Danh sách các roomId duy nhất
         */
        @Query("SELECT DISTINCT c.roomId FROM ChatMessage c WHERE c.sender = :username AND c.roomId IS NOT NULL")
        List<String> findDistinctRoomIdsBySender(@Param("username") String username);

        @Query("SELECT DISTINCT cm.sender FROM ChatMessage cm WHERE cm.roomId = :roomId AND cm.sender != :username " +
                        "UNION " +
                        "SELECT DISTINCT cm.recipient FROM ChatMessage cm WHERE cm.roomId = :roomId AND cm.recipient != :username")
        List<String> findOtherUsersInRoom(@Param("roomId") String roomId, @Param("username") String username);

        Optional<ChatMessage> findTopByRoomIdOrderByTimestampDesc(String roomId);

        /**
         * Đếm số lượng phòng chat duy nhất mà một nhân viên đã được gán.
         * Việc "gán" được xác định bằng một tin nhắn có type là SYSTEM_ASSIGN.
         *
         * @param senderEmail Email của nhân viên.
         * @param type        Loại tin nhắn (SYSTEM_ASSIGN).
         * @return Số lượng phòng chat.
         */
        @Query("SELECT COUNT(DISTINCT c.roomId) FROM ChatMessage c WHERE c.sender = :senderEmail AND c.type = :type")
        long countDistinctRoomIdBySenderAndType(@Param("senderEmail") String senderEmail,
                        @Param("type") ChatMessageDTO.MessageType type);

        /**
         * Tìm tin nhắn SYSTEM_ASSIGN trong một room để xác định agent được gán
         */
        List<ChatMessage> findByRoomIdAndTypeOrderByTimestampAsc(String roomId, ChatMessageDTO.MessageType type);
        
}