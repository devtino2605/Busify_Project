package com.busify.project.chat.service;

import org.springframework.stereotype.Service;

import com.busify.project.chat.config.OpenAIConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {

    private final OpenAIConfig openAIConfig;
    private final OpenRouterService openRouterService;

    /**
     * Gửi tin nhắn tới OpenRouter và nhận phản hồi
     */
    public String getChatGPTResponse(String userMessage, String userEmail) {
        try {
            log.info("Processing AI chat message from user: {}", userEmail);
            log.info("Using model: {}", openAIConfig.getModel());

            // Tạo system prompt cho Busify chatbot
            String systemPrompt = createBusifySystemPrompt();

            // Tạo danh sách tin nhắn cho OpenRouter
            List<OpenRouterService.Message> messages = List.of(
                new OpenRouterService.Message("system", systemPrompt),
                new OpenRouterService.Message("user", userMessage)
            );

            log.info("Sending request to OpenRouter for user: {}", userEmail);
            
            // Gọi OpenRouter API
            String response = openRouterService.getChatCompletion(
                openAIConfig.getKey(),
                openAIConfig.getModel(),
                messages,
                openAIConfig.getMaxTokens(),
                openAIConfig.getTemperature()
            );
            
            if (response != null && !response.trim().isEmpty()) {
                log.info("Received response from OpenRouter for user: {}", userEmail);
                return response;
            } else {
                log.warn("No response from OpenRouter for user: {}", userEmail);
                return null;
            }

        } catch (Exception e) {
            log.error("Error calling OpenRouter API for user: {}", userEmail, e);
            return null; // Fallback to mock response
        }
    }

    /**
     * Tạo system prompt cho Busify chatbot
     */
    private String createBusifySystemPrompt() {
        return """
            Bạn là trợ lý ảo của Busify - hệ thống đặt vé xe khách trực tuyến tại Việt Nam.
            
            Thông tin về Busify:
            - Busify là nền tảng đặt vé xe khách trực tuyến
            - Hỗ trợ đặt vé cho các tuyến đường trong nước
            - Có nhiều loại xe: xe thường, xe VIP, xe giường nằm
            - Hỗ trợ thanh toán qua VNPay, PayPal, chuyển khoản
            - Có chính sách hủy vé linh hoạt
            
            Vai trò của bạn:
            1. Hỗ trợ khách hàng về việc đặt vé xe
            2. Tư vấn giá vé, lịch trình, tuyến đường
            3. Hướng dẫn quy trình đặt vé và thanh toán
            4. Giải đáp thắc mắc về chính sách hủy vé, hoàn tiền
            5. Cung cấp thông tin về các khuyến mãi
            
            Hướng dẫn trả lời:
            - Trả lời bằng tiếng Việt, thân thiện và chuyên nghiệp
            - Cung cấp thông tin chính xác và hữu ích
            - Nếu không biết thông tin cụ thể, hãy gợi ý liên hệ nhân viên hỗ trợ
            - Luôn kết thúc với câu hỏi để tiếp tục hỗ trợ khách hàng
            - Giới hạn câu trả lời trong 300 từ
            
            Nếu khách hàng hỏi về:
            - Đặt vé: Hướng dẫn quy trình từng bước
            - Giá vé: Giải thích các yếu tố ảnh hưởng đến giá
            - Hủy vé: Nêu rõ chính sách hoàn tiền theo thời gian
            - Thanh toán: Liệt kê các phương thức có sẵn
            - Khiếu nại/vấn đề: Gợi ý chuyển cho nhân viên hỗ trợ
            """;
    }

    /**
     * Kiểm tra xem OpenRouter service có sẵn sàng không
     */
    public boolean isOpenAIAvailable() {
        return openAIConfig.getKey() != null && !openAIConfig.getKey().trim().isEmpty();
    }

    /**
     * Gửi tin nhắn tới OpenRouter với context từ lịch sử hội thoại
     */
    public String getChatGPTResponseWithHistory(String userMessage, List<com.busify.project.chat.model.ChatMessage> chatHistory, String userEmail) {
        try {
            log.info("Processing AI chat message with history from user: {}", userEmail);
            log.info("Using model: {}", openAIConfig.getModel());

            // Tạo system prompt
            String systemPrompt = createBusifySystemPrompt();
            
            // Tạo danh sách tin nhắn với lịch sử
            List<OpenRouterService.Message> messages = new java.util.ArrayList<>();
            messages.add(new OpenRouterService.Message("system", systemPrompt));
            
            // Thêm lịch sử hội thoại (giới hạn 10 tin nhắn gần nhất để tránh vượt quá token limit)
            int historyLimit = Math.min(chatHistory.size(), 10);
            for (int i = chatHistory.size() - historyLimit; i < chatHistory.size(); i++) {
                com.busify.project.chat.model.ChatMessage msg = chatHistory.get(i);
                String role = msg.getSender().equals("AI Bot") ? "assistant" : "user";
                
                OpenRouterService.Message historyMessage = new OpenRouterService.Message(role, msg.getContent());
                messages.add(historyMessage);
            }
            
            // Thêm tin nhắn hiện tại
            messages.add(new OpenRouterService.Message("user", userMessage));

            log.info("Sending request with history to OpenRouter for user: {}", userEmail);
            
            // Gọi OpenRouter API
            String response = openRouterService.getChatCompletion(
                openAIConfig.getKey(),
                openAIConfig.getModel(),
                messages,
                openAIConfig.getMaxTokens(),
                openAIConfig.getTemperature()
            );
            
            if (response != null && !response.trim().isEmpty()) {
                log.info("Received response with history from OpenRouter for user: {}", userEmail);
                return response;
            } else {
                log.warn("No response with history from OpenRouter for user: {}", userEmail);
                return null;
            }

        } catch (Exception e) {
            log.error("Error calling OpenRouter API with history for user: {}", userEmail, e);
            return null; // Fallback to simple response
        }
    }
}