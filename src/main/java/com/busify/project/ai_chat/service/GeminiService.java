package com.busify.project.ai_chat.service;

import com.busify.project.ai_chat.config.GeminiConfig;
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

/**
 * Service gọi Google Gemini API
 * Hỗ trợ Function Calling để truy vấn dữ liệu từ database
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private static final String GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/v1beta";
    private final GeminiConfig geminiConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ==================== Request/Response DTOs ====================

    @Data
    public static class GeminiRequest {
        private List<Content> contents;
        private Content systemInstruction;
        private GenerationConfig generationConfig;
        private List<SafetySetting> safetySettings;
        private List<Tool> tools; // Function Calling tools
    }

    @Data
    public static class FunctionDeclaration {
        private String name;
        private String description;
        private Schema parameters;
    }

    @Data
    public static class Schema {
        private String type;
        private String description;
        private Map<String, Schema> properties;
        private List<String> required;
    }

    @Data
    public static class Tool {
        private List<FunctionDeclaration> functionDeclarations;

        public Tool() {
        }

        public Tool(List<FunctionDeclaration> functionDeclarations) {
            this.functionDeclarations = functionDeclarations;
        }
    }

    @Data
    public static class Content {
        private String role;
        private List<Part> parts;

        public Content() {
        }

        public Content(String role, String text) {
            this.role = role;
            this.parts = List.of(new Part(text));
        }

        public Content(String role, List<Part> parts) {
            this.role = role;
            this.parts = parts;
        }

        public static Content ofText(String text) {
            Content content = new Content();
            content.setParts(List.of(new Part(text)));
            return content;
        }

        public static Content ofFunctionResponse(String functionName, Object response) {
            Content content = new Content();
            content.setRole("function");
            Part part = new Part();
            part.setFunctionResponse(new FunctionResponsePart(functionName, response));
            content.setParts(List.of(part));
            return content;
        }
    }

    @Data
    public static class Part {
        private String text;
        private FunctionCallPart functionCall;
        private FunctionResponsePart functionResponse;

        public Part() {
        }

        public Part(String text) {
            this.text = text;
        }
    }

    @Data
    public static class FunctionCallPart {
        private String name;
        private Map<String, Object> args;
    }

    @Data
    public static class FunctionResponsePart {
        private String name;
        private Object response;

        public FunctionResponsePart() {
        }

        public FunctionResponsePart(String name, Object response) {
            this.name = name;
            this.response = response;
        }
    }

    @Data
    public static class GenerationConfig {
        private Double temperature;
        private Integer maxOutputTokens;
        private Double topP;
        private Integer topK;
    }

    @Data
    public static class SafetySetting {
        private String category;
        private String threshold;
    }

    @Data
    public static class GeminiResponse {
        private List<Candidate> candidates;
        private UsageMetadata usageMetadata;
    }

    @Data
    public static class Candidate {
        private Content content;
        private String finishReason;
        private Integer index;
        private List<SafetyRating> safetyRatings;
    }

    @Data
    public static class SafetyRating {
        private String category;
        private String probability;
    }

    @Data
    public static class UsageMetadata {
        private Integer promptTokenCount;
        private Integer candidatesTokenCount;
        private Integer totalTokenCount;
    }

    // ==================== Main Methods ====================

    /**
     * Gọi Gemini API để lấy chat completion
     * Sử dụng systemInstruction theo đúng API spec của Google Gemini
     */
    public String getChatCompletion(String userMessage, String systemPrompt) {
        try {
            String url = String.format("%s/models/%s:generateContent?key=%s",
                    GEMINI_BASE_URL, geminiConfig.getModel(), geminiConfig.getKey());

            WebClient webClient = WebClient.builder()
                    .defaultHeader("Content-Type", "application/json")
                    .build();

            // Tạo request theo đúng Gemini API spec
            GeminiRequest request = new GeminiRequest();

            // System instruction riêng biệt (đúng theo API spec)
            request.setSystemInstruction(Content.ofText(systemPrompt));

            // User message
            request.setContents(List.of(new Content("user", userMessage)));

            // Generation config
            GenerationConfig genConfig = new GenerationConfig();
            genConfig.setTemperature(geminiConfig.getTemperature());
            genConfig.setMaxOutputTokens(geminiConfig.getMaxTokens());
            genConfig.setTopP(0.95);
            genConfig.setTopK(40);
            request.setGenerationConfig(genConfig);

            log.info("Sending request to Gemini API with model: {}", geminiConfig.getModel());

            GeminiResponse response = webClient.post()
                    .uri(url)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .timeout(Duration.ofSeconds(geminiConfig.getTimeout()))
                    .block();

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                Candidate candidate = response.getCandidates().get(0);
                if (candidate.getContent() != null && candidate.getContent().getParts() != null
                        && !candidate.getContent().getParts().isEmpty()) {

                    String content = candidate.getContent().getParts().get(0).getText();

                    // Log usage
                    if (response.getUsageMetadata() != null) {
                        log.info("Gemini API usage - Prompt tokens: {}, Response tokens: {}, Total: {}",
                                response.getUsageMetadata().getPromptTokenCount(),
                                response.getUsageMetadata().getCandidatesTokenCount(),
                                response.getUsageMetadata().getTotalTokenCount());
                    }

                    log.info("Received response from Gemini API successfully");
                    return content;
                }
            }

            log.warn("No response content from Gemini API");
            return null;

        } catch (WebClientResponseException e) {
            log.error("Gemini API error - Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            parseAndLogError(e);
            return null;
        } catch (Exception e) {
            log.error("Error calling Gemini API", e);
            return null;
        }
    }

    /**
     * Gọi Gemini API với lịch sử hội thoại
     * Sử dụng systemInstruction theo đúng API spec của Google Gemini
     */
    public String getChatCompletionWithHistory(String userMessage, List<Content> chatHistory, String systemPrompt) {
        try {
            String url = String.format("%s/models/%s:generateContent?key=%s",
                    GEMINI_BASE_URL, geminiConfig.getModel(), geminiConfig.getKey());

            WebClient webClient = WebClient.builder()
                    .defaultHeader("Content-Type", "application/json")
                    .build();

            GeminiRequest request = new GeminiRequest();

            // System instruction riêng biệt (đúng theo API spec)
            request.setSystemInstruction(Content.ofText(systemPrompt));

            // Contents: lịch sử chat + tin nhắn hiện tại
            List<Content> contents = new java.util.ArrayList<>();

            // Thêm lịch sử chat
            contents.addAll(chatHistory);

            // Thêm tin nhắn hiện tại
            contents.add(new Content("user", userMessage));

            request.setContents(contents);

            // Generation config
            GenerationConfig genConfig = new GenerationConfig();
            genConfig.setTemperature(geminiConfig.getTemperature());
            genConfig.setMaxOutputTokens(geminiConfig.getMaxTokens());
            genConfig.setTopP(0.95);
            genConfig.setTopK(40);
            request.setGenerationConfig(genConfig);

            log.info("Sending request with history to Gemini API, messages: {}", contents.size());

            GeminiResponse response = webClient.post()
                    .uri(url)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .timeout(Duration.ofSeconds(geminiConfig.getTimeout()))
                    .block();

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                Candidate candidate = response.getCandidates().get(0);
                if (candidate.getContent() != null && candidate.getContent().getParts() != null
                        && !candidate.getContent().getParts().isEmpty()) {
                    return candidate.getContent().getParts().get(0).getText();
                }
            }

            return null;

        } catch (Exception e) {
            log.error("Error calling Gemini API with history", e);
            return null;
        }
    }

    /**
     * Gọi Gemini API với Function Calling
     * AI có thể gọi các function để truy vấn dữ liệu thực từ database
     * 
     * @return GeminiResponse để có thể kiểm tra function calls
     */
    public GeminiResponse getChatCompletionWithFunctions(String userMessage, List<Content> chatHistory,
            String systemPrompt, List<FunctionDeclaration> functions) {
        try {
            String url = String.format("%s/models/%s:generateContent?key=%s",
                    GEMINI_BASE_URL, geminiConfig.getModel(), geminiConfig.getKey());

            WebClient webClient = WebClient.builder()
                    .defaultHeader("Content-Type", "application/json")
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                    .build();

            GeminiRequest request = new GeminiRequest();

            // System instruction
            request.setSystemInstruction(Content.ofText(systemPrompt));

            // Contents
            List<Content> contents = new java.util.ArrayList<>();
            if (chatHistory != null) {
                contents.addAll(chatHistory);
            }
            contents.add(new Content("user", userMessage));
            request.setContents(contents);

            // Tools (Functions)
            if (functions != null && !functions.isEmpty()) {
                Tool tool = new Tool(functions);
                request.setTools(List.of(tool));
            }

            // Generation config
            GenerationConfig genConfig = new GenerationConfig();
            genConfig.setTemperature(geminiConfig.getTemperature());
            genConfig.setMaxOutputTokens(geminiConfig.getMaxTokens());
            genConfig.setTopP(0.95);
            genConfig.setTopK(40);
            request.setGenerationConfig(genConfig);

            log.info("Sending request with functions to Gemini API, functions count: {}",
                    functions != null ? functions.size() : 0);

            return webClient.post()
                    .uri(url)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .timeout(Duration.ofSeconds(geminiConfig.getTimeout()))
                    .block();

        } catch (Exception e) {
            log.error("Error calling Gemini API with functions", e);
            return null;
        }
    }

    /**
     * Tiếp tục conversation sau khi có function response
     */
    public GeminiResponse continueWithFunctionResponse(List<Content> conversationHistory,
            String systemPrompt, List<FunctionDeclaration> functions) {
        try {
            String url = String.format("%s/models/%s:generateContent?key=%s",
                    GEMINI_BASE_URL, geminiConfig.getModel(), geminiConfig.getKey());

            WebClient webClient = WebClient.builder()
                    .defaultHeader("Content-Type", "application/json")
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                    .build();

            GeminiRequest request = new GeminiRequest();
            request.setSystemInstruction(Content.ofText(systemPrompt));
            request.setContents(conversationHistory);

            if (functions != null && !functions.isEmpty()) {
                Tool tool = new Tool(functions);
                request.setTools(List.of(tool));
            }

            GenerationConfig genConfig = new GenerationConfig();
            genConfig.setTemperature(geminiConfig.getTemperature());
            genConfig.setMaxOutputTokens(geminiConfig.getMaxTokens());
            request.setGenerationConfig(genConfig);

            log.info("Continuing conversation with function response");

            return webClient.post()
                    .uri(url)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .timeout(Duration.ofSeconds(geminiConfig.getTimeout()))
                    .block();

        } catch (Exception e) {
            log.error("Error continuing with function response", e);
            return null;
        }
    }

    /**
     * Kiểm tra xem response có chứa function call không
     */
    public boolean hasFunctionCall(GeminiResponse response) {
        if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
            return false;
        }
        Candidate candidate = response.getCandidates().get(0);
        if (candidate.getContent() == null || candidate.getContent().getParts() == null) {
            return false;
        }
        return candidate.getContent().getParts().stream()
                .anyMatch(part -> part.getFunctionCall() != null);
    }

    /**
     * Lấy function call từ response
     */
    public FunctionCallPart getFunctionCall(GeminiResponse response) {
        if (!hasFunctionCall(response)) {
            return null;
        }
        return response.getCandidates().get(0).getContent().getParts().stream()
                .filter(part -> part.getFunctionCall() != null)
                .findFirst()
                .map(Part::getFunctionCall)
                .orElse(null);
    }

    /**
     * Lấy text response từ response
     */
    public String getTextResponse(GeminiResponse response) {
        if (response == null || response.getCandidates() == null || response.getCandidates().isEmpty()) {
            return null;
        }
        Candidate candidate = response.getCandidates().get(0);
        if (candidate.getContent() == null || candidate.getContent().getParts() == null) {
            return null;
        }
        return candidate.getContent().getParts().stream()
                .filter(part -> part.getText() != null)
                .findFirst()
                .map(Part::getText)
                .orElse(null);
    }

    /**
     * Kiểm tra Gemini service có sẵn sàng không
     */
    public boolean isAvailable() {
        return geminiConfig.isConfigured();
    }

    private void parseAndLogError(WebClientResponseException e) {
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), Map.class);
            Object error = errorResponse.get("error");
            if (error instanceof Map) {
                Map<String, Object> errorMap = (Map<String, Object>) error;
                String message = (String) errorMap.get("message");
                Integer code = (Integer) errorMap.get("code");
                log.error("Gemini error - Code: {}, Message: {}", code, message);
            }
        } catch (Exception parseError) {
            log.error("Failed to parse Gemini error response");
        }
    }
}
