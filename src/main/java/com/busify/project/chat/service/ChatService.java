package com.busify.project.chat.service;

import com.busify.project.chat.dto.ChatMessageDTO;
import com.busify.project.chat.model.ChatMessage;
import com.busify.project.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessageDTO chatMessageDTO, String roomId) {
        ChatMessage message = ChatMessage.builder()
                .content(chatMessageDTO.getContent())
                .sender(chatMessageDTO.getSender())
                .recipient(chatMessageDTO.getRecipient())
                .type(chatMessageDTO.getType())
                .roomId(roomId) // Sẽ là null nếu là chat 1-1
                .timestamp(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(message);
    }

    public ChatMessage savePrivateMessage(ChatMessageDTO chatMessageDTO) {
        ChatMessage message = ChatMessage.builder()
                .content(chatMessageDTO.getContent())
                .sender(chatMessageDTO.getSender())
                .recipient(chatMessageDTO.getRecipient())
                .type(chatMessageDTO.getType())
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
}