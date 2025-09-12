# Hướng dẫn tích hợp ChatGPT với Busify Chatbot

## 🚀 Tổng quan
Hệ thống chatbot Busify đã được tích hợp với OpenAI ChatGPT API. Khi có API key hợp lệ, chatbot sẽ sử dụng ChatGPT để phản hồi thông minh. Nếu không có API key hoặc gặp lỗi, hệ thống sẽ tự động fallback về phản hồi mẫu.

## 🔑 Cách lấy OpenAI API Key

### Bước 1: Đăng ký tài khoản OpenAI
1. Truy cập: https://platform.openai.com/
2. Đăng ký/đăng nhập tài khoản
3. Xác thực email và số điện thoại

### Bước 2: Tạo API Key
1. Vào mục **API Keys**: https://platform.openai.com/api-keys
2. Nhấn **"Create new secret key"**
3. Đặt tên cho key (ví dụ: "Busify-Chatbot")
4. Copy API key (dạng: `sk-...`)
5. **LƯU Ý**: Chỉ hiển thị 1 lần, hãy lưu lại ngay!

### Bước 3: Nạp tiền vào tài khoản
1. Vào **Billing**: https://platform.openai.com/account/billing
2. Thêm phương thức thanh toán
3. Nạp tiền (tối thiểu $5)

## ⚙️ Cấu hình trong dự án

### 1. Cập nhật application.properties
```properties
# OpenAI configuration - ĐÃ CẤU HÌNH
openai.api.key=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
openai.api.model=gpt-4o
openai.api.timeout=30
openai.api.max-tokens=500
openai.api.temperature=0.7
```

### 2. Giải thích các tham số
- **api.key**: API key từ OpenAI
- **api.model**: Model sử dụng (gpt-3.5-turbo, gpt-4, v.v.)
- **api.timeout**: Timeout cho API call (giây)
- **api.max-tokens**: Số token tối đa cho response
- **api.temperature**: Độ sáng tạo (0.0-1.0)

## 🏗️ Kiến trúc hệ thống

```
ChatAIController
       ↓
   ChatBotService ──→ OpenAIService ──→ ChatGPT API
       ↓                    ↓ (fallback)
   Mock Response         Mock Response
```

### Luồng hoạt động:
1. User gửi tin nhắn qua WebSocket/REST API
2. ChatBotService nhận tin nhắn
3. Gọi OpenAIService để lấy response từ ChatGPT
4. Nếu thành công: trả về response từ ChatGPT
5. Nếu thất bại: fallback về mock response
6. Lưu tin nhắn vào database và gửi về user

## 🧪 Testing

### 1. Test với API key hợp lệ
```bash
# Kiểm tra log
tail -f logs/application.log | grep "OpenAI"

# Gửi tin nhắn test
curl -X POST http://localhost:8080/api/ai-chat/send \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {your-jwt-token}" \
  -d '{"content": "Xin chào, tôi muốn đặt vé xe từ Hà Nội đi Hồ Chí Minh"}'
```

### 2. Test fallback (không có API key)
- Để `openai.api.key=YOUR_OPENAI_API_KEY_HERE`
- Khởi động ứng dụng
- Gửi tin nhắn → sẽ nhận được mock response

## 💰 Chi phí sử dụng

### GPT-3.5-turbo (khuyến nghị)
- Input: $0.0015 / 1K tokens
- Output: $0.002 / 1K tokens
- Trung bình 1 cuộc hội thoại: ~500 tokens = ~$0.0018

### GPT-4 (chất lượng cao hơn)
- Input: $0.03 / 1K tokens  
- Output: $0.06 / 1K tokens
- Trung bình 1 cuộc hội thoại: ~500 tokens = ~$0.045

### Ước tính cho Busify:
- 1000 tin nhắn/ngày với GPT-3.5: ~$1.8/ngày
- 1000 tin nhắn/ngày với GPT-4: ~$45/ngày

## 🔒 Bảo mật

### 1. Bảo vệ API Key
```properties
# KHÔNG commit API key vào Git
openai.api.key=${OPENAI_API_KEY:YOUR_OPENAI_API_KEY_HERE}
```

### 2. Environment Variables
```bash
# Production deployment
export OPENAI_API_KEY=sk-your-real-api-key
```

### 3. Rate Limiting
- OpenAI có rate limit mặc định
- Implement caching để giảm số lượng request
- Monitor usage qua OpenAI dashboard

## 🎯 Tối ưu hóa

### 1. Giảm chi phí
```properties
# Sử dụng model rẻ hơn
openai.api.model=gpt-3.5-turbo

# Giảm max tokens
openai.api.max-tokens=300

# Giảm temperature cho response ổn định
openai.api.temperature=0.3
```

### 2. Cải thiện chất lượng
- Customize system prompt trong `OpenAIService`
- Thêm context từ lịch sử chat
- Fine-tune model với dữ liệu Busify

### 3. Monitoring
```java
// Thêm metrics
@Timed("openai.api.calls")
@Counted("openai.api.success")
public String getChatGPTResponse(String userMessage, String userEmail) {
    // ... existing code
}
```

## 🚨 Troubleshooting

### Lỗi thường gặp:

1. **"Invalid API Key"**
   - Kiểm tra API key trong application.properties
   - Đảm bảo key chưa expire
   - Kiểm tra billing account

2. **"Rate limit exceeded"**
   - Chờ 1 phút rồi thử lại
   - Upgrade plan OpenAI
   - Implement retry mechanism

3. **"Model not found"**
   - Kiểm tra model name (gpt-3.5-turbo, gpt-4)
   - Đảm bảo account có quyền truy cập model

4. **Connection timeout**
   - Tăng timeout trong config
   - Kiểm tra network connection
   - Sử dụng fallback response

### Debug commands:
```bash
# Kiểm tra config
curl localhost:8080/actuator/configprops | grep openai

# Test API directly
curl https://api.openai.com/v1/models \
  -H "Authorization: Bearer sk-your-api-key"
```

## 📈 Monitoring & Analytics

### 1. Metrics cần theo dõi
- Số lượng request tới OpenAI API
- Response time
- Error rate
- Chi phí sử dụng
- User satisfaction

### 2. Logging
```java
log.info("OpenAI API call - User: {}, Tokens: {}, Cost: ${}", 
         userEmail, tokenCount, estimatedCost);
```

## 🔄 Nâng cấp tương lai

### 1. Context Memory
- Lưu lịch sử chat để có context tốt hơn
- Implement conversation memory

### 2. Custom Fine-tuning
- Train model với data Busify specific
- Cải thiện accuracy cho domain knowledge

### 3. Multiple Models
- Sử dụng different models cho different tasks
- Load balancing giữa các models

### 4. Caching
- Cache responses cho câu hỏi phổ biến
- Reduce API calls và chi phí

## 📞 Hỗ trợ

Nếu gặp vấn đề:
1. Check logs: `tail -f logs/application.log`
2. Verify config: `application.properties`
3. Test API key: OpenAI Playground
4. Contact team: [team@busify.com]

---

**Lưu ý**: Luôn test thoroughly trước khi deploy production và monitor chi phí sử dụng API thường xuyên.