package com.busify.project.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
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
        ChatMessage savedMessage = chatService.saveMessage(chatMessage, roomId);
        messagingTemplate.convertAndSend("/topic/public/" + roomId, savedMessage);
        // Thông báo riêng đã được xử lý trong ChatService.saveMessage
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

    /**
     * Lấy lịch sử chat với phân trang.
     */
    @GetMapping("/chat/history/room/{roomId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getRoomHistory(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ChatMessage> chatPage = chatService.getChatHistoryByRoom(roomId, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("messages", chatPage.getContent());
        response.put("pageNumber", chatPage.getNumber() + 1);
        response.put("pageSize", chatPage.getSize());
        response.put("totalMessages", chatPage.getTotalElements());
        response.put("totalPages", chatPage.getTotalPages());
        response.put("hasNext", chatPage.hasNext());
        response.put("hasPrevious", chatPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    /**
     * Lấy lịch sử chat không phân trang (để backward compatibility).
     */
    @GetMapping("/chat/history/room/{roomId}/all")
    @ResponseBody
    public List<ChatMessage> getRoomHistoryAll(@PathVariable String roomId) {
        return chatService.getChatHistoryByRoom(roomId);
    }

    /**
     * Lấy danh sách phòng chat với phân trang và sắp xếp theo tin nhắn mới nhất.
     */
    @GetMapping("/chat/my-rooms")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMyChatSessions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ChatSessionDTO> sessionsPage = chatService.getMyChatSessions(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("sessions", sessionsPage.getContent());
        response.put("pageNumber", sessionsPage.getNumber() + 1);
        response.put("pageSize", sessionsPage.getSize());
        response.put("totalSessions", sessionsPage.getTotalElements());
        response.put("totalPages", sessionsPage.getTotalPages());
        response.put("hasNext", sessionsPage.hasNext());
        response.put("hasPrevious", sessionsPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    /**
     * Lấy danh sách phòng chat không phân trang (để backward compatibility).
     */
    @GetMapping("/chat/my-rooms/all")
    @ResponseBody
    public ResponseEntity<List<ChatSessionDTO>> getMyChatSessionsAll() {
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

    /**
     * Lấy lịch sử chat riêng tư với phân trang.
     */
    @GetMapping("/chat/history/private")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPrivateHistory(
            @RequestParam String user1,
            @RequestParam String user2,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<ChatMessage> chatPage = chatService.getPrivateChatHistory(user1, user2, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("messages", chatPage.getContent());
        response.put("pageNumber", chatPage.getNumber() + 1);
        response.put("pageSize", chatPage.getSize());
        response.put("totalMessages", chatPage.getTotalElements());
        response.put("totalPages", chatPage.getTotalPages());
        response.put("hasNext", chatPage.hasNext());
        response.put("hasPrevious", chatPage.hasPrevious());

        return ResponseEntity.ok(response);
    }
}
