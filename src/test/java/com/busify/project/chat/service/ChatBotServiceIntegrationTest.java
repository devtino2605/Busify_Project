package com.busify.project.chat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "openai.api.key=test-key",
    "openai.api.model=gpt-3.5-turbo",
    "openai.api.timeout=30",
    "openai.api.max-tokens=100",
    "openai.api.temperature=0.7"
})
@Slf4j
public class ChatBotServiceIntegrationTest {

    @Autowired
    private ChatBotService chatBotService;

    @Autowired
    private OpenAIService openAIService;

    @Test
    public void testChatBotServiceNotNull() {
        assertNotNull(chatBotService);
        log.info("ChatBotService bean loaded successfully");
    }

    @Test
    public void testOpenAIServiceNotNull() {
        assertNotNull(openAIService);
        log.info("OpenAIService bean loaded successfully");
    }

    @Test
    public void testOpenAIServiceAvailability() {
        // Với test key, OpenAI service sẽ không available
        boolean isAvailable = openAIService.isOpenAIAvailable();
        log.info("OpenAI service available: {}", isAvailable);
        
        // Expected false vì đang dùng test key
        assertFalse(isAvailable);
    }

    @Test
    public void testGetBotReplyFallback() {
        String userMessage = "Xin chào";
        String userEmail = "test@example.com";
        
        String reply = chatBotService.getBotReply(userMessage, userEmail);
        
        assertNotNull(reply);
        assertFalse(reply.isEmpty());
        assertTrue(reply.contains("Xin chào"));
        log.info("Bot reply: {}", reply);
    }

    @Test
    public void testGetBotReplyBooking() {
        String userMessage = "Tôi muốn đặt vé xe";
        String userEmail = "test@example.com";
        
        String reply = chatBotService.getBotReply(userMessage, userEmail);
        
        assertNotNull(reply);
        assertTrue(reply.contains("đặt vé"));
        log.info("Bot reply for booking: {}", reply);
    }

    @Test
    public void testGetBotReplyPrice() {
        String userMessage = "Giá vé bao nhiêu?";
        String userEmail = "test@example.com";
        
        String reply = chatBotService.getBotReply(userMessage, userEmail);
        
        assertNotNull(reply);
        assertTrue(reply.contains("giá") || reply.contains("Giá"));
        log.info("Bot reply for price: {}", reply);
    }

    @Test
    public void testShouldTransferToHuman() {
        assertTrue(chatBotService.shouldTransferToHuman("Tôi cần gặp nhân viên"));
        assertTrue(chatBotService.shouldTransferToHuman("Có vấn đề với vé"));
        assertTrue(chatBotService.shouldTransferToHuman("Khiếu nại"));
        
        assertFalse(chatBotService.shouldTransferToHuman("Xin chào"));
        assertFalse(chatBotService.shouldTransferToHuman("Giá vé bao nhiêu?"));
        
        log.info("Transfer to human logic working correctly");
    }
}