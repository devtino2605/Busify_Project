# Hệ thống Chat AI - Busify

Hệ thống chat AI cho phép khách hàng trò chuyện với chatbot tự động, riêng biệt với chat giữa khách hàng và nhân viên.

## Cấu trúc

```
src/main/java/com/busify/project/chat/
├── controller/
│   ├── ChatController.java          # Chat giữa customer-admin
│   └── ChatAIController.java        # Chat giữa customer-AI (MỚI)
├── service/
│   ├── ChatService.java             # Service chung
│   ├── ChatAssignmentService.java   # Gán nhân viên
│   └── ChatBotService.java          # Xử lý logic AI (MỚI)
├── dto/
│   ├── ChatMessageDTO.java          # DTO chung
│   ├── AIChatRequestDTO.java        # Request cho AI (MỚI)
│   └── AIChatResponseDTO.java       # Response từ AI (MỚI)
├── model/
│   └── ChatMessage.java             # Entity lưu tin nhắn
└── repository/
    └── ChatMessageRepository.java   # Repository
```

## Cách hoạt động

### 1. Room ID cho chat AI
- Mỗi user có một room riêng để chat với AI: `"ai-{userEmail}"`
- Ví dụ: `"ai-user@example.com"`

### 2. WebSocket Endpoints

#### Gửi tin nhắn tới AI
```javascript
// Client gửi tin nhắn
stompClient.send("/app/chat.ai/" + userId, {}, JSON.stringify({
    content: "Xin chào",
    sender: userEmail,
    type: "CHAT"
}));
```

#### Nhận phản hồi từ AI
```javascript
// Client subscribe để nhận tin nhắn
stompClient.subscribe("/topic/ai/" + userId, function(message) {
    const chatMessage = JSON.parse(message.body);
    // Hiển thị tin nhắn AI
});
```

### 3. REST API Endpoints

#### POST `/api/ai-chat/send`
Gửi tin nhắn cho AI (alternative cho WebSocket)

**Request:**
```json
{
    "content": "Tôi muốn đặt vé xe"
}
```

**Response:**
```json
{
    "code": 200,
    "message": "Tin nhắn đã được gửi thành công",
    "result": {
        "id": 123,
        "content": "Để đặt vé xe, bạn có thể...",
        "sender": "AI Bot",
        "timestamp": "2025-09-12T10:00:00"
    }
}
```

#### GET `/api/ai-chat/history`
Lấy lịch sử chat với AI của user hiện tại

**Response:**
```json
{
    "code": 200,
    "message": "Lấy lịch sử chat AI thành công",
    "result": [
        {
            "id": 122,
            "content": "Xin chào",
            "sender": "user@example.com",
            "timestamp": "2025-09-12T09:59:00"
        },
        {
            "id": 123,
            "content": "Xin chào! Tôi là trợ lý ảo...",
            "sender": "AI Bot",
            "timestamp": "2025-09-12T10:00:00"
        }
    ]
}
```

#### POST `/api/ai-chat/start`
Khởi tạo cuộc trò chuyện với AI (gửi tin nhắn chào)

#### GET `/api/ai-chat/history/{userId}`
Lấy lịch sử chat AI theo userId (cho admin)

## Tính năng AI

### 1. Phản hồi tự động
ChatBotService hiện tại có các phản hồi cho:
- Chào hỏi
- Đặt vé
- Giá vé
- Hủy vé
- Lịch trình
- Thanh toán
- Tạm biệt

### 2. Chuyển cho nhân viên
AI tự động phát hiện khi nào cần chuyển cho nhân viên dựa trên từ khóa:
- "nhân viên", "staff", "support"
- "khiếu nại", "complaint"
- "vấn đề", "problem", "lỗi", "bug"
- "khẩn cấp", "emergency"

### 3. Lưu lịch sử
- Tất cả tin nhắn đều được lưu vào database
- Dùng chung bảng `chat_messages` với chat customer-admin
- Phân biệt bằng roomId và sender

## Tích hợp AI thực tế

Để tích hợp với AI service thực tế (OpenAI, Rasa, v.v.), sửa method `getBotReply()` trong `ChatBotService`:

```java
public String getBotReply(String userMessage, String userEmail) {
    try {
        // Thay thế phần này bằng call tới AI service thực tế
        // Ví dụ với OpenAI:
        // return openAiClient.getChatCompletion(userMessage);
        
        // Hoặc với service AI nội bộ:
        // return internalAiService.processMessage(userMessage, userEmail);
        
        return generateMockReply(userMessage);
    } catch (Exception e) {
        log.error("Error generating AI reply", e);
        return "Xin lỗi, tôi đang gặp sự cố...";
    }
}
```

## Frontend Integration

### HTML Example
```html
<!-- Chat AI Container -->
<div id="ai-chat-container">
    <div id="ai-chat-messages"></div>
    <input type="text" id="ai-message-input" placeholder="Nhập tin nhắn...">
    <button onclick="sendAIMessage()">Gửi</button>
</div>
```

### JavaScript Example
```javascript
let stompClient = null;
let currentUser = "user@example.com";

function connectToAIChat() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        // Subscribe to AI responses
        stompClient.subscribe('/topic/ai/' + currentUser, function(message) {
            const chatMessage = JSON.parse(message.body);
            displayAIMessage(chatMessage);
        });
    });
}

function sendAIMessage() {
    const messageInput = document.getElementById('ai-message-input');
    const content = messageInput.value.trim();
    
    if (content && stompClient) {
        const chatMessage = {
            content: content,
            sender: currentUser,
            type: 'CHAT'
        };
        
        stompClient.send("/app/chat.ai/" + currentUser, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
        
        // Display user message immediately
        displayUserMessage(chatMessage);
    }
}

function displayAIMessage(message) {
    const messagesDiv = document.getElementById('ai-chat-messages');
    messagesDiv.innerHTML += `
        <div class="ai-message">
            <strong>AI Bot:</strong> ${message.content}
            <small>${new Date(message.timestamp).toLocaleTimeString()}</small>
        </div>
    `;
    messagesDiv.scrollTop = messagesDiv.scrollHeight;
}
```

## Testing

### Test WebSocket
1. Kết nối WebSocket tới `/ws`
2. Subscribe `/topic/ai/{userEmail}`
3. Gửi message tới `/app/chat.ai/{userEmail}`
4. Kiểm tra phản hồi từ AI

### Test REST API
```bash
# Gửi tin nhắn
curl -X POST http://localhost:8080/api/ai-chat/send \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"content": "Xin chào"}'

# Lấy lịch sử
curl -X GET http://localhost:8080/api/ai-chat/history \
  -H "Authorization: Bearer {token}"
```