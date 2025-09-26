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

            // 1. Xử lý tin nhắn của người dùng (không lưu lịch sử)
            log.info("Processing user message (not saving to DB)");

            // 2. Kiểm tra xem có cần chuyển cho nhân viên không
            if (chatBotService.shouldTransferToHuman(chatMessage.getContent())) {
                // Gửi tin nhắn thông báo sẽ chuyển cho nhân viên
                ChatMessageDTO transferMessage = ChatMessageDTO.builder()
                    .content("Tôi thấy bạn cần hỗ trợ từ nhân viên. Đang chuyển cuộc trò chuyện cho nhân viên hỗ trợ...")
                    .sender("AI Bot")
                    .recipient(chatMessage.getSender())
                    .type(ChatMessageDTO.MessageType.CHAT)
                    .build();
                
                ChatMessage transferMsg = chatService.saveAIMessage(transferMessage, roomId);
                messagingTemplate.convertAndSend("/topic/ai/" + userId, transferMsg);
                
                // TODO: Implement logic để chuyển cho nhân viên thật
                return;
            }

            // 3. Lấy phản hồi từ AI (không sử dụng lịch sử vì không lưu)
            String aiReply = chatBotService.getBotReply(chatMessage.getContent(), chatMessage.getSender());

            // 4. Tạo tin nhắn phản hồi từ AI
            ChatMessageDTO aiMessage = ChatMessageDTO.builder()
                .content(aiReply)
                .sender("AI Bot")
                .recipient(chatMessage.getSender())
                .type(ChatMessageDTO.MessageType.CHAT)
                .build();

            // 5. Xử lý tin nhắn AI (không lưu database)
            ChatMessage aiMessageObj = chatService.saveAIMessage(aiMessage, roomId);
            log.info("Processed AI message (not saved to DB)");

            // 6. Gửi phản hồi AI đến client
            messagingTemplate.convertAndSend("/topic/ai/" + userId, aiMessageObj);
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
                ChatMessage errorMsg = chatService.saveAIMessage(errorMessage, roomId);
                messagingTemplate.convertAndSend("/topic/ai/" + userId, errorMsg);
            } catch (Exception saveError) {
                log.error("Failed to process/send error message", saveError);
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

            // Xử lý tin nhắn người dùng (không lưu database)
            chatMessage.setSender(currentUser);
            log.info("Processing user message via REST API (not saving to DB)");

            // Lấy phản hồi AI (không sử dụng lịch sử)
            String aiReply = chatBotService.getBotReply(chatMessage.getContent(), currentUser);

            // Tạo tin nhắn AI (không lưu database)
            ChatMessageDTO aiMessage = ChatMessageDTO.builder()
                .content(aiReply)
                .sender("AI Bot")
                .recipient(currentUser)
                .type(ChatMessageDTO.MessageType.CHAT)
                .build();

            ChatMessage aiMessageObj = chatService.saveAIMessage(aiMessage, roomId);

            // Gửi qua WebSocket nếu có kết nối
            messagingTemplate.convertAndSend("/topic/ai/" + currentUser, aiMessageObj);

            return ApiResponse.success("Tin nhắn đã được gửi thành công", aiMessageObj);

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
            
            // Không lưu lịch sử AI chat nên trả về list rỗng
            List<ChatMessage> history = chatService.getAIChatHistory(currentUser);
            log.info("AI chat history not stored, returning empty list for user: {}", currentUser);
            
            return ApiResponse.success("Không lưu lịch sử chat AI", history);

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
            // Không lưu lịch sử AI chat nên trả về list rỗng
            List<ChatMessage> history = chatService.getAIChatHistory(userId);
            
            log.info("AI chat history not stored, returning empty list for userId: {}", userId);
            return ApiResponse.success("Không lưu lịch sử chat AI", history);

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

            ChatMessage welcomeMsg = chatService.saveAIMessage(welcomeMessage, roomId);
            
            // Gửi qua WebSocket
            messagingTemplate.convertAndSend("/topic/ai/" + currentUser, welcomeMsg);
            
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