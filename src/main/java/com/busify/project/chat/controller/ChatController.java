package com.busify.project.chat.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.chat.dto.ChatMessageDTO;
import com.busify.project.chat.dto.ChatSessionDTO;
import com.busify.project.chat.model.ChatMessage;
import com.busify.project.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    /**
     * Xử lý tin nhắn chat trong một phòng chat chung (n-n).
     * Client gửi tin nhắn đến "/app/chat.sendMessage/{roomId}".
     * Server sẽ broadcast tin nhắn đến tất cả client đã đăng ký
     * "/topic/public/{roomId}".
     */
    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessageDTO chatMessage) {
        chatService.saveMessage(chatMessage, roomId);
        messagingTemplate.convertAndSend("/topic/public/" + roomId, chatMessage);
    }

    /**
     * Xử lý việc một người dùng tham gia vào phòng chat (n-n).
     * Client gửi tin nhắn đến "/app/chat.addUser/{roomId}".
     * Server sẽ broadcast thông báo cho những người khác trong phòng.
     * Sửa đổi: Không lưu tin nhắn JOIN vào database.
     */
    @MessageMapping("/chat.addUser/{roomId}")
    public void addUser(@DestinationVariable String roomId, @Payload ChatMessageDTO chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {
        // Thêm username vào WebSocket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        // Chỉ broadcast tin nhắn JOIN, không lưu lại
        messagingTemplate.convertAndSend("/topic/public/" + roomId, chatMessage);
    }

    /**
     * Xử lý tin nhắn chat riêng tư (1-1).
     * Client gửi tin nhắn đến "/app/chat.private".
     * Server sẽ gửi tin nhắn đến một người dùng cụ thể qua queue riêng của họ.
     * Người nhận (recipient) cần đăng ký lắng nghe trên "/user/queue/private".
     */
    // @MessageMapping("/chat.private")
    // public void sendPrivateMessage(@Payload ChatMessageDTO chatMessage) {
    // chatService.savePrivateMessage(chatMessage);
    // // Gửi tin nhắn đến queue riêng của người nhận
    // // Ví dụ: /user/{recipientUsername}/queue/private
    // messagingTemplate.convertAndSendToUser(
    // chatMessage.getRecipient(),
    // "/queue/private",
    // chatMessage);
    // }

    @GetMapping("/chat/history/room/{roomId}")
    @ResponseBody
    public List<ChatMessage> getRoomHistory(@PathVariable String roomId) {
        return chatService.getChatHistoryByRoom(roomId);
    }

    // @GetMapping("/chat/history/private")
    // @ResponseBody
    // public List<ChatMessage> getPrivateHistory(@RequestParam String user1,
    // @RequestParam String user2) {
    // return chatService.getPrivateChatHistory(user1, user2);
    // }

    /**
     * Lấy danh sách tất cả các phòng chat nhóm (n-n) của người dùng hiện tại.
     */
    @GetMapping("/chat/my-rooms")
    @ResponseBody
    public ResponseEntity<List<ChatSessionDTO>> getMyChatSessions() {
        List<ChatSessionDTO> sessions = chatService.getMyChatSessions();
        return ResponseEntity.ok(sessions);
    }

    /**
     * Tạo một phòng chat mới và trả về room ID tự động.
     * Client gọi POST /chat/createRoom để lấy room ID.
     */
    @PostMapping("/chat/createRoom")
    @ResponseBody
    public String createRoom() {
        String roomId = UUID.randomUUID().toString();
        // Có thể lưu roomId vào database nếu cần (ví dụ: thêm entity ChatRoom)
        return roomId;
    }

    @GetMapping("/chat/recent-sessions")
    @ResponseBody
    public ResponseEntity<List<ChatSessionDTO>> getRecentChatSessions() {
        List<ChatSessionDTO> recentSessions = chatService.getMyRecentChatSessions(3);
        return ResponseEntity.ok(recentSessions);
    }
}
