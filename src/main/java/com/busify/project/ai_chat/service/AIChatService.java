package com.busify.project.ai_chat.service;

import com.busify.project.ai_chat.dto.AIChatHistoryDTO;
import com.busify.project.ai_chat.dto.AIChatResponseDTO;
import com.busify.project.ai_chat.dto.AIResponseWrapper;
import com.busify.project.ai_chat.dto.AIResponseWrapper.AISource;
import com.busify.project.ai_chat.entity.AIChatMessage;
import com.busify.project.ai_chat.repository.AIChatMessageRepository;
import com.busify.project.common.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service quản lý AI Chat - hoàn toàn độc lập với Customer Service Chat
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AIChatService {

    private final AIChatMessageRepository aiChatMessageRepository;
    private final AIService aiService;
    private final JwtUtils jwtUtils;

    /**
     * Xử lý tin nhắn từ user và trả về phản hồi AI
     */
    @Transactional
    public AIChatResponseDTO processUserMessage(String message, String sessionId) {
        String userEmail = getCurrentUserEmail();
        log.info("Processing AI chat message from user: {}", userEmail);

        // Tạo session ID nếu chưa có
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = generateSessionId();
        }

        // 1. Lưu tin nhắn của user
        AIChatMessage userMessage = saveMessage(userEmail, message, AIChatMessage.MessageRole.USER, sessionId);
        log.info("Saved user message with ID: {}", userMessage.getId());

        // 2. Kiểm tra có cần chuyển cho nhân viên không
        if (aiService.shouldTransferToHuman(message)) {
            String transferContent = "Tôi thấy bạn cần hỗ trợ từ nhân viên. Đang chuyển cuộc trò chuyện cho nhân viên hỗ trợ...";
            AIChatMessage transferMessage = saveMessage(userEmail, transferContent, AIChatMessage.MessageRole.SYSTEM,
                    sessionId);

            return AIChatResponseDTO.builder()
                    .id(transferMessage.getId())
                    .message(transferContent)
                    .role("SYSTEM")
                    .timestamp(transferMessage.getTimestamp())
                    .shouldTransferToHuman(true)
                    .transferReason("Khách hàng yêu cầu hỗ trợ từ nhân viên")
                    .aiSource("SYSTEM")
                    .build();
        }

        // 3. Lấy lịch sử để có context
        List<AIChatMessage> chatHistory = aiChatMessageRepository.findByUserEmailOrderByTimestampAsc(userEmail);

        // 4. Lấy phản hồi từ AI (sử dụng method mới với source)
        AIResponseWrapper aiResponse;
        if (chatHistory.size() > 1) {
            aiResponse = aiService.getAIResponseWithHistoryAndSource(message, chatHistory, userEmail);
        } else {
            aiResponse = aiService.getAIResponseWithSource(message, userEmail);
        }

        // 5. Lưu tin nhắn AI
        AIChatMessage aiMessage = saveMessage(userEmail, aiResponse.getMessage(), AIChatMessage.MessageRole.ASSISTANT,
                sessionId);
        log.info("Saved AI message with ID: {}, source: {}", aiMessage.getId(), aiResponse.getSource());

        return AIChatResponseDTO.builder()
                .id(aiMessage.getId())
                .message(aiResponse.getMessage())
                .role("ASSISTANT")
                .timestamp(aiMessage.getTimestamp())
                .shouldTransferToHuman(false)
                .aiSource(aiResponse.getSource().name())
                .build();
    }

    /**
     * Lấy lịch sử chat AI của user hiện tại
     */
    public AIChatHistoryDTO getChatHistory() {
        String userEmail = getCurrentUserEmail();
        List<AIChatMessage> messages = aiChatMessageRepository.findByUserEmailOrderByTimestampAsc(userEmail);

        List<AIChatResponseDTO> messageDTOs = messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return AIChatHistoryDTO.builder()
                .userEmail(userEmail)
                .totalMessages(messages.size())
                .messages(messageDTOs)
                .build();
    }

    /**
     * Lấy lịch sử chat AI theo email (cho admin)
     */
    public AIChatHistoryDTO getChatHistoryByEmail(String userEmail) {
        List<AIChatMessage> messages = aiChatMessageRepository.findByUserEmailOrderByTimestampAsc(userEmail);

        List<AIChatResponseDTO> messageDTOs = messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return AIChatHistoryDTO.builder()
                .userEmail(userEmail)
                .totalMessages(messages.size())
                .messages(messageDTOs)
                .build();
    }

    /**
     * Khởi tạo cuộc hội thoại AI mới
     */
    @Transactional
    public AIChatResponseDTO startNewChat() {
        String userEmail = getCurrentUserEmail();
        String sessionId = generateSessionId();

        String welcomeContent = "Xin chào! Tôi là trợ lý ảo của Busify. " +
                "Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. " +
                "Bạn cần hỗ trợ gì hôm nay?";

        AIChatMessage welcomeMessage = saveMessage(userEmail, welcomeContent, AIChatMessage.MessageRole.ASSISTANT,
                sessionId);

        log.info("Started new AI chat session for user: {}", userEmail);

        return AIChatResponseDTO.builder()
                .id(welcomeMessage.getId())
                .message(welcomeContent)
                .role("ASSISTANT")
                .timestamp(welcomeMessage.getTimestamp())
                .shouldTransferToHuman(false)
                .aiSource(AISource.WELCOME.name())
                .build();
    }

    /**
     * Xóa lịch sử chat AI của user hiện tại
     */
    @Transactional
    public void clearChatHistory() {
        String userEmail = getCurrentUserEmail();
        aiChatMessageRepository.deleteByUserEmail(userEmail);
        log.info("Cleared AI chat history for user: {}", userEmail);
    }

    /**
     * Kiểm tra trạng thái AI service
     */
    public boolean checkAIStatus() {
        return aiService.isAvailable();
    }

    // ==================== Private methods ====================

    private AIChatMessage saveMessage(String userEmail, String content, AIChatMessage.MessageRole role,
            String sessionId) {
        AIChatMessage message = AIChatMessage.builder()
                .userEmail(userEmail)
                .content(content)
                .role(role)
                .sessionId(sessionId)
                .timestamp(LocalDateTime.now())
                .build();
        return aiChatMessageRepository.save(message);
    }

    private String getCurrentUserEmail() {
        return jwtUtils.getCurrentUserLogin().orElse("anonymous");
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private AIChatResponseDTO convertToDTO(AIChatMessage message) {
        return AIChatResponseDTO.builder()
                .id(message.getId())
                .message(message.getContent())
                .role(message.getRole().name())
                .timestamp(message.getTimestamp())
                .shouldTransferToHuman(false)
                .build();
    }
}
