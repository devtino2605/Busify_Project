package com.busify.project.chat.event;

import com.busify.project.chat.service.ChatAssignmentService;
import com.busify.project.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomEventListener {

    private final ChatAssignmentService chatAssignmentService; // Thêm service phân bổ

    @EventListener
    public void handleRoomCreatedEvent(RoomCreatedEvent event) {

        chatAssignmentService.assignChatToAvailableAgent(event.getRoomId(), event.getUserEmail());
    }
}