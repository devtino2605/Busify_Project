# Hệ Thống AI Chat - Busify

Hệ thống chat AI tích hợp với **OpenRouter** sử dụng model **Meta Llama 3.3** để hỗ trợ khách hàng tự động.

## 🚀 Tính Năng

### ✅ Đã Hoàn Thành
- **AI Chat Controller**: REST API và WebSocket cho chat AI
- **OpenRouter Integration**: Tích hợp với Meta Llama 3.3 thông qua OpenRouter
- **Chat Bot Service**: Logic xử lý tin nhắn và phản hồi AI
- **Database Integration**: Lưu trữ lịch sử chat AI
- **Error Handling**: Xử lý lỗi và fallback responses
- **Security**: JWT authentication cho API endpoints
- **System Prompt**: Context đặc biệt cho Busify chatbot

### 🔄 Luồng Hoạt Động
1. User gửi tin nhắn qua REST API hoặc WebSocket
2. Hệ thống kiểm tra authentication
3. ChatBotService xử lý tin nhắn:
   - Gọi OpenRouter API với context Busify
   - Nếu API thất bại, sử dụng mock response
   - Kiểm tra xem có cần chuyển cho human support không
4. Lưu tin nhắn và phản hồi vào database
5. Trả kết quả cho client

## 🛠️ Technical Stack

### Backend Components
- **Spring Boot**: Framework chính
- **OpenRouter Service**: Custom HTTP client cho OpenRouter API  
- **WebSocket**: Real-time communication
- **MySQL**: Database lưu trữ
- **JWT**: Authentication & authorization

### AI Integration
- **OpenRouter**: AI API gateway
- **Model**: meta-llama/llama-3.3-8b-instruct:free
- **Custom HTTP Client**: Spring WebFlux WebClient
- **System Prompt**: Busify-specific context

## 📁 Cấu Trúc Code

```
src/main/java/com/busify/project/chat/
├── controller/
│   └── ChatAIController.java          # REST & WebSocket endpoints
├── service/
│   ├── ChatBotService.java           # Core AI logic
│   ├── OpenAIService.java            # AI API abstraction  
│   ├── OpenRouterService.java        # OpenRouter HTTP client
│   └── ChatService.java             # Database operations
├── config/
│   └── OpenAIConfig.java             # OpenRouter configuration
├── dto/
│   ├── AIChatRequestDTO.java         # Request models
│   └── AIChatResponseDTO.java        # Response models
└── model/
    └── ChatMessage.java              # Database entity
```

## ⚙️ Cấu Hình

### Application Properties
```properties
# OpenRouter configuration (compatible with OpenAI API)
openai.api.key=sk-or-v1-your-openrouter-key-here
openai.api.model=meta-llama/llama-3.3-8b-instruct:free
openai.api.base-url=https://openrouter.ai/api/v1
openai.api.timeout=30
openai.api.max-tokens=500
openai.api.temperature=0.7
```

### Dependencies (pom.xml)
```xml
<!-- Spring WebFlux for OpenRouter HTTP client -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

<!-- WebSocket support -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

## 📡 API Endpoints

### REST API
- `POST /api/ai-chat/send` - Gửi tin nhắn AI chat
- `GET /api/ai-chat/history/{userId}` - Lấy lịch sử chat

### WebSocket
- `/app/chat.ai/{userId}` - Real-time AI chat
- `/topic/ai-chat/{userId}` - Subscribe AI responses

## 🧪 Testing

### Test File: `testAPI/ai-chat-openrouter-test.http`
- Test cơ bản với AI chat
- Test lịch sử chat
- Test error cases
- Test authentication

### Chạy Tests
```bash
# Compile project
mvn clean compile -DskipTests

# Run application  
mvn spring-boot:run

# Test với VS Code REST Client extension
# Mở file ai-chat-openrouter-test.http và click "Send Request"
```

## 🔧 Troubleshooting

### OpenRouter API Issues
1. **API Key không hợp lệ**:
   - Kiểm tra `openai.api.key` trong application.properties
   - Verify key tại OpenRouter dashboard

2. **Model không available**:
   - Đảm bảo model `meta-llama/llama-3.3-8b-instruct:free` được hỗ trợ
   - Kiểm tra quota limits

3. **Timeout errors**:
   - Tăng `openai.api.timeout` value
   - Kiểm tra network connectivity

### System Fallbacks
- Nếu OpenRouter API fail → Mock responses
- Nếu tin nhắn phức tạp → Transfer to human support
- Error logging và monitoring đầy đủ

## 🎯 Busify AI Assistant Features

### Hỗ Trợ Khách Hàng
- **Đặt vé xe**: Hướng dẫn quy trình từng bước
- **Giá vé**: Giải thích factors ảnh hưởng giá
- **Lịch trình**: Thông tin tuyến đường và thời gian
- **Thanh toán**: Liệt kê phương thức available
- **Hủy vé**: Chính sách hoàn tiền chi tiết
- **Khuyến mãi**: Thông tin promotion hiện tại

### Smart Context
- System prompt được tối ưu cho Busify domain
- Hiểu context về dịch vụ xe khách Việt Nam
- Phản hồi bằng tiếng Việt chuyên nghiệp
- Giới hạn 300 từ per response
- Luôn đề xuất liên hệ human support khi cần

## 🚀 Deployment Notes

### Production Checklist
- [ ] Update OpenRouter API key
- [ ] Configure proper logging levels
- [ ] Set up monitoring cho API calls  
- [ ] Test performance với concurrent users
- [ ] Backup và restore chat history
- [ ] Security audit cho AI responses

### Monitoring
- Track OpenRouter API usage và costs
- Monitor response times
- Log AI chat patterns
- Alert cho API failures

---

**Status**: ✅ **READY FOR TESTING**  
**Integration**: OpenRouter + Meta Llama 3.3  
**Last Updated**: 2025-09-12