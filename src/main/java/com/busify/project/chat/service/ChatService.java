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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        boolean isFirstMessage = chatMessageRepository.findByRoomIdOrderByTimestampDesc(roomId).isEmpty();

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

    /**
     * Lấy lịch sử chat của một phòng với phân trang.
     * Trang đầu tiên (page=1) sẽ chứa các tin nhắn mới nhất.
     * Các tin nhắn trong mỗi trang được sắp xếp theo thứ tự thời gian tăng dần (cũ
     * đến mới).
     */
    public Page<ChatMessage> getChatHistoryByRoom(String roomId, int page, int size) {
        long totalMessages = chatMessageRepository.countByRoomId(roomId);
        if (totalMessages == 0) {
            return Page.empty();
        }
        int totalPages = (int) Math.ceil((double) totalMessages / size);
        int targetPage = totalPages - page;

        if (targetPage < 0) {
            // Nếu page yêu cầu lớn hơn tổng số trang, trả về trang trống
            return Page.empty(PageRequest.of(page - 1, size));
        }

        Pageable pageable = PageRequest.of(targetPage, size);
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId, pageable);
    }

    public List<ChatMessage> getChatHistoryByRoom(String roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampDesc(roomId);
    }

    /**
     * Lấy lịch sử chat riêng tư với phân trang.
     * Tương tự getChatHistoryByRoom, trang đầu tiên là mới nhất, sắp xếp ASC.
     */
    public Page<ChatMessage> getPrivateChatHistory(String user1, String user2, int page, int size) {
        long totalMessages = chatMessageRepository.countBySenderAndRecipientOrRecipientAndSender(user1, user2, user1,
                user2);
        if (totalMessages == 0) {
            return Page.empty();
        }
        int totalPages = (int) Math.ceil((double) totalMessages / size);
        int targetPage = totalPages - page;

        if (targetPage < 0) {
            return Page.empty(PageRequest.of(page - 1, size));
        }

        Pageable pageable = PageRequest.of(targetPage, size);
        return chatMessageRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampAsc(
                user1, user2, user1, user2, pageable);
    }

    public List<ChatMessage> getPrivateChatHistory(String user1, String user2) {
        return chatMessageRepository.findBySenderAndRecipientOrRecipientAndSenderOrderByTimestampDesc(user1, user2,
                user1, user2);
    }

    public List<String> findMyGroupConversations(String username) {
        return chatMessageRepository.findDistinctRoomIdsBySender(username);
    }

    /**
     * Lấy danh sách session chat với phân trang và sắp xếp theo tin nhắn mới nhất.
     */
    public Page<ChatSessionDTO> getMyChatSessions(int page, int size) {
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<String> roomIdsPage = chatMessageRepository.findDistinctRoomIdsBySenderOrderByLatestMessage(email,
                pageable);

        List<ChatSessionDTO> chatSessions = new ArrayList<>();

        for (String roomId : roomIdsPage.getContent()) {
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

        return new org.springframework.data.domain.PageImpl<>(
                chatSessions,
                pageable,
                roomIdsPage.getTotalElements());
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