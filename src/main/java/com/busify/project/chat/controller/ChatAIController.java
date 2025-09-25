package com.busify.project.chat.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.chat.dto.ChatMessageDTO;
import com.busify.project.chat.model.ChatMessage;
import com.busify.project.chat.service.ChatBotService;
import com.busify.project.chat.service.ChatService;
import com.busify.project.chat.service.OpenAIService;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.common.utils.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/ai-chat")
@RequiredArgsConstructor
@Slf4j
public class ChatAIController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ChatBotService chatBotService;
    private final JwtUtils jwtUtils;
    private final OpenAIService openAIService;

    /**
     * Xử lý tin nhắn chat với AI qua WebSocket.
     * Client gửi tin nhắn đến "/app/chat.ai/{userId}".
     * Server sẽ trả lời tự động và broadcast đến "/topic/ai/{userId}".
     */
    @MessageMapping("/chat.ai/{userId}")
    public void chatWithAI(@DestinationVariable String userId, @Payload ChatMessageDTO chatMessage) {
        try {
            String roomId = "ai-" + userId;
            log.info("Received AI chat message from user: {} in room: {}", chatMessage.getSender(), roomId);

            // 1. Lưu tin nhắn của người dùng
            ChatMessage savedUserMessage = chatService.saveMessage(chatMessage, roomId);
            log.info("Saved user message with ID: {}", savedUserMessage.getId());

            // 2. Kiểm tra xem có cần chuyển cho nhân viên không
            if (chatBotService.shouldTransferToHuman(chatMessage.getContent())) {
                // Gửi tin nhắn thông báo sẽ chuyển cho nhân viên
                ChatMessageDTO transferMessage = ChatMessageDTO.builder()
                    .content("Tôi thấy bạn cần hỗ trợ từ nhân viên. Đang chuyển cuộc trò chuyện cho nhân viên hỗ trợ...")
                    .sender("AI Bot")
                    .recipient(chatMessage.getSender())
                    .type(ChatMessageDTO.MessageType.CHAT)
                    .build();
                
                ChatMessage savedTransferMessage = chatService.saveMessage(transferMessage, roomId);
                messagingTemplate.convertAndSend("/topic/ai/" + userId, savedTransferMessage);
                
                // TODO: Implement logic để chuyển cho nhân viên thật
                return;
            }

            // 3. Lấy phản hồi từ AI (với lịch sử nếu có)
            List<ChatMessage> chatHistory = chatService.getChatHistoryByRoom(roomId);
            String aiReply;
            
            if (chatHistory.size() > 1) {
                // Có lịch sử chat, sử dụng context
                aiReply = chatBotService.getBotReplyWithHistory(chatMessage.getContent(), chatHistory, chatMessage.getSender());
            } else {
                // Không có lịch sử, sử dụng reply thông thường
                aiReply = chatBotService.getBotReply(chatMessage.getContent(), chatMessage.getSender());
            }

            // 4. Tạo tin nhắn phản hồi từ AI
            ChatMessageDTO aiMessage = ChatMessageDTO.builder()
                .content(aiReply)
                .sender("AI Bot")
                .recipient(chatMessage.getSender())
                .type(ChatMessageDTO.MessageType.CHAT)
                .build();

            // 5. Lưu tin nhắn AI
            ChatMessage savedAiMessage = chatService.saveMessage(aiMessage, roomId);
            log.info("Saved AI message with ID: {}", savedAiMessage.getId());

            // 6. Gửi phản hồi AI đến client
            messagingTemplate.convertAndSend("/topic/ai/" + userId, savedAiMessage);
            log.info("Sent AI reply to topic: /topic/ai/{}", userId);

        } catch (Exception e) {
            log.error("Error processing AI chat message from user: {}", chatMessage.getSender(), e);
            
            // Gửi tin nhắn lỗi
            ChatMessageDTO errorMessage = ChatMessageDTO.builder()
                .content("Xin lỗi, đã có lỗi xảy ra. Vui lòng thử lại sau.")
                .sender("AI Bot")
                .recipient(chatMessage.getSender())
                .type(ChatMessageDTO.MessageType.CHAT)
                .build();
            
            try {
                String roomId = "ai-" + userId;
                ChatMessage savedErrorMessage = chatService.saveMessage(errorMessage, roomId);
                messagingTemplate.convertAndSend("/topic/ai/" + userId, savedErrorMessage);
            } catch (Exception saveError) {
                log.error("Failed to save/send error message", saveError);
            }
        }
    }

    /**
     * REST API để gửi tin nhắn cho AI (alternative cho WebSocket)
     */
    @PostMapping("/send")
    public ApiResponse<ChatMessage> sendMessageToAI(@RequestBody ChatMessageDTO chatMessage) {
        try {
            String currentUser = jwtUtils.getCurrentUserLogin().orElse("anonymous");
            String roomId = "ai-" + currentUser;
            
            log.info("REST API: Received AI chat message from user: {}", currentUser);

            // Lưu tin nhắn người dùng
            chatMessage.setSender(currentUser);
            chatService.saveMessage(chatMessage, roomId);

            // Lấy phản hồi AI (với lịch sử nếu có)
            List<ChatMessage> chatHistory = chatService.getChatHistoryByRoom(roomId);
            String aiReply;
            
            if (chatHistory.size() > 1) {
                aiReply = chatBotService.getBotReplyWithHistory(chatMessage.getContent(), chatHistory, currentUser);
            } else {
                aiReply = chatBotService.getBotReply(chatMessage.getContent(), currentUser);
            }

            // Tạo và lưu tin nhắn AI
            ChatMessageDTO aiMessage = ChatMessageDTO.builder()
                .content(aiReply)
                .sender("AI Bot")
                .recipient(currentUser)
                .type(ChatMessageDTO.MessageType.CHAT)
                .build();

            ChatMessage savedAiMessage = chatService.saveMessage(aiMessage, roomId);

            // Gửi qua WebSocket nếu có kết nối
            messagingTemplate.convertAndSend("/topic/ai/" + currentUser, savedAiMessage);

            return ApiResponse.success("Tin nhắn đã được gửi thành công", savedAiMessage);

        } catch (Exception e) {
            log.error("Error in REST API chat with AI", e);
            return ApiResponse.internalServerError("Lỗi khi gửi tin nhắn: " + e.getMessage());
        }
    }

    /**
     * Lấy lịch sử chat với AI của người dùng hiện tại
     */
    @GetMapping("/history")
    @ResponseBody
    public ApiResponse<List<ChatMessage>> getAIChatHistory() {
        try {
            String currentUser = jwtUtils.getCurrentUserLogin().orElse("anonymous");
            String roomId = "ai-" + currentUser;
            
            List<ChatMessage> history = chatService.getChatHistoryByRoom(roomId);
            log.info("Retrieved AI chat history for user: {}, messages count: {}", currentUser, history.size());
            
            return ApiResponse.success("Lấy lịch sử chat AI thành công", history);

        } catch (Exception e) {
            log.error("Error retrieving AI chat history", e);
            return ApiResponse.internalServerError("Lỗi khi lấy lịch sử chat: " + e.getMessage());
        }
    }

    /**
     * Lấy lịch sử chat với AI theo userId (cho admin)
     */
    @GetMapping("/history/{userId}")
    @ResponseBody
    public ApiResponse<List<ChatMessage>> getAIChatHistoryByUserId(@PathVariable String userId) {
        try {
            String roomId = "ai-" + userId;
            List<ChatMessage> history = chatService.getChatHistoryByRoom(roomId);
            
            log.info("Retrieved AI chat history for userId: {}, messages count: {}", userId, history.size());
            return ApiResponse.success("Lấy lịch sử chat AI thành công", history);

        } catch (Exception e) {
            log.error("Error retrieving AI chat history for userId: {}", userId, e);
            return ApiResponse.internalServerError("Lỗi khi lấy lịch sử chat: " + e.getMessage());
        }
    }

    /**
     * Khởi tạo cuộc trò chuyện với AI
     */
    @PostMapping("/start")
    public ApiResponse<String> startAIChat() {
        try {
            String currentUser = jwtUtils.getCurrentUserLogin().orElse("anonymous");
            String roomId = "ai-" + currentUser;
            
            // Tạo tin nhắn chào mừng từ AI
            ChatMessageDTO welcomeMessage = ChatMessageDTO.builder()
                .content("Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp bạn về việc đặt vé, tra cứu lịch trình, giá vé và nhiều thông tin khác. Bạn cần hỗ trợ gì hôm nay?")
                .sender("AI Bot")
                .recipient(currentUser)
                .type(ChatMessageDTO.MessageType.CHAT)
                .build();

            ChatMessage savedWelcomeMessage = chatService.saveMessage(welcomeMessage, roomId);
            
            // Gửi qua WebSocket
            messagingTemplate.convertAndSend("/topic/ai/" + currentUser, savedWelcomeMessage);
            
            log.info("Started AI chat for user: {}", currentUser);
            return ApiResponse.success("Đã khởi tạo cuộc trò chuyện với AI", roomId);

        } catch (Exception e) {
            log.error("Error starting AI chat", e);
            return ApiResponse.internalServerError("Lỗi khi khởi tạo chat: " + e.getMessage());
        }
    }

    /**
     * Kiểm tra trạng thái OpenAI API
     */
    @GetMapping("/status")
    public ApiResponse<String> checkOpenAIStatus() {
        try {
            boolean isAvailable = openAIService.isOpenAIAvailable();
            if (isAvailable) {
                return ApiResponse.success("OpenAI API đang hoạt động", "OpenAI Service is available");
            } else {
                return ApiResponse.success("OpenAI API không khả dụng, sử dụng phản hồi fallback", "Using fallback responses");
            }
        } catch (Exception e) {
            log.error("Error checking OpenAI status", e);
            return ApiResponse.internalServerError("Lỗi khi kiểm tra trạng thái OpenAI: " + e.getMessage());
        }
    }
}