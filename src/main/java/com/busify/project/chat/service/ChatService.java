package com.busify.project.chat.service;

import com.busify.project.chat.dto.ChatMessageDTO;
import com.busify.project.chat.dto.ChatNotificationDTO;
import com.busify.project.chat.dto.ChatSessionDTO;
import com.busify.project.chat.event.RoomCreatedEvent;
import com.busify.project.chat.model.ChatMessage;
import com.busify.project.chat.repository.ChatMessageRepository;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ProfileRepository profileRepository;
    private final JwtUtils jwtUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final SimpMessagingTemplate messagingTemplate; // Thêm dependency này

    public ChatMessage saveMessage(ChatMessageDTO chatMessageDTO, String roomId) {
        // Check if this is the first message in the room
        boolean isFirstMessage = chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId).isEmpty();

        // Thêm một khoảng trễ nhỏ để đảm bảo tin nhắn này được lưu trước
        // bất kỳ tin nhắn tự động nào được kích hoạt bởi sự kiện.
        if (isFirstMessage) {
            try {
                Thread.sleep(10); // 10ms delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Save the message
        ChatMessage message = ChatMessage.builder()
                .content(chatMessageDTO.getContent())
                .sender(chatMessageDTO.getSender())
                .recipient(chatMessageDTO.getRecipient())
                .type(chatMessageDTO.getType())
                .roomId(roomId) // Sẽ là null nếu là chat 1-1
                .timestamp(LocalDateTime.now())
                .build();
        ChatMessage savedMessage = chatMessageRepository.save(message);

        // Sau khi lưu tin nhắn, gửi thông báo đến kênh riêng của người nhận
        sendNotificationToRecipients(savedMessage, roomId);

        // If this is the first message, publish the room created event
        if (isFirstMessage) {
            eventPublisher.publishEvent(new RoomCreatedEvent(this, roomId, chatMessageDTO.getSender()));
        }

        return savedMessage;
    }

    /**
     * Lưu tin nhắn AI với xử lý đặc biệt
     */
    public ChatMessage saveAIMessage(ChatMessageDTO chatMessageDTO, String roomId) {
        ChatMessage message = ChatMessage.builder()
                .content(chatMessageDTO.getContent())
                .sender(chatMessageDTO.getSender())
                .recipient(chatMessageDTO.getRecipient())
                .type(chatMessageDTO.getType())
                .roomId(roomId)
                .timestamp(LocalDateTime.now())
                .build();
        
        ChatMessage savedMessage = chatMessageRepository.save(message);
        
        // Không gửi thông báo cho tin nhắn AI để tránh spam
        // AI chat thường là 1-1 và không cần thông báo
        
        return savedMessage;
    }

    /**
     * Lấy lịch sử chat AI của một user
     */
    public List<ChatMessage> getAIChatHistory(String userEmail) {
        String roomId = "ai-" + userEmail;
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    /**
     * Kiểm tra xem user có lịch sử chat với AI không
     */
    public boolean hasAIChatHistory(String userEmail) {
        String roomId = "ai-" + userEmail;
        List<ChatMessage> history = chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
        return !history.isEmpty();
    }

    public ChatMessage saveAutomaticMessage(ChatMessageDTO chatMessageDTO, String roomId) {
        ChatMessage message = ChatMessage.builder()
                .content(chatMessageDTO.getContent())
                .sender(chatMessageDTO.getSender())
                .recipient(chatMessageDTO.getRecipient())
                .type(chatMessageDTO.getType())
                .roomId(roomId)
                .timestamp(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatHistoryByRoom(String roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }

    public List<ChatMessage> getPrivateChatHistory(String user1, String user2) {
        return chatMessageRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(user1, user2,
                user1, user2);
    }

    public List<String> findMyGroupConversations(String username) {
        return chatMessageRepository.findDistinctRoomIdsBySender(username);
    }

    public List<ChatSessionDTO> getMyChatSessions() {
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";
        List<String> roomIds = chatMessageRepository.findDistinctRoomIdsBySender(email);
        List<ChatSessionDTO> chatSessions = new ArrayList<>();

        for (String roomId : roomIds) {
            // Lấy danh sách người dùng khác trong phòng chat (loại trừ email hiện tại)
            List<String> otherUsers = chatMessageRepository.findOtherUsersInRoom(roomId, email);
            if (otherUsers.size() != 1) {
                continue; // Bỏ qua nếu không có đúng 1 người khác (không phải chat 1-1)
            }

            String otherUserEmail = otherUsers.get(0);

            // Lấy thông tin người dùng khác
            Optional<Profile> otherUserProfileOpt = profileRepository.findByEmail(otherUserEmail);
            if (otherUserProfileOpt.isEmpty()) {
                continue; // Bỏ qua nếu không tìm thấy profile
            }

            Profile otherUserProfile = otherUserProfileOpt.get();

            // Lấy tin nhắn cuối cùng
            Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findTopByRoomIdOrderByTimestampDesc(roomId);
            if (lastMessageOpt.isEmpty()) {
                continue; // Bỏ qua nếu chưa có tin nhắn
            }

            ChatMessage lastMessage = lastMessageOpt.get();

            ChatSessionDTO sessionDTO = ChatSessionDTO.builder()
                    .id(roomId)
                    .customerName(otherUserProfile.getFullName())
                    .customerEmail(otherUserProfile.getEmail())
                    .avatar(null) // Hiện tại chưa có trường avatar trong Profile
                    .lastMessage(lastMessage.getContent())
                    .lastMessageTime(lastMessage.getTimestamp())
                    .build();

            chatSessions.add(sessionDTO);
        }

        // Sắp xếp các cuộc trò chuyện theo tin nhắn cuối cùng gần nhất
        chatSessions.sort((s1, s2) -> s2.getLastMessageTime().compareTo(s1.getLastMessageTime()));

        return chatSessions;
    }

    /**
     * Retrieves the most recent chat sessions for the current user, limited to the
     * specified number.
     * 
     * @param limit The maximum number of sessions to return (e.g., 3).
     * @return List of ChatSessionDTO sorted by lastMessageTime descending, limited
     *         to 'limit'.
     */
    public List<ChatSessionDTO> getMyRecentChatSessions(int limit) {
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";
        List<String> roomIds = chatMessageRepository.findDistinctRoomIdsBySender(email);
        List<ChatSessionDTO> chatSessions = new ArrayList<>();

        for (String roomId : roomIds) {
            List<String> otherUsers = chatMessageRepository.findOtherUsersInRoom(roomId, email);
            if (otherUsers.size() != 1) {
                continue;
            }

            String otherUserEmail = otherUsers.get(0);
            Optional<Profile> otherUserProfileOpt = profileRepository.findByEmail(otherUserEmail);
            if (otherUserProfileOpt.isEmpty()) {
                continue;
            }

            Profile otherUserProfile = otherUserProfileOpt.get();
            Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findTopByRoomIdOrderByTimestampDesc(roomId);
            if (lastMessageOpt.isEmpty()) {
                continue;
            }

            ChatMessage lastMessage = lastMessageOpt.get();
            ChatSessionDTO sessionDTO = ChatSessionDTO.builder()
                    .id(roomId)
                    .customerName(otherUserProfile.getFullName())
                    .customerEmail(otherUserProfile.getEmail())
                    .avatar(null)
                    .lastMessage(lastMessage.getContent())
                    .lastMessageTime(lastMessage.getTimestamp())
                    .build();

            chatSessions.add(sessionDTO);
        }

        chatSessions.sort((s1, s2) -> s2.getLastMessageTime().compareTo(s1.getLastMessageTime()));
        if (chatSessions.size() > limit) {
            chatSessions = chatSessions.subList(0, limit);
        }

        return chatSessions;
    }

    /**
     * Gửi thông báo đến kênh riêng của các người nhận (trừ người gửi).
     */
    private void sendNotificationToRecipients(ChatMessage message, String roomId) {
        // Bỏ qua nếu là tin nhắn hệ thống hoặc không phải CHAT
        if (message.getType() != ChatMessageDTO.MessageType.CHAT) {
            return;
        }

        // Tạo DTO thông báo
        String contentPreview = message.getContent().length() > 50
                ? message.getContent().substring(0, 50) + "..."
                : message.getContent();
        ChatNotificationDTO notification = ChatNotificationDTO.builder()
                .roomId(roomId)
                .sender(message.getSender())
                .contentPreview(contentPreview)
                .type(message.getType().toString())
                .timestamp(message.getTimestamp().toString()) // Chuyển sang ISO nếu cần
                .build();

        // Lấy danh sách người nhận (đối với group chat: tất cả trừ sender; private:
        // recipient)
        List<String> recipients = new ArrayList<>();
        if (roomId != null) {
            // Group chat: Lấy tất cả người dùng khác trong phòng
            recipients = chatMessageRepository.findOtherUsersInRoom(roomId, message.getSender());
        } else if (message.getRecipient() != null) {
            // Private chat: Chỉ recipient
            recipients.add(message.getRecipient());
        }

        // Gửi thông báo đến kênh riêng của mỗi người nhận
        for (String recipient : recipients) {
            messagingTemplate.convertAndSend("/topic/user/" + recipient + "/notifications", notification);
        }
    }
}