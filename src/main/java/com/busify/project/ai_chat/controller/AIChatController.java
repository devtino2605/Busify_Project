package com.busify.project.ai_chat.controller;

import com.busify.project.ai_chat.dto.AIChatHistoryDTO;
import com.busify.project.ai_chat.dto.AIChatRequestDTO;
import com.busify.project.ai_chat.dto.AIChatResponseDTO;
import com.busify.project.ai_chat.service.AIChatService;
import com.busify.project.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý AI Chat - hoàn toàn độc lập với Customer Service Chat
 * 
 * REST API endpoints: /api/ai-chat/*
 * WebSocket endpoint: /app/ai.chat/{userEmail}
 * WebSocket topic: /topic/ai-chat/{userEmail}
 */
@RestController
@RequestMapping("/api/ai-chat")
@RequiredArgsConstructor
@Slf4j
public class AIChatController {

    private final AIChatService aiChatService;
    private final SimpMessagingTemplate messagingTemplate;

    // ==================== WebSocket Endpoints ====================

    /**
     * Xử lý tin nhắn AI chat qua WebSocket
     * Client gửi đến: /app/ai.chat/{userEmail}
     * Server trả về: /topic/ai-chat/{userEmail}
     */
    @MessageMapping("/ai.chat/{userEmail}")
    public void handleAIChatMessage(@DestinationVariable String userEmail, @Payload AIChatRequestDTO request) {
        try {
            log.info("WebSocket: Received AI chat message from user: {}", userEmail);

            AIChatResponseDTO response = aiChatService.processUserMessage(request.getMessage(), request.getSessionId());

            // Gửi phản hồi qua WebSocket
            messagingTemplate.convertAndSend("/topic/ai-chat/" + userEmail, response);
            log.info("WebSocket: Sent AI reply to /topic/ai-chat/{}", userEmail);

        } catch (Exception e) {
            log.error("WebSocket: Error processing AI chat message from user: {}", userEmail, e);

            AIChatResponseDTO errorResponse = AIChatResponseDTO.builder()
                    .message("Xin lỗi, đã có lỗi xảy ra. Vui lòng thử lại sau.")
                    .role("SYSTEM")
                    .build();

            messagingTemplate.convertAndSend("/topic/ai-chat/" + userEmail, errorResponse);
        }
    }

    // ==================== REST API Endpoints ====================

    /**
     * Gửi tin nhắn cho AI (REST API alternative)
     * POST /api/ai-chat/send
     */
    @PostMapping("/send")
    public ApiResponse<AIChatResponseDTO> sendMessage(@Valid @RequestBody AIChatRequestDTO request) {
        try {
            log.info("REST: Processing AI chat message");

            AIChatResponseDTO response = aiChatService.processUserMessage(request.getMessage(), request.getSessionId());

            return ApiResponse.success("Tin nhắn đã được gửi thành công", response);

        } catch (Exception e) {
            log.error("REST: Error processing AI chat message", e);
            return ApiResponse.internalServerError("Lỗi khi gửi tin nhắn: " + e.getMessage());
        }
    }

    /**
     * Lấy lịch sử chat AI của user hiện tại
     * GET /api/ai-chat/history
     */
    @GetMapping("/history")
    public ApiResponse<AIChatHistoryDTO> getChatHistory() {
        try {
            AIChatHistoryDTO history = aiChatService.getChatHistory();
            log.info("Retrieved AI chat history, messages count: {}", history.getTotalMessages());
            return ApiResponse.success("Lấy lịch sử chat AI thành công", history);

        } catch (Exception e) {
            log.error("Error retrieving AI chat history", e);
            return ApiResponse.internalServerError("Lỗi khi lấy lịch sử chat: " + e.getMessage());
        }
    }

    /**
     * Lấy lịch sử chat AI theo email (cho admin)
     * GET /api/ai-chat/history/{userEmail}
     */
    @GetMapping("/history/{userEmail}")
    public ApiResponse<AIChatHistoryDTO> getChatHistoryByEmail(@PathVariable String userEmail) {
        try {
            AIChatHistoryDTO history = aiChatService.getChatHistoryByEmail(userEmail);
            log.info("Retrieved AI chat history for user: {}, messages count: {}", userEmail,
                    history.getTotalMessages());
            return ApiResponse.success("Lấy lịch sử chat AI thành công", history);

        } catch (Exception e) {
            log.error("Error retrieving AI chat history for user: {}", userEmail, e);
            return ApiResponse.internalServerError("Lỗi khi lấy lịch sử chat: " + e.getMessage());
        }
    }

    /**
     * Khởi tạo cuộc hội thoại AI mới
     * POST /api/ai-chat/start
     */
    @PostMapping("/start")
    public ApiResponse<AIChatResponseDTO> startNewChat() {
        try {
            AIChatResponseDTO response = aiChatService.startNewChat();
            log.info("Started new AI chat session");
            return ApiResponse.success("Đã khởi tạo cuộc trò chuyện với AI", response);

        } catch (Exception e) {
            log.error("Error starting AI chat", e);
            return ApiResponse.internalServerError("Lỗi khi khởi tạo chat: " + e.getMessage());
        }
    }

    /**
     * Xóa lịch sử chat AI của user hiện tại
     * DELETE /api/ai-chat/history
     */
    @DeleteMapping("/history")
    public ApiResponse<Void> clearChatHistory() {
        try {
            aiChatService.clearChatHistory();
            log.info("Cleared AI chat history");
            return ApiResponse.success("Đã xóa lịch sử chat AI", null);

        } catch (Exception e) {
            log.error("Error clearing AI chat history", e);
            return ApiResponse.internalServerError("Lỗi khi xóa lịch sử: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra trạng thái AI service
     * GET /api/ai-chat/status
     */
    @GetMapping("/status")
    public ApiResponse<String> checkStatus() {
        try {
            boolean isAvailable = aiChatService.checkAIStatus();
            if (isAvailable) {
                return ApiResponse.success("AI Service đang hoạt động", "AI Service is available");
            } else {
                return ApiResponse.success("AI Service không khả dụng, sử dụng phản hồi fallback",
                        "Using fallback responses");
            }
        } catch (Exception e) {
            log.error("Error checking AI status", e);
            return ApiResponse.internalServerError("Lỗi khi kiểm tra trạng thái: " + e.getMessage());
        }
    }

    /**
     * Health check endpoint
     * GET /api/ai-chat/health
     */
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.success("AI Chat Service is healthy", "OK");
    }
}
