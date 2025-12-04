package com.busify.project.ai_chat.service;

import com.busify.project.ai_chat.config.GeminiConfig;
import com.busify.project.ai_chat.dto.AIResponseWrapper;
import com.busify.project.ai_chat.dto.AIResponseWrapper.AISource;
import com.busify.project.ai_chat.entity.AIChatMessage;
import com.busify.project.ai_chat.function.AIFunctionDefinitions;
import com.busify.project.ai_chat.function.AIFunctionExecutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service tích hợp với Google Gemini API để xử lý AI chat
 * Hỗ trợ Function Calling để truy vấn database thực
 */
@Service("aiChatAIService")
@RequiredArgsConstructor
@Slf4j
public class AIService {

    private final GeminiConfig geminiConfig;
    private final GeminiService geminiService;
    private final AIFunctionExecutor functionExecutor;

    // Số lần function call tối đa để tránh loop vô hạn
    private static final int MAX_FUNCTION_CALLS = 5;

    /**
     * System prompt cho Busify AI chatbot
     * Chi tiết về hệ thống đặt vé xe khách Busify
     * 
     * LƯU Ý: Prompt này được viết dựa trên các entity và logic thực tế trong
     * project:
     * - CargoBooking, CargoStatus, CargoType (cargo module)
     * - BookingServiceImpl - refund logic (hủy trong 24h = 100%, trước 24h
     * departure = 70%)
     * - PaymentMethod enum: VNPAY, MOMO, PAYPAL, BANK_TRANSFER, SIMULATION
     * - Bus entity với amenities (JSON), BusModel
     * - TripStatus: on_sell (đang mở bán)
     */
    private static final String BUSIFY_SYSTEM_PROMPT = """
            # BUSIFY AI ASSISTANT - HỆ THỐNG ĐẶT VÉ XE KHÁCH TRỰC TUYẾN

            ## 🎯 VAI TRÒ CỦA BẠN
            Bạn là "Busify Assistant" - trợ lý ảo thông minh của Busify, nền tảng đặt vé xe khách trực tuyến.
            Bạn thân thiện, chuyên nghiệp và luôn sẵn sàng hỗ trợ khách hàng 24/7.

            ## 🔧 KHẢ NĂNG CỦA BẠN - FUNCTION CALLING
            **BẠN CÓ KHẢ NĂNG TRUY VẤN DATABASE THỰC** thông qua các functions sau:

            ### Functions tra cứu thông tin:
            1. **search_trips**: Tìm kiếm chuyến xe ĐANG MỞ BÁN
               - Trả về: số ghế trống, giá vé, tiện ích xe, đánh giá
               - Khi khách hỏi "có chuyến nào đi X?", "còn chỗ không?", "có wifi không?" → GỌI FUNCTION NÀY

            2. **get_active_promotions**: Lấy danh sách khuyến mãi đang có
               - Khi khách hỏi về mã giảm giá, voucher, khuyến mãi → GỌI FUNCTION NÀY

            3. **get_booking_info**: Tra cứu thông tin đặt vé theo mã booking
               - Khi khách đưa mã vé và muốn tra cứu → GỌI FUNCTION NÀY

            4. **get_route_info**: Lấy thông tin các tuyến đường
               - Khi khách hỏi về tuyến đường có sẵn → GỌI FUNCTION NÀY

            ### Functions đặt vé (QUAN TRỌNG - BẠN CÓ THỂ ĐẶT VÉ CHO KHÁCH!):
            5. **get_available_seats**: Lấy danh sách ghế trống của chuyến xe
               - Cần tripId (từ search_trips)
               - Khi khách muốn chọn ghế → GỌI FUNCTION NÀY

            6. **create_booking**: Tạo đơn đặt vé mới
               - Cần: tripId, seatNumbers, guestFullName, guestPhone, guestEmail
               - QUAN TRỌNG: XÁC NHẬN với khách TRƯỚC khi gọi!
               - Sau khi có đủ thông tin → GỌI FUNCTION NÀY

            7. **initiate_payment**: Tạo link thanh toán
               - Cần: bookingId (từ create_booking), paymentMethod (VNPAY/MOMO/PAYPAL/SIMULATION)
               - SIMULATION dùng để test, tự động hoàn tất thanh toán
               - Sau khi booking thành công → HỎI khách phương thức và GỌI FUNCTION NÀY

            ### Function hủy vé:
            8. **cancel_booking**: Hủy vé và hoàn tiền
               - Cần: bookingCode (mã vé), reason (lý do hủy)
               - QUAN TRỌNG: PHẢI thông báo chính sách hoàn tiền TRƯỚC khi hủy:
                 • Hủy trong 24h sau đặt: hoàn 100%
                 • Hủy trước 24h khởi hành: hoàn 70%
                 • Hủy sát giờ (<24h trước khởi hành): KHÔNG hoàn tiền
               - Khi khách muốn hủy vé, hủy đơn → XÁC NHẬN rồi GỌI FUNCTION NÀY

            ### Functions tiện ích mới:
            9. **change_seat**: Đổi ghế cho vé đã đặt
               - Cần: bookingCode, newSeatNumber (ghế mới)
               - Khi khách muốn đổi ghế, đổi chỗ → GỌI FUNCTION NÀY

            10. **get_booking_history**: Xem lịch sử đặt vé
                - Khi khách hỏi "tôi đã đặt vé nào", "lịch sử vé của tôi" → GỌI FUNCTION NÀY

            11. **search_best_trips**: Tìm chuyến xe tốt nhất
                - Cần: sortBy (CHEAPEST/BEST_RATED/MOST_AVAILABLE)
                - Khi khách hỏi "chuyến nào rẻ nhất", "xe nào đánh giá cao", "chuyến nào còn nhiều chỗ" → GỌI FUNCTION NÀY

            12. **search_round_trip**: Tìm chuyến khứ hồi
                - Cần: startCity, endCity, departureDate, returnDate
                - Khi khách muốn đặt vé 2 chiều, khứ hồi → GỌI FUNCTION NÀY

            ### Functions hỗ trợ gửi hàng (CARGO):
            13. **calculate_cargo_fee**: Tính phí gửi hàng
                - Cần: tripId, cargoType (DOCUMENT/PACKAGE/FRAGILE/ELECTRONICS/OTHER), weight
                - Khi khách hỏi "gửi hàng bao nhiêu tiền?", "phí ship?" → GỌI FUNCTION NÀY
                - Sau khi tính phí, HƯỚNG DẪN khách tạo đơn qua website/hotline

            14. **get_cargo_info**: Tra cứu đơn gửi hàng
                - Cần: cargoCode (mã vận đơn)
                - Khi khách hỏi "hàng của tôi đến đâu?", "tra cứu vận đơn" → GỌI FUNCTION NÀY

            15. **cancel_cargo**: Hủy đơn gửi hàng
                - Cần: cargoCode, reason
                - Chỉ hủy được khi đơn ở trạng thái PENDING hoặc CONFIRMED
                - Khi khách muốn hủy đơn gửi hàng → GỌI FUNCTION NÀY

            ⚠️ **QUAN TRỌNG**: Khi khách hỏi về chuyến xe, khuyến mãi, vé, tuyến đường - HÃY GỌI FUNCTION!

            ## 📋 THÔNG TIN HỆ THỐNG BUSIFY

            ### 1. QUY TRÌNH ĐẶT VÉ (BẠN CÓ THỂ HỖ TRỢ TOÀN BỘ!)
            **FLOW ĐẶT VÉ QUA CHAT:**
            ```
            Bước 1: Khách nói điểm đi/đến + ngày → Gọi search_trips
            Bước 2: Khách chọn chuyến → Gọi get_available_seats
            Bước 3: Khách chọn ghế + cung cấp thông tin → XÁC NHẬN rồi gọi create_booking
            Bước 4: Khách chọn phương thức thanh toán → Gọi initiate_payment
            Bước 5: Gửi link thanh toán cho khách
            ```

            **VÍ DỤ HỘI THOẠI:**
            Khách: "Đặt vé đi Đà Lạt ngày mai đi"
            → Gọi search_trips(endCity="Đà Lạt", departureDate="2024-12-05")
            → Hiển thị danh sách chuyến

            Khách: "Chọn chuyến 2"
            → Gọi get_available_seats(tripId=123)
            → Hiển thị ghế trống

            Khách: "Lấy ghế A1 A2, tên Nguyễn Văn A, 0901234567, a@gmail.com"
            → XÁC NHẬN: "Bạn muốn đặt 2 ghế A1, A2 chuyến 9:00 đi Đà Lạt, tổng 600k. Đúng chưa?"

            Khách: "Đúng rồi"
            → Gọi create_booking(tripId=123, seatNumbers="A1,A2", guestFullName="Nguyễn Văn A", ...)
            → Hiển thị thông tin booking + hỏi thanh toán

            Khách: "Thanh toán VNPay"
            → Gọi initiate_payment(bookingId=456, paymentMethod="VNPAY")
            → Gửi link thanh toán

            ### 2. TIỆN ÍCH XE (tùy từng xe, gọi search_trips để xem cụ thể)
            Các tiện ích có thể có: WiFi, Điều hòa, Ổ cắm sạc USB, Chăn, Gối, Nước uống, TV, Toilet
            → Khi khách hỏi "có wifi không?", "có ổ cắm không?" → Gọi search_trips để xem tiện ích cụ thể

            ### 3. CHÍNH SÁCH HỦY VÉ VÀ HOÀN TIỀN (theo logic BookingServiceImpl)
            | Điều kiện | Tỷ lệ hoàn tiền |
            |-----------|-----------------|
            | Hủy trong vòng 24 giờ SAU KHI ĐẶT | 100% |
            | Hủy trước chuyến đi >= 24 giờ | 70% |
            | Hủy sát giờ khởi hành (< 24h) | 0% |
            - Hoàn tiền qua phương thức thanh toán ban đầu

            ### 4. PHƯƠNG THỨC THANH TOÁN (theo PaymentMethod enum)
            - VNPay (ATM nội địa, Visa, Mastercard)
            - Ví MoMo
            - PayPal
            - Chuyển khoản ngân hàng
            - **KHÔNG có phụ phí ẩn** - Giá hiển thị là giá cuối cùng

            ### 5. DỊCH VỤ GỬI HÀNG - CARGO (theo CargoBooking entity)

            **Loại hàng hóa** (CargoType enum):
            - DOCUMENT (Tài liệu): hệ số phí x0.5
            - PACKAGE (Hàng hóa thường): hệ số phí x1.0
            - FRAGILE (Hàng dễ vỡ): hệ số phí x1.5
            - ELECTRONICS (Thiết bị điện tử): hệ số phí x1.3
            - OTHER (Khác): hệ số phí x1.0

            **Trạng thái đơn hàng** (CargoStatus enum):
            - PENDING → CONFIRMED → PICKED_UP → IN_TRANSIT → ARRIVED → DELIVERED
            - Có thể bị: CANCELLED, REJECTED, RETURNED

            **Giao nhận:**
            - Nhận tại bến xe/điểm trả hàng (dropoff_location)
            - Khách đến nhận cần mang CMND/CCCD và mã vận đơn (cargo_code)
            - **CHƯA hỗ trợ giao tận nhà**

            **Phí và bảo hiểm:**
            - Phí gửi = cargo_fee (tính theo loại hàng, cân nặng)
            - Bảo hiểm = insurance_fee (tùy chọn, theo giá trị khai báo declared_value)
            - Tổng = total_amount

            **Người nhận không lấy hàng:**
            - Trạng thái chuyển thành RETURNED (hoàn trả về người gửi)

            **Hàng hóa KHÔNG nhận gửi:**
            - Chất cháy nổ, chất độc hại
            - Động vật sống
            - Hàng cấm theo quy định pháp luật
            - Tiền mặt, vàng bạc, đá quý

            ### 6. CÁC CÂU HỎI THƯỜNG GẶP

            **Về đặt vé:**
            - "Chuyến này còn chỗ không?" → Gọi search_trips xem available_seats
            - "Có wifi/ổ cắm không?" → Gọi search_trips xem amenities
            - "Xe có sạch không?" → Xe được vệ sinh trước mỗi chuyến, xem đánh giá (average_rating)
            - "Xe chạy đúng giờ không?" → Nhà xe cam kết đúng giờ, nếu trễ do lỗi nhà xe được hỗ trợ
            - "Đổi chỗ/đổi ghế được không?" → CÓ! Gọi change_seat với mã vé để đổi ghế
            - "Kiểm tra lại vé" → Gọi get_booking_info với mã vé vừa thao tác

            **Về gửi hàng:**
            - "Gửi gấp trong ngày được không?" → Tùy thuộc vào chuyến xe, xem thời gian khởi hành
            - "Hàng hư hỏng ai chịu?" → Nếu mua bảo hiểm: bồi thường theo declared_value
            - "Có nhận COD không?" → Hiện tại hệ thống CHƯA hỗ trợ COD
            - "Giao tận nhà không?" → Hiện tại CHƯA hỗ trợ, cần ra bến xe nhận

            **Về thanh toán:**
            - "Thanh toán online được hoàn tiền không?" → Có, theo chính sách hủy vé
            - "Nhà xe hủy thì sao?" → Trạng thái booking chuyển canceled_by_operator, hoàn tiền 100%
            - "Có phụ phí không?" → KHÔNG có phụ phí ẩn

            ## 📞 LIÊN HỆ HỖ TRỢ
            - **Hotline**: 1900 6067 (7:00 - 22:00 hàng ngày)
            - **Email**: support@busify.vn
            - **Khuyến mãi**: Gọi function get_active_promotions để xem

            ## 📝 QUY TẮC TRẢ LỜI

            ### PHẢI làm:
            ✅ GỌI FUNCTION khi khách hỏi về chuyến xe, khuyến mãi, vé, tuyến đường
            ✅ Trả lời bằng tiếng Việt, thân thiện
            ✅ Dựa vào DỮ LIỆU THỰC từ function để trả lời
            ✅ Nếu không chắc chắn về tính năng → Nói "hiện tại hệ thống chưa hỗ trợ" hoặc "vui lòng liên hệ hotline"
            ✅ Khi khách nói "kiểm tra lại", "xác nhận lại" → Gọi get_booking_info với mã vé đã đề cập trước đó
            ✅ NHỚ mã vé/booking code từ context hội thoại để sử dụng cho các thao tác tiếp theo

            ### KHÔNG được làm:
            ❌ Bịa đặt tính năng mà hệ thống không có
            ❌ Nói "không thể truy cập database" - BẠN CÓ THỂ thông qua functions!
            ❌ Hứa hẹn những gì hệ thống không hỗ trợ (VD: giao tận nhà, COD...)

            ### KHI KHÔNG CHẮC CHẮN:
            Nếu khách hỏi tính năng mà bạn không chắc hệ thống có → Trả lời: "Để chắc chắn nhất, bạn vui lòng liên hệ hotline 1900 6067 để được tư vấn chi tiết nhé!"
            """;

    /**
     * Lấy phản hồi từ AI cho tin nhắn đơn lẻ (trả về wrapper với source)
     * Hỗ trợ Function Calling để truy vấn dữ liệu thực từ database
     */
    public AIResponseWrapper getAIResponseWithSource(String userMessage, String userEmail) {
        try {
            log.info("Processing AI chat message from user: {}", userEmail);

            if (!geminiConfig.isConfigured()) {
                log.warn("Gemini API key not configured, using fallback response");
                return AIResponseWrapper.builder()
                        .message(generateFallbackResponse(userMessage))
                        .source(AISource.FALLBACK)
                        .build();
            }

            // Sử dụng Function Calling để AI có thể query database
            return processWithFunctionCalling(userMessage, null);

        } catch (Exception e) {
            log.error("Error getting AI response for user: {}", userEmail, e);
            return AIResponseWrapper.builder()
                    .message(
                            "Xin lỗi, tôi đang gặp sự cố kỹ thuật. Vui lòng thử lại sau hoặc liên hệ với nhân viên hỗ trợ.")
                    .source(AISource.ERROR)
                    .build();
        }
    }

    /**
     * Lấy phản hồi từ AI với lịch sử hội thoại (trả về wrapper với source)
     * Hỗ trợ Function Calling để truy vấn dữ liệu thực từ database
     */
    public AIResponseWrapper getAIResponseWithHistoryAndSource(String userMessage, List<AIChatMessage> chatHistory,
            String userEmail) {
        try {
            log.info("Processing AI chat message with history from user: {}", userEmail);

            if (!geminiConfig.isConfigured()) {
                log.warn("Gemini API key not configured, using fallback response");
                return AIResponseWrapper.builder()
                        .message(generateFallbackResponse(userMessage))
                        .source(AISource.FALLBACK)
                        .build();
            }

            // Convert chat history to Gemini format
            List<GeminiService.Content> geminiHistory = new ArrayList<>();
            int historyLimit = Math.min(chatHistory.size(), 10);
            for (int i = chatHistory.size() - historyLimit; i < chatHistory.size(); i++) {
                AIChatMessage msg = chatHistory.get(i);
                String role = msg.getRole() == AIChatMessage.MessageRole.ASSISTANT ? "model" : "user";
                geminiHistory.add(new GeminiService.Content(role, msg.getContent()));
            }

            // Sử dụng Function Calling với history
            return processWithFunctionCalling(userMessage, geminiHistory);

        } catch (Exception e) {
            log.error("Error getting AI response with history for user: {}", userEmail, e);
            return getAIResponseWithSource(userMessage, userEmail); // Fallback to simple
        }
    }

    /**
     * Xử lý tin nhắn với Function Calling
     * AI có thể gọi functions để query database và trả về dữ liệu thực
     */
    private AIResponseWrapper processWithFunctionCalling(String userMessage, List<GeminiService.Content> history) {
        try {
            // Lấy định nghĩa các functions
            List<GeminiService.FunctionDeclaration> functions = AIFunctionDefinitions.getAllFunctions();

            // Gọi Gemini với functions
            GeminiService.GeminiResponse response;
            if (history != null && !history.isEmpty()) {
                response = geminiService.getChatCompletionWithFunctions(userMessage, history, BUSIFY_SYSTEM_PROMPT,
                        functions);
            } else {
                response = geminiService.getChatCompletionWithFunctions(userMessage, null, BUSIFY_SYSTEM_PROMPT,
                        functions);
            }

            // Xử lý function calls (có thể có nhiều lần gọi liên tiếp)
            int functionCallCount = 0;
            List<GeminiService.Content> conversationHistory = history != null ? new ArrayList<>(history)
                    : new ArrayList<>();

            // Thêm user message vào history
            conversationHistory.add(new GeminiService.Content("user", userMessage));

            while (geminiService.hasFunctionCall(response) && functionCallCount < MAX_FUNCTION_CALLS) {
                functionCallCount++;

                // Lấy thông tin function call
                GeminiService.FunctionCallPart functionCall = geminiService.getFunctionCall(response);
                String functionName = functionCall.getName();
                Map<String, Object> args = functionCall.getArgs();

                log.info("AI requesting function call: {} with args: {}", functionName, args);

                // Thực thi function và lấy kết quả từ database
                String functionResult = functionExecutor.executeFunction(functionName, args);
                log.info("Function {} returned: {}", functionName,
                        functionResult.length() > 200 ? functionResult.substring(0, 200) + "..." : functionResult);

                // Thêm model response (function call) vào history
                if (response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                    GeminiService.Content modelContent = response.getCandidates().get(0).getContent();
                    if (modelContent != null) {
                        conversationHistory.add(modelContent);
                    }
                }

                // Thêm function response vào history - wrap result in a Map for proper JSON
                // format
                Map<String, Object> functionResultMap = new HashMap<>();
                functionResultMap.put("result", functionResult);
                conversationHistory.add(GeminiService.Content.ofFunctionResponse(functionName, functionResultMap));

                // Tiếp tục hội thoại với kết quả function
                response = geminiService.continueWithFunctionResponse(
                        conversationHistory,
                        BUSIFY_SYSTEM_PROMPT,
                        functions);
            }

            // Lấy text response cuối cùng
            String textResponse = geminiService.getTextResponse(response);

            if (textResponse != null && !textResponse.trim().isEmpty()) {
                log.info("AI response generated successfully (function calls: {})", functionCallCount);
                return AIResponseWrapper.builder()
                        .message(textResponse)
                        .source(AISource.GEMINI_AI)
                        .build();
            } else {
                log.warn("No text response from AI, using fallback");
                return AIResponseWrapper.builder()
                        .message(generateFallbackResponse(userMessage))
                        .source(AISource.FALLBACK)
                        .build();
            }

        } catch (Exception e) {
            log.error("Error in function calling process", e);
            // Fallback về gọi AI đơn giản không có function
            try {
                String response = geminiService.getChatCompletion(userMessage, BUSIFY_SYSTEM_PROMPT);
                if (response != null && !response.trim().isEmpty()) {
                    return AIResponseWrapper.builder()
                            .message(response)
                            .source(AISource.GEMINI_AI)
                            .build();
                }
            } catch (Exception ex) {
                log.error("Fallback AI call also failed", ex);
            }

            return AIResponseWrapper.builder()
                    .message(generateFallbackResponse(userMessage))
                    .source(AISource.FALLBACK)
                    .build();
        }
    }

    /**
     * Lấy phản hồi từ AI cho tin nhắn đơn lẻ (backward compatible - deprecated)
     * 
     * @deprecated Sử dụng getAIResponseWithSource() để có thêm thông tin nguồn gốc
     */
    @Deprecated
    public String getAIResponse(String userMessage, String userEmail) {
        return getAIResponseWithSource(userMessage, userEmail).getMessage();
    }

    /**
     * Lấy phản hồi từ AI với lịch sử hội thoại (backward compatible - deprecated)
     * 
     * @deprecated Sử dụng getAIResponseWithHistoryAndSource() để có thêm thông tin
     *             nguồn gốc
     */
    @Deprecated
    public String getAIResponseWithHistory(String userMessage, List<AIChatMessage> chatHistory, String userEmail) {
        return getAIResponseWithHistoryAndSource(userMessage, chatHistory, userEmail).getMessage();
    }

    /**
     * Kiểm tra xem AI service có sẵn sàng không
     */
    public boolean isAvailable() {
        return geminiConfig.isConfigured();
    }

    /**
     * Phản hồi fallback ngắn gọn khi AI không khả dụng
     * Chỉ sử dụng khi Gemini API gặp lỗi
     */
    private String generateFallbackResponse(String userMessage) {
        return "Xin lỗi, tôi tạm thời không thể xử lý yêu cầu của bạn. " +
                "Vui lòng thử lại sau hoặc liên hệ hotline hỗ trợ: 1900-xxxx";
    }

    /**
     * Kiểm tra xem tin nhắn có cần chuyển cho nhân viên không
     */
    public boolean shouldTransferToHuman(String userMessage) {
        String lowerMessage = userMessage.toLowerCase();

        String[] transferKeywords = {
                "nhân viên", "staff", "support", "complaint", "khiếu nại",
                "problem", "vấn đề", "bug", "lỗi", "emergency", "khẩn cấp",
                "manager", "quản lý", "giám đốc", "tức giận", "angry"
        };

        for (String keyword : transferKeywords) {
            if (lowerMessage.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
}
