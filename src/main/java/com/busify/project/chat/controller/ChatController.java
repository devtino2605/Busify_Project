package com.busify.project.chat.controller;

import java.util.Objects;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.busify.project.chat.dto.ChatMessageDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Xử lý tin nhắn chat trong một phòng chat chung (n-n).
     * Client gửi tin nhắn đến "/app/chat.sendMessage/{roomId}".
     * Server sẽ broadcast tin nhắn đến tất cả client đã đăng ký
     * "/topic/public/{roomId}".
     */
    @MessageMapping("/chat.sendMessage/{roomId}")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessageDTO chatMessage) {
        messagingTemplate.convertAndSend("/topic/public/" + roomId, chatMessage);
    }

    /**
     * Xử lý việc một người dùng tham gia vào phòng chat (n-n).
     * Client gửi tin nhắn đến "/app/chat.addUser/{roomId}".
     * Server sẽ broadcast thông báo cho những người khác trong phòng.
     */
    @MessageMapping("/chat.addUser/{roomId}")
    public void addUser(@DestinationVariable String roomId, @Payload ChatMessageDTO chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {
        // Thêm username vào WebSocket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessage.getSender());
        messagingTemplate.convertAndSend("/topic/public/" + roomId, chatMessage);
    }

    /**
     * Xử lý tin nhắn chat riêng tư (1-1).
     * Client gửi tin nhắn đến "/app/chat.private".
     * Server sẽ gửi tin nhắn đến một người dùng cụ thể qua queue riêng của họ.
     * Người nhận (recipient) cần đăng ký lắng nghe trên "/user/queue/private".
     */
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload ChatMessageDTO chatMessage) {
        // Gửi tin nhắn đến queue riêng của người nhận
        // Ví dụ: /user/{recipientUsername}/queue/private
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(),
                "/queue/private",
                chatMessage);
    }
}
