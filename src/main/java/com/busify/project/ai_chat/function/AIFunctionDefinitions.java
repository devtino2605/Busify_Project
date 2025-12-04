package com.busify.project.ai_chat.function;

import com.busify.project.ai_chat.service.GeminiService.FunctionDeclaration;
import com.busify.project.ai_chat.service.GeminiService.Schema;

import java.util.List;
import java.util.Map;

/**
 * Định nghĩa các Function mà AI có thể gọi
 * Sử dụng các class từ GeminiService để đảm bảo tương thích với Gemini API
 */
public class AIFunctionDefinitions {

        /**
         * Danh sách tất cả functions mà AI có thể sử dụng
         */
        public static List<FunctionDeclaration> getAllFunctions() {
                return List.of(
                                getSearchTripsFunction(),
                                getActivePromotionsFunction(),
                                getBookingInfoFunction(),
                                getRouteInfoFunction(),
                                // Booking Flow Functions
                                getAvailableSeatsFunction(),
                                createBookingFunction(),
                                initiatePaymentFunction(),
                                // Cancel Booking Function
                                cancelBookingFunction(),
                                // New Features
                                changeSeatFunction(),
                                getBookingHistoryFunction(),
                                searchBestTripsFunction(),
                                searchRoundTripFunction(),
                                // Cargo Functions (chỉ hỗ trợ tính phí, tra cứu, hủy)
                                calculateCargoFeeFunction(),
                                getCargoInfoFunction(),
                                cancelCargoFunction());
        }

        /**
         * Function: Tìm kiếm chuyến xe ĐANG MỞ BÁN
         */
        public static FunctionDeclaration getSearchTripsFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("search_trips");
                fd.setDescription(
                                "Tìm kiếm các chuyến xe khách ĐANG MỞ BÁN (chỉ những chuyến còn có thể đặt vé). Sử dụng khi người dùng muốn tìm chuyến xe, hỏi về lịch trình, hoặc muốn biết có chuyến nào đi từ A đến B. Không trả về các chuyến đã khởi hành, đã đến, bị hủy hay chưa mở bán.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "startCity",
                                createSchema("string",
                                                "Thành phố/tỉnh điểm đi (VD: Hà Nội, TP.HCM, Đà Nẵng). Để trống nếu không chỉ định."),
                                "endCity",
                                createSchema("string", "Thành phố/tỉnh điểm đến. Để trống nếu không chỉ định."),
                                "departureDate",
                                createSchema("string",
                                                "Ngày khởi hành (format: yyyy-MM-dd). Để trống để lấy tất cả chuyến sắp tới."),
                                "limit", createSchema("integer", "Số lượng kết quả tối đa trả về (mặc định: 5)")));
                params.setRequired(List.of());
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Lấy danh sách khuyến mãi đang hoạt động
         */
        public static FunctionDeclaration getActivePromotionsFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("get_active_promotions");
                fd.setDescription(
                                "Lấy danh sách các chương trình khuyến mãi, mã giảm giá đang có hiệu lực. Sử dụng khi người dùng hỏi về khuyến mãi, voucher, mã giảm giá.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "limit", createSchema("integer", "Số lượng kết quả tối đa (mặc định: 5)")));
                params.setRequired(List.of());
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Tra cứu thông tin đặt vé
         */
        public static FunctionDeclaration getBookingInfoFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("get_booking_info");
                fd.setDescription(
                                "Tra cứu thông tin đặt vé theo mã booking. Sử dụng khi người dùng muốn kiểm tra vé, tra cứu đơn hàng.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "bookingCode", createSchema("string", "Mã đặt vé (booking code)")));
                params.setRequired(List.of("bookingCode"));
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Lấy thông tin tuyến đường
         */
        public static FunctionDeclaration getRouteInfoFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("get_route_info");
                fd.setDescription(
                                "Lấy thông tin các tuyến đường phổ biến. Sử dụng khi người dùng hỏi về tuyến đường, điểm đi/đến có sẵn.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "limit", createSchema("integer", "Số lượng tuyến đường trả về (mặc định: 10)")));
                params.setRequired(List.of());
                fd.setParameters(params);

                return fd;
        }

        /**
         * Helper method tạo Schema
         */
        private static Schema createSchema(String type, String description) {
                Schema schema = new Schema();
                schema.setType(type);
                schema.setDescription(description);
                return schema;
        }

        // ======================= BOOKING FLOW FUNCTIONS =======================

        /**
         * Function: Lấy danh sách ghế trống của một chuyến xe
         */
        public static FunctionDeclaration getAvailableSeatsFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("get_available_seats");
                fd.setDescription(
                                "Lấy danh sách ghế trống (available) của một chuyến xe cụ thể. " +
                                                "Sử dụng khi khách muốn xem sơ đồ ghế, danh sách ghế, chọn ghế để đặt vé, hoặc hỏi 'còn ghế nào', 'danh sách ghế'. "
                                                +
                                                "Cần có tripId từ kết quả search_trips hoặc từ context trước đó.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "tripId", createSchema("integer",
                                                "ID của chuyến xe (bắt buộc, lấy từ kết quả search_trips hoặc context)")));
                params.setRequired(List.of("tripId"));
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Tạo đơn đặt vé mới
         */
        public static FunctionDeclaration createBookingFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("create_booking");
                fd.setDescription(
                                "Tạo đơn đặt vé xe khách mới. Sử dụng sau khi khách đã chọn chuyến xe, ghế ngồi và cung cấp thông tin cá nhân. "
                                                +
                                                "QUAN TRỌNG: Cần xác nhận lại với khách trước khi gọi function này.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "tripId", createSchema("integer", "ID chuyến xe (bắt buộc)"),
                                "seatNumbers",
                                createSchema("string",
                                                "Số ghế muốn đặt, nhiều ghế cách nhau bởi dấu phẩy (VD: A1,A2). Bắt buộc"),
                                "guestFullName", createSchema("string", "Họ tên hành khách (bắt buộc)"),
                                "guestPhone", createSchema("string", "Số điện thoại (bắt buộc)"),
                                "guestEmail", createSchema("string", "Email nhận vé (bắt buộc)"),
                                "discountCode", createSchema("string", "Mã giảm giá (không bắt buộc)")));
                params.setRequired(List.of("tripId", "seatNumbers", "guestFullName", "guestPhone", "guestEmail"));
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Khởi tạo thanh toán cho booking
         */
        public static FunctionDeclaration initiatePaymentFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("initiate_payment");
                fd.setDescription(
                                "Tạo link thanh toán cho đơn đặt vé. Sử dụng sau khi đã tạo booking thành công. " +
                                                "Trả về URL để khách click vào thanh toán qua VNPay, MoMo, PayPal hoặc Simulation (test).");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "bookingId", createSchema("integer", "ID booking vừa tạo (bắt buộc)"),
                                "paymentMethod", createSchema("string",
                                                "Phương thức thanh toán: VNPAY, MOMO, PAYPAL, SIMULATION (mặc định: VNPAY). Dùng SIMULATION để test.")));
                params.setRequired(List.of("bookingId"));
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Hủy vé và yêu cầu hoàn tiền
         */
        public static FunctionDeclaration cancelBookingFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("cancel_booking");
                fd.setDescription(
                                "Hủy vé xe đã đặt và yêu cầu hoàn tiền. Sử dụng khi khách muốn hủy vé, hủy đơn, yêu cầu hoàn tiền. "
                                                +
                                                "QUAN TRỌNG: Cần xác nhận với khách về chính sách hoàn tiền trước khi hủy: "
                                                +
                                                "- Hủy trong 24h sau đặt: hoàn 100% " +
                                                "- Hủy trước 24h khởi hành: hoàn 70% " +
                                                "- Hủy sát giờ (<24h): không hoàn tiền");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "bookingCode", createSchema("string", "Mã đặt vé cần hủy (bắt buộc)"),
                                "reason", createSchema("string", "Lý do hủy vé (bắt buộc)")));
                params.setRequired(List.of("bookingCode", "reason"));
                fd.setParameters(params);

                return fd;
        }

        // ======================= NEW FEATURES =======================

        /**
         * Function: Đổi ghế cho booking đã đặt
         */
        public static FunctionDeclaration changeSeatFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("change_seat");
                fd.setDescription(
                                "Đổi ghế cho vé đã đặt. Sử dụng khi khách muốn đổi ghế, đổi chỗ ngồi. " +
                                                "Nếu chỉ có bookingCode mà không có newSeatNumber, sẽ hiển thị danh sách ghế trống để khách chọn. "
                                                +
                                                "Chỉ có thể đổi ghế khi booking ở trạng thái PENDING hoặc CONFIRMED và chuyến chưa khởi hành.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "bookingCode", createSchema("string", "Mã đặt vé cần đổi ghế (bắt buộc)"),
                                "newSeatNumber", createSchema("string",
                                                "Số ghế mới muốn đổi sang (không bắt buộc, nếu không có sẽ hiển thị ghế trống. VD: A.1.1)")));
                params.setRequired(List.of("bookingCode"));
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Xem lịch sử đặt vé của user
         */
        public static FunctionDeclaration getBookingHistoryFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("get_booking_history");
                fd.setDescription(
                                "Lấy lịch sử đặt vé của người dùng hiện tại. Sử dụng khi khách hỏi 'tôi đã đặt vé nào', "
                                                +
                                                "'lịch sử đặt vé của tôi', 'xem các vé đã đặt'. Yêu cầu user đã đăng nhập.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "status",
                                createSchema("string",
                                                "Lọc theo trạng thái: PENDING, CONFIRMED, COMPLETED, CANCELED (để trống = tất cả)"),
                                "limit", createSchema("integer", "Số lượng kết quả tối đa (mặc định: 10)")));
                params.setRequired(List.of());
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Tìm chuyến xe tốt nhất (rẻ nhất, rating cao nhất, ít người nhất)
         */
        public static FunctionDeclaration searchBestTripsFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("search_best_trips");
                fd.setDescription(
                                "Tìm chuyến xe tốt nhất theo tiêu chí: rẻ nhất, rating cao nhất, hoặc còn nhiều ghế trống nhất. "
                                                +
                                                "Sử dụng khi khách hỏi 'chuyến nào rẻ nhất', 'xe nào tốt nhất', 'chuyến nào còn nhiều chỗ'.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "startCity", createSchema("string", "Thành phố điểm đi"),
                                "endCity", createSchema("string", "Thành phố điểm đến"),
                                "departureDate", createSchema("string", "Ngày khởi hành (yyyy-MM-dd)"),
                                "sortBy",
                                createSchema("string",
                                                "Tiêu chí sắp xếp: CHEAPEST (rẻ nhất), BEST_RATED (đánh giá cao), MOST_AVAILABLE (nhiều ghế trống)"),
                                "limit", createSchema("integer", "Số lượng kết quả (mặc định: 5)")));
                params.setRequired(List.of("sortBy"));
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Tìm chuyến khứ hồi
         */
        public static FunctionDeclaration searchRoundTripFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("search_round_trip");
                fd.setDescription(
                                "Tìm chuyến xe khứ hồi (đi và về). Sử dụng khi khách muốn đặt vé 2 chiều, " +
                                                "hỏi 'đặt vé khứ hồi', 'tìm chuyến đi và về', 'đặt cả vé về luôn'.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "startCity", createSchema("string", "Thành phố điểm đi (bắt buộc)"),
                                "endCity", createSchema("string", "Thành phố điểm đến (bắt buộc)"),
                                "departureDate", createSchema("string", "Ngày đi (yyyy-MM-dd, bắt buộc)"),
                                "returnDate", createSchema("string", "Ngày về (yyyy-MM-dd, bắt buộc)"),
                                "limit", createSchema("integer", "Số lượng kết quả mỗi chiều (mặc định: 3)")));
                params.setRequired(List.of("startCity", "endCity", "departureDate", "returnDate"));
                fd.setParameters(params);

                return fd;
        }

        // ======================= CARGO BOOKING FUNCTIONS =======================

        /**
         * Function: Tính phí gửi hàng
         */
        public static FunctionDeclaration calculateCargoFeeFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("calculate_cargo_fee");
                fd.setDescription(
                                "Tính phí gửi hàng dựa trên loại hàng, cân nặng và chuyến xe. " +
                                                "Sử dụng khi khách hỏi 'gửi hàng bao nhiêu tiền?', 'phí ship bao nhiêu?', "
                                                +
                                                "'gửi đồ đi Đà Lạt giá bao nhiêu?'");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "tripId", createSchema("integer", "ID chuyến xe để gửi hàng (lấy từ search_trips)"),
                                "cargoType", createSchema("string",
                                                "Loại hàng: DOCUMENT (tài liệu), PACKAGE (hàng thường), FRAGILE (dễ vỡ), ELECTRONICS (điện tử), OTHER (khác)"),
                                "weight", createSchema("number", "Cân nặng hàng hóa (kg), tối đa 50kg"),
                                "needsInsurance", createSchema("boolean", "Có cần bảo hiểm không? (true/false)")));
                params.setRequired(List.of("tripId", "cargoType", "weight"));
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Tra cứu đơn gửi hàng
         */
        public static FunctionDeclaration getCargoInfoFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("get_cargo_info");
                fd.setDescription(
                                "Tra cứu thông tin đơn gửi hàng theo mã vận đơn (cargo code). " +
                                                "Sử dụng khi khách hỏi 'kiểm tra đơn hàng', 'hàng của tôi đến đâu rồi?', "
                                                +
                                                "'tra cứu mã vận đơn ABC123'.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "cargoCode", createSchema("string", "Mã vận đơn (cargo code) - bắt buộc")));
                params.setRequired(List.of("cargoCode"));
                fd.setParameters(params);

                return fd;
        }

        /**
         * Function: Hủy đơn gửi hàng
         */
        public static FunctionDeclaration cancelCargoFunction() {
                FunctionDeclaration fd = new FunctionDeclaration();
                fd.setName("cancel_cargo");
                fd.setDescription(
                                "Hủy đơn gửi hàng. Chỉ có thể hủy khi đơn ở trạng thái PENDING hoặc CONFIRMED. " +
                                                "Sử dụng khi khách muốn hủy đơn gửi hàng.");

                Schema params = new Schema();
                params.setType("object");
                params.setProperties(Map.of(
                                "cargoCode", createSchema("string", "Mã vận đơn cần hủy (bắt buộc)"),
                                "reason", createSchema("string", "Lý do hủy đơn")));
                params.setRequired(List.of("cargoCode"));
                fd.setParameters(params);

                return fd;
        }
}
