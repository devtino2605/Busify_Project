package com.busify.project.chat.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotService {

    private final OpenAIService openAIService;

    /**
     * Xử lý tin nhắn từ người dùng và tạo phản hồi từ AI
     * 
     * @param userMessage Tin nhắn từ người dùng
     * @param userEmail Email của người dùng (để cá nhân hóa phản hồi)
     * @return Phản hồi từ AI
     */
    public String getBotReply(String userMessage, String userEmail) {
        try {
            log.info("Processing AI chat message from user: {}", userEmail);
            
            // Thử sử dụng ChatGPT trước
            String chatGPTReply = openAIService.getChatGPTResponse(userMessage, userEmail);
            
            if (chatGPTReply != null && !chatGPTReply.trim().isEmpty()) {
                log.info("Generated ChatGPT reply for user: {}", userEmail);
                return chatGPTReply;
            } else {
                log.warn("ChatGPT unavailable, using fallback mock reply for user: {}", userEmail);
                // Fallback về phản hồi mẫu nếu ChatGPT không khả dụng
                return generateMockReply(userMessage);
            }
            
        } catch (Exception e) {
            log.error("Error generating AI reply for user: {}", userEmail, e);
            return "Xin lỗi, tôi đang gặp sự cố kỹ thuật. Vui lòng thử lại sau hoặc liên hệ với nhân viên hỗ trợ.";
        }
    }

    /**
     * Xử lý tin nhắn với lịch sử hội thoại để có context tốt hơn
     */
    public String getBotReplyWithHistory(String userMessage, java.util.List<com.busify.project.chat.model.ChatMessage> chatHistory, String userEmail) {
        try {
            log.info("Processing AI chat message with history from user: {}", userEmail);
            
            // Thử sử dụng ChatGPT với history trước
            String chatGPTReply = openAIService.getChatGPTResponseWithHistory(userMessage, chatHistory, userEmail);
            
            if (chatGPTReply != null && !chatGPTReply.trim().isEmpty()) {
                log.info("Generated ChatGPT reply with history for user: {}", userEmail);
                return chatGPTReply;
            } else {
                log.warn("ChatGPT with history unavailable, falling back to simple reply for user: {}", userEmail);
                // Fallback về getBotReply thông thường
                return getBotReply(userMessage, userEmail);
            }
            
        } catch (Exception e) {
            log.error("Error generating AI reply with history for user: {}", userEmail, e);
            return getBotReply(userMessage, userEmail); // Fallback
        }
    }

    /**
     * Tạo phản hồi mẫu dựa trên tin nhắn của người dùng (fallback khi ChatGPT không khả dụng)
     */
    private String generateMockReply(String userMessage) {
        String lowerMessage = userMessage.toLowerCase();
        
        // Phản hồi dựa trên từ khóa
        if (lowerMessage.contains("hello") || lowerMessage.contains("xin chào") || lowerMessage.contains("chào")) {
            return "Xin chào! Tôi là trợ lý ảo của Busify. Tôi có thể giúp gì cho bạn hôm nay?\n\n" +
                   "(Lưu ý: Hiện tại tôi đang hoạt động ở chế độ fallback. Để trải nghiệm tốt nhất, vui lòng cấu hình OpenAI API key.)";
        }
        
        if (lowerMessage.contains("đặt vé") || lowerMessage.contains("booking") || lowerMessage.contains("book")) {
            return "Để đặt vé xe trên Busify, bạn có thể:\n" +
                   "1. Truy cập trang chủ Busify\n" +
                   "2. Chọn điểm đi và điểm đến\n" +
                   "3. Chọn ngày và giờ đi\n" +
                   "4. Chọn loại xe và ghế ngồi\n" +
                   "5. Nhập thông tin hành khách\n" +
                   "6. Thanh toán và nhận vé điện tử\n\n" +
                   "Bạn cần hỗ trợ thêm về quy trình đặt vé không?";
        }
        
        if (lowerMessage.contains("giá vé") || lowerMessage.contains("price") || lowerMessage.contains("cost")) {
            return "Giá vé xe trên Busify phụ thuộc vào:\n" +
                   "• Tuyến đường (khoảng cách)\n" +
                   "• Loại xe (thường, VIP, giường nằm)\n" +
                   "• Thời gian đi (giờ cao điểm, cuối tuần)\n" +
                   "• Nhà xe vận hành\n\n" +
                   "Để biết giá chính xác, bạn vui lòng chọn tuyến đường cụ thể trên trang chủ. " +
                   "Có tuyến đường nào bạn quan tâm không?";
        }
        
        if (lowerMessage.contains("hủy vé") || lowerMessage.contains("cancel") || lowerMessage.contains("refund")) {
            return "Chính sách hủy vé của Busify:\n" +
                   "🕐 Hủy trước 24h: Hoàn tiền 90%\n" +
                   "🕐 Hủy trước 12h: Hoàn tiền 70%\n" +
                   "🕐 Hủy trước 6h: Hoàn tiền 50%\n" +
                   "🕐 Hủy trong 6h: Không hoàn tiền\n\n" +
                   "Để hủy vé, bạn có thể:\n" +
                   "1. Đăng nhập tài khoản Busify\n" +
                   "2. Vào mục 'Lịch sử đặt vé'\n" +
                   "3. Chọn vé cần hủy\n" +
                   "4. Nhấn 'Hủy vé' và xác nhận\n\n" +
                   "Bạn cần hỗ trợ hủy vé cụ thể không?";
        }
        
        if (lowerMessage.contains("thanh toán") || lowerMessage.contains("payment") || lowerMessage.contains("pay")) {
            return "Busify hỗ trợ các phương thức thanh toán:\n" +
                   "💳 VNPay (ATM, Visa, Mastercard)\n" +
                   "🌐 PayPal (thẻ quốc tế)\n" +
                   "🏪 Chuyển khoản ngân hàng\n" +
                   "🎫 Thanh toán tại bến xe (một số tuyến)\n\n" +
                   "Tất cả giao dịch đều được mã hóa và bảo mật. " +
                   "Bạn sẽ nhận được email xác nhận và vé điện tử sau khi thanh toán thành công.\n\n" +
                   "Bạn gặp vấn đề gì với thanh toán không?";
        }
        
        if (lowerMessage.contains("lịch trình") || lowerMessage.contains("schedule") || lowerMessage.contains("thời gian")) {
            return "Để xem lịch trình xe:\n" +
                   "1. Truy cập trang chủ Busify\n" +
                   "2. Nhập điểm đi và điểm đến\n" +
                   "3. Chọn ngày đi\n" +
                   "4. Hệ thống sẽ hiển thị tất cả chuyến xe khả dụng với:\n" +
                   "   • Giờ khởi hành\n" +
                   "   • Thời gian hành trình\n" +
                   "   • Loại xe\n" +
                   "   • Giá vé\n" +
                   "   • Số ghế trống\n\n" +
                   "Bạn muốn tìm lịch trình cho tuyến nào?";
        }
        
        // Phản hồi mặc định
        return "Tôi hiểu bạn đang hỏi về: \"" + userMessage + "\"\n\n" +
               "Tôi có thể hỗ trợ bạn về các vấn đề sau:\n" +
               "🎫 Đặt vé xe khách\n" +
               "💰 Giá vé và khuyến mãi\n" +
               "🚌 Lịch trình và tuyến đường\n" +
               "❌ Hủy vé và hoàn tiền\n" +
               "💳 Thanh toán và vé điện tử\n" +
               "📍 Thông tin bến xe\n\n" +
               "Bạn có thể hỏi cụ thể hơn hoặc liên hệ nhân viên hỗ trợ nếu cần giúp đỡ thêm. " +
               "Tôi có thể giúp gì khác cho bạn không?";
    }

    /**
     * Kiểm tra xem tin nhắn có cần chuyển cho nhân viên không
     */
    public boolean shouldTransferToHuman(String userMessage) {
        String lowerMessage = userMessage.toLowerCase();
        
        // Các từ khóa cần chuyển cho nhân viên
        String[] transferKeywords = {
            "nhân viên", "staff", "support", "complaint", "khiếu nại", 
            "problem", "vấn đề", "bug", "lỗi", "emergency", "khẩn cấp"
        };
        
        for (String keyword : transferKeywords) {
            if (lowerMessage.contains(keyword)) {
                return true;
            }
        }
        
        return false;
    }
}