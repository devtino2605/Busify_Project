package com.busify.project.chat.event;

import com.busify.project.chat.dto.ChatMessageDTO;
import com.busify.project.chat.service.ChatAssignmentService;
import com.busify.project.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomEventListener {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatAssignmentService chatAssignmentService; // Thêm service phân bổ

    // Customer service email - có thể không cần nữa nếu logic gán luôn thành công
    private static final String CUSTOMER_SERVICE_EMAIL = "customer.service@busify.com";

    @EventListener
    public void handleRoomCreatedEvent(RoomCreatedEvent event) {
        // 1. Gửi tin nhắn chào mừng ban đầu từ hệ thống
        ChatMessageDTO welcomeMessage = ChatMessageDTO.builder()
                .content(
                        "Chào bạn, yêu cầu của bạn đã được Chăm sóc khách hàng tiếp nhận. Vui lòng chờ trong giây lát để kết nối với nhân viên hỗ trợ.")
                .sender(CUSTOMER_SERVICE_EMAIL)
                .recipient(event.getUserEmail())
                .type(ChatMessageDTO.MessageType.CHAT)
                .build();

        chatService.saveAutomaticMessage(welcomeMessage, event.getRoomId());
        messagingTemplate.convertAndSend("/topic/public/" + event.getRoomId(), welcomeMessage);

        // 2. Kích hoạt cơ chế phân bổ để gán phòng chat cho một nhân viên
        chatAssignmentService.assignChatToAvailableAgent(event.getRoomId(), event.getUserEmail());
    }
}