package com.busify.project.chat.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenRouterService {

    private static final String OPENROUTER_BASE_URL = "https://openrouter.ai/api/v1";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Data
    public static class OpenRouterRequest {
        private String model;
        private List<Message> messages;
        
        @JsonProperty("max_tokens")
        private Integer maxTokens;
        
        private Double temperature;
        private Boolean stream = false;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
        
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    @Data
    public static class OpenRouterResponse {
        private String id;
        private String object;
        private Long created;
        private String model;
        private List<Choice> choices;
        private Usage usage;
    }

    @Data
    public static class Choice {
        private Integer index;
        private Message message;
        
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Data
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;
        
        @JsonProperty("completion_tokens")
        private Integer completionTokens;
        
        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }

    public String getChatCompletion(String apiKey, String model, List<Message> messages, 
                                   Integer maxTokens, Double temperature) {
        try {
            WebClient webClient = WebClient.builder()
                .baseUrl(OPENROUTER_BASE_URL)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("HTTP-Referer", "http://localhost:8080") // OpenRouter yêu cầu
                .defaultHeader("X-Title", "Busify Chatbot") // OpenRouter yêu cầu
                .build();

            OpenRouterRequest request = new OpenRouterRequest();
            request.setModel(model);
            request.setMessages(messages);
            request.setMaxTokens(maxTokens);
            request.setTemperature(temperature);

            log.info("Sending request to OpenRouter API with model: {}", model);
            log.debug("Request messages count: {}", messages.size());

            OpenRouterResponse response = webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenRouterResponse.class)
                .timeout(Duration.ofSeconds(30))
                .block();

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String content = response.getChoices().get(0).getMessage().getContent();
                
                // Log usage info
                if (response.getUsage() != null) {
                    log.info("OpenRouter API usage - Prompt tokens: {}, Completion tokens: {}, Total tokens: {}", 
                        response.getUsage().getPromptTokens(),
                        response.getUsage().getCompletionTokens(),
                        response.getUsage().getTotalTokens());
                }
                
                log.info("Received response from OpenRouter API successfully");
                return content;
            } else {
                log.warn("No response content from OpenRouter API");
                return null;
            }

        } catch (WebClientResponseException e) {
            log.error("OpenRouter API error - Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            
            // Parse error response để hiển thị thông tin hữu ích
            try {
                Map<String, Object> errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
                Object error = errorResponse.get("error");
                if (error instanceof Map) {
                    Map<String, Object> errorMap = (Map<String, Object>) error;
                    String message = (String) errorMap.get("message");
                    String code = (String) errorMap.get("code");
                    log.error("OpenRouter error - Code: {}, Message: {}", code, message);
                }
            } catch (Exception parseError) {
                log.error("Failed to parse error response");
            }
            
            return null;
        } catch (Exception e) {
            log.error("Error calling OpenRouter API", e);
            return null;
        }
    }
}