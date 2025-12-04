package com.busify.project.ai_chat.function;

import com.busify.project.booking.dto.request.BookingAddRequestDTO;
import com.busify.project.booking.dto.response.BookingAddResponseDTO;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.booking.service.BookingService;
import com.busify.project.cargo.dto.request.CargoCancelRequestDTO;
import com.busify.project.cargo.dto.request.CargoBookingRequestDTO;
import com.busify.project.cargo.dto.response.CargoBookingResponseDTO;
import com.busify.project.cargo.dto.response.CargoDetailResponseDTO;
import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.cargo.enums.CargoType;
import com.busify.project.cargo.service.CargoService;
import com.busify.project.payment.dto.request.PaymentRequestDTO;
import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import com.busify.project.payment.service.PaymentService;
import com.busify.project.promotion.dto.response.PromotionResponseDTO;
import com.busify.project.promotion.service.PromotionService;
import com.busify.project.refund.dto.request.RefundRequestDTO;
import com.busify.project.refund.dto.response.RefundResponseDTO;
import com.busify.project.refund.service.RefundService;
import com.busify.project.route.entity.Route;
import com.busify.project.route.repository.RouteRepository;
import com.busify.project.trip.dto.request.TripFilterRequestDTO;
import com.busify.project.trip.dto.response.FilterResponseDTO;
import com.busify.project.trip.dto.response.TripFilterResponseDTO;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.trip.service.TripService;
import com.busify.project.trip_seat.dto.SeatStatus;
import com.busify.project.trip_seat.enums.TripSeatStatus;
import com.busify.project.trip_seat.services.TripSeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service thực thi các function calls từ AI
 * Kết nối giữa Gemini AI và các business services
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AIFunctionExecutor {

    private final TripService tripService;
    private final TripRepository tripRepository;
    private final PromotionService promotionService;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final RouteRepository routeRepository;
    private final TripSeatService tripSeatService;
    private final PaymentService paymentService;
    private final RefundService refundService;
    private final CargoService cargoService;

    /**
     * Thực thi function call và trả về kết quả
     */
    public String executeFunction(String functionName, Map<String, Object> args) {
        log.info("Executing AI function: {} with args: {}", functionName, args);

        try {
            return switch (functionName) {
                case "search_trips" -> searchTrips(args);
                case "get_active_promotions" -> getActivePromotions(args);
                case "get_booking_info" -> getBookingInfo(args);
                case "get_route_info" -> getRouteInfo(args);
                // Booking Flow Functions
                case "get_available_seats" -> getAvailableSeats(args);
                case "create_booking" -> createBooking(args);
                case "initiate_payment" -> initiatePayment(args);
                // Cancel Booking Function
                case "cancel_booking" -> cancelBooking(args);
                // New Features
                case "change_seat" -> changeSeat(args);
                case "get_booking_history" -> getBookingHistory(args);
                case "search_best_trips" -> searchBestTrips(args);
                case "search_round_trip" -> searchRoundTrip(args);
                // Cargo Functions (chỉ hỗ trợ tính phí, tra cứu, hủy)
                case "calculate_cargo_fee" -> calculateCargoFee(args);
                case "get_cargo_info" -> getCargoInfo(args);
                case "cancel_cargo" -> cancelCargo(args);
                default -> "Không tìm thấy function: " + functionName;
            };
        } catch (Exception e) {
            log.error("Error executing function {}: {}", functionName, e.getMessage(), e);
            return "Lỗi khi thực hiện truy vấn: " + e.getMessage();
        }
    }

    /**
     * Tìm kiếm chuyến xe
     */
    private String searchTrips(Map<String, Object> args) {
        String startCity = (String) args.getOrDefault("startCity", null);
        String endCity = (String) args.getOrDefault("endCity", null);
        String departureDateStr = (String) args.getOrDefault("departureDate", null);
        int limit = args.get("limit") != null ? ((Number) args.get("limit")).intValue() : 5;

        TripFilterRequestDTO filter = new TripFilterRequestDTO();

        if (startCity != null && !startCity.isEmpty()) {
            filter.setStartCity(startCity);
        }
        if (endCity != null && !endCity.isEmpty()) {
            filter.setEndCity(endCity);
        }
        if (departureDateStr != null && !departureDateStr.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(departureDateStr, DateTimeFormatter.ISO_DATE);
                filter.setDepartureDate(date.atStartOfDay());
            } catch (Exception e) {
                log.warn("Invalid date format: {}", departureDateStr);
            }
        }

        // Mặc định sắp xếp theo thời gian khởi hành
        filter.setSortBy("departureTime");
        filter.setSortDirection("ASC");

        FilterResponseDTO result = tripService.filterTrips(filter, 0, limit);

        if (result == null || result.getData() == null || result.getData().isEmpty()) {
            return buildNoTripsResponse(startCity, endCity, departureDateStr);
        }

        return formatTripsResponse(result.getData(), startCity, endCity);
    }

    /**
     * Format kết quả tìm kiếm chuyến xe thành text
     */
    private String formatTripsResponse(List<TripFilterResponseDTO> trips, String startCity, String endCity) {
        StringBuilder sb = new StringBuilder();

        String routeInfo = "";
        if (startCity != null && endCity != null) {
            routeInfo = String.format(" tuyến %s - %s", startCity, endCity);
        } else if (startCity != null) {
            routeInfo = String.format(" từ %s", startCity);
        } else if (endCity != null) {
            routeInfo = String.format(" đến %s", endCity);
        }

        sb.append(String.format("Tìm thấy %d chuyến xe ĐANG MỞ BÁN%s:\n\n", trips.size(), routeInfo));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        for (int i = 0; i < trips.size(); i++) {
            TripFilterResponseDTO trip = trips.get(i);
            sb.append(String.format("%d. **%s** (ID: %d)\n", i + 1, trip.getOperator_name(), trip.getTrip_id()));

            if (trip.getRoute() != null) {
                sb.append(String.format("   📍 Tuyến: %s → %s\n",
                        trip.getRoute().getStart_location(),
                        trip.getRoute().getEnd_location()));
            }

            sb.append(String.format("   🕐 Khởi hành: %s\n",
                    trip.getDeparture_time().format(timeFormatter)));

            if (trip.getArrival_time() != null) {
                sb.append(String.format("   🏁 Dự kiến đến: %s\n",
                        trip.getArrival_time().format(timeFormatter)));
            }

            sb.append(String.format("   💰 Giá vé: %,.0f VNĐ\n", trip.getPrice_per_seat()));
            sb.append(String.format("   🪑 Còn trống: %d/%d ghế\n",
                    trip.getAvailable_seats(), trip.getTotal_seats()));

            // Hiển thị tiện ích của xe
            if (trip.getAmenities() != null && !trip.getAmenities().isEmpty()) {
                List<String> activeAmenities = trip.getAmenities().entrySet().stream()
                        .filter(e -> Boolean.TRUE.equals(e.getValue()))
                        .map(e -> formatAmenityName(e.getKey()))
                        .toList();
                if (!activeAmenities.isEmpty()) {
                    sb.append(String.format("   🚌 Tiện ích: %s\n", String.join(", ", activeAmenities)));
                }
            }

            if (trip.getAverage_rating() != null && trip.getAverage_rating() > 0) {
                sb.append(String.format("   ⭐ Đánh giá: %.1f/5\n", trip.getAverage_rating()));
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Format tên tiện ích từ key sang tên hiển thị tiếng Việt
     */
    private String formatAmenityName(String key) {
        return switch (key.toLowerCase()) {
            case "wifi" -> "WiFi";
            case "air_conditioning", "airconditioning", "ac" -> "Điều hòa";
            case "tv" -> "TV";
            case "usb_charging", "usbcharging", "usb" -> "Sạc USB";
            case "blanket" -> "Chăn";
            case "pillow" -> "Gối";
            case "water", "free_water" -> "Nước miễn phí";
            case "snack", "food" -> "Đồ ăn nhẹ";
            case "toilet", "restroom" -> "Toilet";
            case "legroom", "leg_room" -> "Chỗ để chân rộng";
            case "reclining_seat", "recliningseat" -> "Ghế ngả";
            case "reading_light", "readinglight" -> "Đèn đọc sách";
            case "curtain" -> "Rèm cửa";
            case "footrest" -> "Kê chân";
            default -> key.replace("_", " ");
        };
    }

    private String buildNoTripsResponse(String startCity, String endCity, String date) {
        StringBuilder sb = new StringBuilder("Không tìm thấy chuyến xe ĐANG MỞ BÁN");
        if (startCity != null && endCity != null) {
            sb.append(String.format(" từ %s đến %s", startCity, endCity));
        }
        if (date != null) {
            sb.append(String.format(" vào ngày %s", date));
        }
        sb.append(". Các chuyến có thể đã hết vé hoặc chưa mở bán. Vui lòng thử tìm với tiêu chí khác hoặc ngày khác.");
        return sb.toString();
    }

    /**
     * Lấy danh sách khuyến mãi đang hoạt động
     */
    private String getActivePromotions(Map<String, Object> args) {
        int limit = args.get("limit") != null ? ((Number) args.get("limit")).intValue() : 5;

        List<PromotionResponseDTO> promotions = promotionService.getAllCurrentPromotions();

        if (promotions == null || promotions.isEmpty()) {
            return "Hiện tại không có chương trình khuyến mãi nào đang hoạt động.";
        }

        // Limit results
        if (promotions.size() > limit) {
            promotions = promotions.subList(0, limit);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Có %d khuyến mãi đang hoạt động:\n\n", promotions.size()));

        for (int i = 0; i < promotions.size(); i++) {
            PromotionResponseDTO promo = promotions.get(i);
            sb.append(String.format("%d. **%s**\n", i + 1,
                    promo.getCode() != null ? promo.getCode() : "Tự động áp dụng"));

            String discountText;
            if ("PERCENTAGE".equals(String.valueOf(promo.getDiscountType()))) {
                discountText = String.format("Giảm %s%%", promo.getDiscountValue());
            } else {
                discountText = String.format("Giảm %,.0f VNĐ", promo.getDiscountValue());
            }
            sb.append(String.format("   🎁 %s\n", discountText));

            if (promo.getMinOrderValue() != null && promo.getMinOrderValue().doubleValue() > 0) {
                sb.append(String.format("   📌 Đơn tối thiểu: %,.0f VNĐ\n", promo.getMinOrderValue()));
            }

            sb.append(String.format("   📅 Hiệu lực: %s - %s\n",
                    promo.getStartDate(), promo.getEndDate()));

            if (promo.getUsageLimit() != null) {
                sb.append(String.format("   🔢 Giới hạn: %d lượt\n", promo.getUsageLimit()));
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Tra cứu thông tin đặt vé
     */
    private String getBookingInfo(Map<String, Object> args) {
        String bookingCode = (String) args.get("bookingCode");

        if (bookingCode == null || bookingCode.isEmpty()) {
            return "Vui lòng cung cấp mã đặt vé để tra cứu.";
        }

        Optional<Bookings> bookingOpt = bookingRepository.findByBookingCode(bookingCode);

        if (bookingOpt.isEmpty()) {
            return String.format("Không tìm thấy đơn đặt vé với mã: %s. Vui lòng kiểm tra lại mã đặt vé.", bookingCode);
        }

        Bookings booking = bookingOpt.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("📋 **Thông tin đặt vé: %s**\n\n", bookingCode));
        sb.append(String.format("🎫 Trạng thái: %s\n", formatBookingStatus(booking.getStatus().name())));
        sb.append(String.format("💺 Số ghế: %s\n", booking.getSeatNumber()));
        sb.append(String.format("💰 Tổng tiền: %,.0f VNĐ\n", booking.getTotalAmount()));

        if (booking.getTrip() != null) {
            sb.append(String.format("🕐 Khởi hành: %s\n",
                    booking.getTrip().getDepartureTime().format(formatter)));
            if (booking.getTrip().getRoute() != null) {
                sb.append(String.format("📍 Tuyến: %s → %s\n",
                        booking.getTrip().getRoute().getStartLocation().getName(),
                        booking.getTrip().getRoute().getEndLocation().getName()));
            }
        }

        sb.append(String.format("📅 Ngày đặt: %s\n", booking.getCreatedAt().format(formatter)));

        return sb.toString();
    }

    private String formatBookingStatus(String status) {
        return switch (status.toLowerCase()) {
            case "pending" -> "⏳ Đang chờ thanh toán";
            case "confirmed" -> "✅ Đã xác nhận";
            case "canceled_by_user" -> "❌ Đã hủy bởi khách hàng";
            case "canceled_by_operator" -> "❌ Đã hủy bởi nhà xe";
            case "completed" -> "✔️ Hoàn thành";
            default -> status;
        };
    }

    /**
     * Lấy thông tin tuyến đường
     */
    private String getRouteInfo(Map<String, Object> args) {
        int limit = args.get("limit") != null ? ((Number) args.get("limit")).intValue() : 10;

        List<Route> routes = routeRepository.findAll();

        if (routes.isEmpty()) {
            return "Hiện tại chưa có thông tin tuyến đường trong hệ thống.";
        }

        if (routes.size() > limit) {
            routes = routes.subList(0, limit);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Có %d tuyến đường phổ biến:\n\n", routes.size()));

        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            sb.append(String.format("%d. **%s**\n", i + 1, route.getName()));

            if (route.getStartLocation() != null && route.getEndLocation() != null) {
                sb.append(String.format("   📍 %s → %s\n",
                        route.getStartLocation().getName(),
                        route.getEndLocation().getName()));
            }

            if (route.getDefaultDurationMinutes() != null) {
                int hours = route.getDefaultDurationMinutes() / 60;
                int mins = route.getDefaultDurationMinutes() % 60;
                sb.append(String.format("   ⏱️ Thời gian: %dh%02dp\n", hours, mins));
            }

            if (route.getDefaultPrice() != null) {
                sb.append(String.format("   💰 Giá tham khảo: %,.0f VNĐ\n", route.getDefaultPrice()));
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    // ======================= BOOKING FLOW FUNCTIONS =======================

    /**
     * Lấy danh sách ghế trống của một chuyến xe
     */
    private String getAvailableSeats(Map<String, Object> args) {
        Long tripId = args.get("tripId") != null ? ((Number) args.get("tripId")).longValue() : null;

        if (tripId == null) {
            return "Vui lòng cung cấp ID chuyến xe (tripId) để xem ghế trống.";
        }

        // Kiểm tra trip có tồn tại không
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        if (tripOpt.isEmpty()) {
            return String.format("Không tìm thấy chuyến xe với ID: %d", tripId);
        }

        Trip trip = tripOpt.get();
        List<SeatStatus> seats = tripSeatService.getTripSeatsStatus(tripId);

        if (seats == null || seats.isEmpty()) {
            return "Không có thông tin ghế cho chuyến xe này.";
        }

        // Phân loại ghế theo trạng thái
        List<String> availableSeats = new ArrayList<>();
        List<String> bookedSeats = new ArrayList<>();
        List<String> lockedSeats = new ArrayList<>();

        for (SeatStatus seat : seats) {
            if (seat.getStatus() == TripSeatStatus.available) {
                availableSeats.add(seat.getSeatNumber());
            } else if (seat.getStatus() == TripSeatStatus.booked) {
                bookedSeats.add(seat.getSeatNumber());
            } else if (seat.getStatus() == TripSeatStatus.locked) {
                lockedSeats.add(seat.getSeatNumber());
            }
        }

        // Sắp xếp ghế theo thứ tự
        Collections.sort(availableSeats);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("🪑 **Sơ đồ ghế chuyến xe #%d**\n", tripId));

        if (trip.getRoute() != null) {
            sb.append(String.format("📍 Tuyến: %s → %s\n",
                    trip.getRoute().getStartLocation().getName(),
                    trip.getRoute().getEndLocation().getName()));
        }
        sb.append(String.format("🕐 Khởi hành: %s\n\n",
                trip.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))));

        // Giải thích format ghế nếu có dạng X.Y.Z (Dãy.Số.Tầng)
        if (!availableSeats.isEmpty() && availableSeats.get(0).contains(".")) {
            sb.append("📌 **Format ghế**: Dãy.Số.Tầng (VD: A.1.1 = Dãy A, Ghế số 1, Tầng 1)\n\n");

            // Nhóm ghế theo tầng
            Map<String, List<String>> seatsByFloor = groupSeatsByFloor(availableSeats);

            sb.append(String.format("✅ **Ghế trống** (%d ghế):\n", availableSeats.size()));
            for (Map.Entry<String, List<String>> entry : seatsByFloor.entrySet()) {
                sb.append(String.format("   • Tầng %s: %s\n", entry.getKey(), String.join(", ", entry.getValue())));
            }
        } else {
            sb.append(String.format("✅ **Ghế trống** (%d ghế): %s\n",
                    availableSeats.size(),
                    availableSeats.isEmpty() ? "Hết ghế" : String.join(", ", availableSeats)));
        }

        sb.append("\n");
        if (!bookedSeats.isEmpty()) {
            sb.append(String.format("❌ Đã đặt (%d ghế)\n", bookedSeats.size()));
        }
        if (!lockedSeats.isEmpty()) {
            sb.append(String.format("🔒 Đang giữ (%d ghế)\n", lockedSeats.size()));
        }

        if (!availableSeats.isEmpty()) {
            sb.append("\n💡 Bạn muốn đặt ghế nào? (VD: A.1.1 hoặc A.1.1, A.2.1 nếu đặt nhiều ghế)");
        }

        return sb.toString();
    }

    /**
     * Nhóm ghế theo tầng (từ format X.Y.Z lấy Z là tầng)
     */
    private Map<String, List<String>> groupSeatsByFloor(List<String> seats) {
        Map<String, List<String>> result = new TreeMap<>();
        for (String seat : seats) {
            String[] parts = seat.split("\\.");
            String floor = parts.length >= 3 ? parts[2] : "1";
            result.computeIfAbsent(floor, k -> new ArrayList<>()).add(seat);
        }
        return result;
    }

    /**
     * Tạo đơn đặt vé mới
     */
    private String createBooking(Map<String, Object> args) {
        Long tripId = args.get("tripId") != null ? ((Number) args.get("tripId")).longValue() : null;
        String seatNumbers = (String) args.get("seatNumbers");
        String guestFullName = (String) args.get("guestFullName");
        String guestPhone = (String) args.get("guestPhone");
        String guestEmail = (String) args.get("guestEmail");
        String discountCode = (String) args.get("discountCode");

        // Validate required fields
        if (tripId == null) {
            return "❌ Thiếu thông tin chuyến xe (tripId). Vui lòng chọn chuyến xe trước.";
        }
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            return "❌ Thiếu số ghế. Vui lòng chọn ghế muốn đặt.";
        }
        if (guestFullName == null || guestFullName.isEmpty()) {
            return "❌ Thiếu họ tên hành khách. Vui lòng cung cấp họ tên.";
        }
        if (guestPhone == null || guestPhone.isEmpty()) {
            return "❌ Thiếu số điện thoại. Vui lòng cung cấp SĐT.";
        }
        if (guestEmail == null || guestEmail.isEmpty()) {
            return "❌ Thiếu email. Vui lòng cung cấp email để nhận vé.";
        }

        // Kiểm tra trip có tồn tại không
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        if (tripOpt.isEmpty()) {
            return String.format("❌ Không tìm thấy chuyến xe với ID: %d", tripId);
        }

        Trip trip = tripOpt.get();
        BigDecimal pricePerSeat = trip.getPricePerSeat();
        int seatCount = seatNumbers.split(",").length;
        BigDecimal totalAmount = pricePerSeat.multiply(BigDecimal.valueOf(seatCount));

        try {
            // Tạo booking request
            BookingAddRequestDTO request = new BookingAddRequestDTO();
            request.setTripId(tripId);
            request.setSeatNumber(seatNumbers);
            request.setGuestFullName(guestFullName);
            request.setGuestPhone(guestPhone);
            request.setGuestEmail(guestEmail);
            request.setTotalAmount(totalAmount);

            if (discountCode != null && !discountCode.isEmpty()) {
                request.setDiscountCode(discountCode);
            }

            // Gọi service tạo booking
            BookingAddResponseDTO response = bookingService.addBooking(request);

            StringBuilder sb = new StringBuilder();
            sb.append("✅ **ĐẶT VÉ THÀNH CÔNG!**\n\n");
            sb.append(String.format("📋 Mã đặt vé: **%s**\n", response.getBookingCode()));
            sb.append(String.format("🎫 Booking ID: %d\n", response.getBookingId()));
            sb.append(String.format("💺 Ghế: %s\n", response.getSeatNumber()));
            sb.append(String.format("💰 Tổng tiền: **%,.0f VNĐ**\n", response.getTotalAmount()));
            sb.append(String.format("📊 Trạng thái: %s\n\n", formatBookingStatus(response.getStatus().name())));

            sb.append("📍 **Thông tin chuyến xe:**\n");
            if (trip.getRoute() != null) {
                sb.append(String.format("   • Tuyến: %s → %s\n",
                        trip.getRoute().getStartLocation().getName(),
                        trip.getRoute().getEndLocation().getName()));
            }
            sb.append(String.format("   • Khởi hành: %s\n",
                    trip.getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))));

            sb.append("\n⏳ **Vui lòng thanh toán trong 15 phút** để giữ chỗ!\n");
            sb.append("💳 Bạn muốn thanh toán bằng VNPay, MoMo hay PayPal?");

            // Lưu bookingId để dùng cho bước thanh toán
            sb.append(String.format("\n\n[BOOKING_ID:%d]", response.getBookingId()));

            return sb.toString();

        } catch (Exception e) {
            log.error("Error creating booking: {}", e.getMessage(), e);
            return String.format("❌ Lỗi khi đặt vé: %s\nVui lòng thử lại hoặc liên hệ hotline 1900 6067.",
                    e.getMessage());
        }
    }

    /**
     * Khởi tạo thanh toán cho booking
     */
    private String initiatePayment(Map<String, Object> args) {
        Long bookingId = args.get("bookingId") != null ? ((Number) args.get("bookingId")).longValue() : null;
        String paymentMethodStr = (String) args.getOrDefault("paymentMethod", "VNPAY");

        if (bookingId == null) {
            return "❌ Thiếu booking ID. Vui lòng tạo đơn đặt vé trước.";
        }

        // Parse payment method
        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(paymentMethodStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return String.format("❌ Phương thức thanh toán '%s' không hợp lệ. Vui lòng chọn: VNPAY, MOMO, hoặc PAYPAL.",
                    paymentMethodStr);
        }

        try {
            PaymentRequestDTO request = PaymentRequestDTO.builder()
                    .bookingId(bookingId)
                    .paymentMethod(paymentMethod)
                    .build();

            PaymentResponseDTO response = paymentService.createPayment(request);

            StringBuilder sb = new StringBuilder();

            // SIMULATION payment auto-completes immediately (paymentUrl = null)
            if (paymentMethod == PaymentMethod.SIMULATION) {
                if (response.getStatus() == com.busify.project.payment.enums.PaymentStatus.completed) {
                    sb.append("✅ **THANH TOÁN THÀNH CÔNG!** (Chế độ Test)\n\n");
                    sb.append(String.format("📋 Booking ID: %d\n", bookingId));
                    sb.append(String.format("💰 Phương thức: %s\n\n", formatPaymentMethod(paymentMethod)));
                    sb.append("🎉 Vé của bạn đã được xác nhận!\n");
                    sb.append("📧 Thông tin vé sẽ được gửi qua email.\n\n");
                    sb.append("💡 Đây là thanh toán TEST, không tính phí thật.");
                    return sb.toString();
                }
            }

            // Other payment methods need redirect URL
            if (response.getPaymentUrl() == null || response.getPaymentUrl().isEmpty()) {
                return "❌ Không thể tạo link thanh toán. Vui lòng thử lại hoặc liên hệ hotline 1900 6067.";
            }

            sb.append("💳 **LINK THANH TOÁN**\n\n");
            sb.append(String.format("📋 Booking ID: %d\n", bookingId));
            sb.append(String.format("💰 Phương thức: %s\n\n", formatPaymentMethod(paymentMethod)));

            sb.append("👇 **Click link bên dưới để thanh toán:**\n");
            sb.append(String.format("🔗 %s\n\n", response.getPaymentUrl()));

            sb.append("⏳ Link có hiệu lực trong **15 phút**.\n");
            sb.append("✅ Sau khi thanh toán thành công, bạn sẽ nhận được vé qua email.\n\n");
            sb.append("💡 Nếu gặp vấn đề, vui lòng liên hệ hotline 1900 6067.");

            return sb.toString();

        } catch (Exception e) {
            log.error("Error creating payment: {}", e.getMessage(), e);
            return String.format("❌ Lỗi khi tạo thanh toán: %s\nVui lòng thử lại hoặc liên hệ hotline 1900 6067.",
                    e.getMessage());
        }
    }

    /**
     * Format tên phương thức thanh toán
     */
    private String formatPaymentMethod(PaymentMethod method) {
        return switch (method) {
            case VNPAY -> "VNPay (ATM/Visa/Mastercard)";
            case MOMO -> "Ví MoMo";
            case PAYPAL -> "PayPal";
            case SIMULATION -> "Test Payment";
            case CREDIT_CARD -> "Thẻ tín dụng";
            case BANK_TRANSFER -> "Chuyển khoản";
        };
    }

    // ======================= CANCEL BOOKING FUNCTION =======================

    /**
     * Hủy vé và yêu cầu hoàn tiền
     */
    private String cancelBooking(Map<String, Object> args) {
        String bookingCode = (String) args.get("bookingCode");
        String reason = (String) args.get("reason");

        if (bookingCode == null || bookingCode.isEmpty()) {
            return "❌ Vui lòng cung cấp mã đặt vé (bookingCode) để hủy.";
        }
        if (reason == null || reason.isEmpty()) {
            return "❌ Vui lòng cung cấp lý do hủy vé.";
        }

        // Tìm booking
        Optional<Bookings> bookingOpt = bookingRepository.findByBookingCode(bookingCode);
        if (bookingOpt.isEmpty()) {
            return String.format("❌ Không tìm thấy đơn đặt vé với mã: **%s**\nVui lòng kiểm tra lại mã vé.",
                    bookingCode);
        }

        Bookings booking = bookingOpt.get();

        // Kiểm tra trạng thái booking
        if (booking.getStatus() == BookingStatus.canceled_by_user ||
                booking.getStatus() == BookingStatus.canceled_by_operator) {
            return String.format("❌ Đơn đặt vé **%s** đã được hủy trước đó.", bookingCode);
        }

        if (booking.getStatus() == BookingStatus.completed) {
            return String.format("❌ Đơn đặt vé **%s** đã hoàn thành, không thể hủy.", bookingCode);
        }

        // Kiểm tra payment
        if (booking.getPayment() == null) {
            // Booking chưa thanh toán - hủy trực tiếp
            try {
                booking.setStatus(BookingStatus.canceled_by_user);
                bookingRepository.save(booking);

                // Release ghế
                String[] seats = booking.getSeatNumber().split(",");
                for (String seat : seats) {
                    tripSeatService.changeTripSeatStatusToAvailable(booking.getTrip().getId(), seat.trim());
                }

                StringBuilder sb = new StringBuilder();
                sb.append("✅ **HỦY VÉ THÀNH CÔNG!**\n\n");
                sb.append(String.format("📋 Mã vé: **%s**\n", bookingCode));
                sb.append(String.format("💺 Ghế: %s\n", booking.getSeatNumber()));
                sb.append("💰 Vé chưa thanh toán nên không có hoàn tiền.\n");
                sb.append(String.format("📝 Lý do: %s\n\n", reason));
                sb.append("Cảm ơn bạn đã sử dụng dịch vụ Busify! 🙏");

                return sb.toString();
            } catch (Exception e) {
                log.error("Error canceling unpaid booking: {}", e.getMessage(), e);
                return "❌ Lỗi khi hủy vé. Vui lòng liên hệ hotline 1900 6067.";
            }
        }

        // Booking đã thanh toán - cần tạo refund
        if (booking.getPayment().getStatus() != PaymentStatus.completed) {
            return String.format("❌ Đơn đặt vé **%s** chưa thanh toán hoàn tất, không thể hoàn tiền.", bookingCode);
        }

        try {
            // Tạo refund request
            RefundRequestDTO refundRequest = new RefundRequestDTO();
            refundRequest.setPaymentId(booking.getPayment().getPaymentId());
            refundRequest.setRefundReason(reason);
            refundRequest.setNotes("Hủy vé qua AI Chat - Mã vé: " + bookingCode);

            // Gọi refund service
            RefundResponseDTO refundResponse = refundService.createRefund(refundRequest);

            // Cập nhật booking status
            booking.setStatus(BookingStatus.canceled_by_user);
            bookingRepository.save(booking);

            // Release ghế
            String[] seats = booking.getSeatNumber().split(",");
            for (String seat : seats) {
                tripSeatService.changeTripSeatStatusToAvailable(booking.getTrip().getId(), seat.trim());
            }

            StringBuilder sb = new StringBuilder();
            sb.append("✅ **HỦY VÉ VÀ HOÀN TIỀN THÀNH CÔNG!**\n\n");
            sb.append(String.format("📋 Mã vé: **%s**\n", bookingCode));
            sb.append(String.format("💺 Ghế đã hủy: %s\n", booking.getSeatNumber()));
            sb.append(String.format("📝 Lý do: %s\n\n", reason));

            sb.append("💰 **Thông tin hoàn tiền:**\n");
            sb.append(String.format("   • Số tiền gốc: %,.0f VNĐ\n",
                    refundResponse.getRefundAmount().add(refundResponse.getCancellationFee())));
            sb.append(String.format("   • Phí hủy: %,.0f VNĐ\n", refundResponse.getCancellationFee()));
            sb.append(String.format("   • **Số tiền hoàn lại: %,.0f VNĐ**\n", refundResponse.getNetRefundAmount()));
            sb.append(String.format("   • Trạng thái: %s\n\n", formatRefundStatus(refundResponse.getStatus().name())));

            sb.append("📧 Thông tin hoàn tiền sẽ được gửi qua email.\n");
            sb.append("⏳ Tiền sẽ được hoàn trong 3-5 ngày làm việc.\n\n");
            sb.append("Cảm ơn bạn đã sử dụng dịch vụ Busify! 🙏");

            return sb.toString();

        } catch (Exception e) {
            log.error("Error canceling booking with refund: {}", e.getMessage(), e);
            return String.format("❌ Lỗi khi hủy vé: %s\nVui lòng liên hệ hotline 1900 6067 để được hỗ trợ.",
                    e.getMessage());
        }
    }

    /**
     * Format trạng thái refund
     */
    private String formatRefundStatus(String status) {
        return switch (status.toLowerCase()) {
            case "pending" -> "⏳ Đang xử lý";
            case "processing" -> "🔄 Đang hoàn tiền";
            case "completed" -> "✅ Hoàn thành";
            case "failed" -> "❌ Thất bại";
            case "cancelled" -> "❌ Đã hủy";
            default -> status;
        };
    }

    // ======================= NEW FEATURES =======================

    /**
     * Đổi ghế cho booking đã đặt
     */
    private String changeSeat(Map<String, Object> args) {
        String bookingCode = (String) args.get("bookingCode");
        String newSeatNumber = (String) args.get("newSeatNumber");

        if (bookingCode == null || bookingCode.isEmpty()) {
            return "❌ Vui lòng cung cấp mã đặt vé để đổi ghế.";
        }

        Optional<Bookings> bookingOpt = bookingRepository.findByBookingCode(bookingCode);
        if (bookingOpt.isEmpty()) {
            return String.format("❌ Không tìm thấy đơn đặt vé với mã: %s", bookingCode);
        }

        Bookings booking = bookingOpt.get();

        // Kiểm tra trạng thái booking
        if (booking.getStatus() != BookingStatus.pending && booking.getStatus() != BookingStatus.confirmed) {
            return String.format(
                    "❌ Không thể đổi ghế cho đơn ở trạng thái: %s. Chỉ có thể đổi ghế cho đơn PENDING hoặc CONFIRMED.",
                    formatBookingStatus(booking.getStatus().name()));
        }

        // Kiểm tra chuyến đã khởi hành chưa
        if (booking.getTrip().getDepartureTime().isBefore(java.time.LocalDateTime.now())) {
            return "❌ Không thể đổi ghế vì chuyến xe đã khởi hành.";
        }

        Long tripId = booking.getTrip().getId();
        String oldSeatNumber = booking.getSeatNumber();

        // NẾU CHƯA CÓ GHẾMỚI → Hiển thị danh sách ghế trống để khách chọn
        if (newSeatNumber == null || newSeatNumber.isEmpty()) {
            List<SeatStatus> seats = tripSeatService.getTripSeatsStatus(tripId);
            List<String> availableSeats = seats.stream()
                    .filter(s -> s.getStatus() == TripSeatStatus.available)
                    .map(SeatStatus::getSeatNumber)
                    .sorted()
                    .toList();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("📋 **Thông tin vé: %s**\n", bookingCode));
            sb.append(String.format("💺 Ghế hiện tại: **%s**\n", oldSeatNumber));
            if (booking.getTrip().getRoute() != null) {
                sb.append(String.format("📍 Tuyến: %s → %s\n",
                        booking.getTrip().getRoute().getStartLocation().getName(),
                        booking.getTrip().getRoute().getEndLocation().getName()));
            }
            sb.append(String.format("🕐 Khởi hành: %s\n\n",
                    booking.getTrip().getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))));

            if (availableSeats.isEmpty()) {
                sb.append("❌ Rất tiếc, chuyến xe này **không còn ghế trống** để đổi.\n");
                sb.append("Bạn có thể hủy vé và đặt chuyến khác nếu cần.");
            } else {
                sb.append(String.format("✅ **Ghế trống** (%d ghế):\n", availableSeats.size()));

                // Nhóm theo tầng
                Map<String, List<String>> seatsByFloor = groupSeatsByFloor(availableSeats);
                for (Map.Entry<String, List<String>> entry : seatsByFloor.entrySet()) {
                    sb.append(String.format("**Tầng %s**: %s\n", entry.getKey(), String.join(", ", entry.getValue())));
                }

                sb.append("\n💡 Bạn muốn đổi sang ghế nào? (VD: A.2.1)");
            }

            return sb.toString();
        }

        // CÓ GHẾ MỚI → Tiến hành đổi ghế

        // Kiểm tra ghế mới có trống không
        List<SeatStatus> seats = tripSeatService.getTripSeatsStatus(tripId);
        String[] newSeats = newSeatNumber.split(",");
        List<String> unavailableSeats = new ArrayList<>();

        for (String seat : newSeats) {
            String trimmedSeat = seat.trim();
            boolean isAvailable = seats.stream()
                    .anyMatch(s -> s.getSeatNumber().equals(trimmedSeat) && s.getStatus() == TripSeatStatus.available);
            if (!isAvailable) {
                unavailableSeats.add(trimmedSeat);
            }
        }

        if (!unavailableSeats.isEmpty()) {
            return String.format("❌ Các ghế sau không còn trống: %s\nVui lòng chọn ghế khác.",
                    String.join(", ", unavailableSeats));
        }

        try {
            // Release ghế cũ
            String[] oldSeats = oldSeatNumber.split(",");
            for (String seat : oldSeats) {
                tripSeatService.changeTripSeatStatusToAvailable(tripId, seat.trim());
            }

            // Book ghế mới (cập nhật booking)
            booking.setSeatNumber(newSeatNumber);
            bookingRepository.save(booking);

            // Lock ghế mới
            for (String seat : newSeats) {
                // Note: Cần có method lock seat, tạm thời để như vậy
            }

            StringBuilder sb = new StringBuilder();
            sb.append("✅ **ĐỔI GHẾ THÀNH CÔNG!**\n\n");
            sb.append(String.format("📋 Mã vé: **%s**\n", bookingCode));
            sb.append(String.format("💺 Ghế cũ: %s\n", oldSeatNumber));
            sb.append(String.format("💺 **Ghế mới: %s**\n\n", newSeatNumber));

            if (booking.getTrip().getRoute() != null) {
                sb.append(String.format("📍 Tuyến: %s → %s\n",
                        booking.getTrip().getRoute().getStartLocation().getName(),
                        booking.getTrip().getRoute().getEndLocation().getName()));
            }
            sb.append(String.format("🕐 Khởi hành: %s\n",
                    booking.getTrip().getDepartureTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))));

            sb.append("\n✨ Chúc bạn có chuyến đi vui vẻ!");

            return sb.toString();

        } catch (Exception e) {
            log.error("Error changing seat: {}", e.getMessage(), e);
            return String.format("❌ Lỗi khi đổi ghế: %s\nVui lòng liên hệ hotline 1900 6067.", e.getMessage());
        }
    }

    /**
     * Lấy lịch sử đặt vé của user hiện tại
     */
    private String getBookingHistory(Map<String, Object> args) {
        String statusFilter = (String) args.getOrDefault("status", null);
        int limit = args.get("limit") != null ? ((Number) args.get("limit")).intValue() : 10;

        // Lấy tất cả bookings (trong thực tế cần filter theo user đang login)
        List<Bookings> bookings = bookingRepository.findAll();

        // Filter theo status nếu có
        if (statusFilter != null && !statusFilter.isEmpty()) {
            try {
                BookingStatus status = BookingStatus.valueOf(statusFilter.toLowerCase());
                bookings = bookings.stream()
                        .filter(b -> b.getStatus() == status)
                        .toList();
            } catch (IllegalArgumentException e) {
                log.warn("Invalid booking status filter: {}", statusFilter);
            }
        }

        // Sort theo ngày tạo mới nhất
        bookings = bookings.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .toList();

        if (bookings.isEmpty()) {
            return "📋 Bạn chưa có đơn đặt vé nào" +
                    (statusFilter != null ? " với trạng thái " + statusFilter : "") + ".";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("📋 **LỊCH SỬ ĐẶT VÉ** (%d đơn gần nhất)\n\n", bookings.size()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        for (int i = 0; i < bookings.size(); i++) {
            Bookings booking = bookings.get(i);
            sb.append(String.format("**%d. Mã vé: %s**\n", i + 1, booking.getBookingCode()));
            sb.append(String.format("   📊 Trạng thái: %s\n", formatBookingStatus(booking.getStatus().name())));
            sb.append(String.format("   💺 Ghế: %s\n", booking.getSeatNumber()));
            sb.append(String.format("   💰 Tổng tiền: %,.0f VNĐ\n", booking.getTotalAmount()));

            if (booking.getTrip() != null) {
                if (booking.getTrip().getRoute() != null) {
                    sb.append(String.format("   📍 Tuyến: %s → %s\n",
                            booking.getTrip().getRoute().getStartLocation().getName(),
                            booking.getTrip().getRoute().getEndLocation().getName()));
                }
                sb.append(String.format("   🕐 Khởi hành: %s\n",
                        booking.getTrip().getDepartureTime().format(formatter)));
            }

            sb.append(String.format("   📅 Ngày đặt: %s\n\n", booking.getCreatedAt().format(formatter)));
        }

        sb.append("💡 Nói mã vé để xem chi tiết hoặc thực hiện thao tác (đổi ghế, hủy vé...)");

        return sb.toString();
    }

    /**
     * Tìm chuyến xe tốt nhất theo tiêu chí
     */
    private String searchBestTrips(Map<String, Object> args) {
        String startCity = (String) args.getOrDefault("startCity", null);
        String endCity = (String) args.getOrDefault("endCity", null);
        String departureDateStr = (String) args.getOrDefault("departureDate", null);
        String sortBy = (String) args.getOrDefault("sortBy", "CHEAPEST");
        int limit = args.get("limit") != null ? ((Number) args.get("limit")).intValue() : 5;

        TripFilterRequestDTO filter = new TripFilterRequestDTO();

        if (startCity != null && !startCity.isEmpty()) {
            filter.setStartCity(startCity);
        }
        if (endCity != null && !endCity.isEmpty()) {
            filter.setEndCity(endCity);
        }
        if (departureDateStr != null && !departureDateStr.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(departureDateStr, DateTimeFormatter.ISO_DATE);
                filter.setDepartureDate(date.atStartOfDay());
            } catch (Exception e) {
                log.warn("Invalid date format: {}", departureDateStr);
            }
        }

        // Set sort theo tiêu chí
        switch (sortBy.toUpperCase()) {
            case "CHEAPEST" -> {
                filter.setSortBy("pricePerSeat");
                filter.setSortDirection("ASC");
            }
            case "BEST_RATED" -> {
                filter.setSortBy("averageRating");
                filter.setSortDirection("DESC");
            }
            case "MOST_AVAILABLE" -> {
                filter.setSortBy("availableSeats");
                filter.setSortDirection("DESC");
            }
            default -> {
                filter.setSortBy("pricePerSeat");
                filter.setSortDirection("ASC");
            }
        }

        FilterResponseDTO result = tripService.filterTrips(filter, 0, limit);

        if (result == null || result.getData() == null || result.getData().isEmpty()) {
            return buildNoTripsResponse(startCity, endCity, departureDateStr);
        }

        String criteriaText = switch (sortBy.toUpperCase()) {
            case "CHEAPEST" -> "RẺ NHẤT 💰";
            case "BEST_RATED" -> "ĐÁNH GIÁ CAO NHẤT ⭐";
            case "MOST_AVAILABLE" -> "NHIỀU GHẾ TRỐNG NHẤT 🪑";
            default -> "TỐT NHẤT";
        };

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("🏆 **TOP %d CHUYẾN XE %s**\n\n", result.getData().size(), criteriaText));

        return sb.toString() + formatTripsResponseWithRank(result.getData(), sortBy);
    }

    /**
     * Format trips response với ranking
     */
    private String formatTripsResponseWithRank(List<TripFilterResponseDTO> trips, String sortBy) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        for (int i = 0; i < trips.size(); i++) {
            TripFilterResponseDTO trip = trips.get(i);

            String medal = switch (i) {
                case 0 -> "🥇";
                case 1 -> "🥈";
                case 2 -> "🥉";
                default -> String.valueOf(i + 1) + ".";
            };

            sb.append(String.format("%s **%s** (ID: %d)\n", medal, trip.getOperator_name(), trip.getTrip_id()));

            if (trip.getRoute() != null) {
                sb.append(String.format("   📍 %s → %s\n",
                        trip.getRoute().getStart_location(),
                        trip.getRoute().getEnd_location()));
            }

            sb.append(String.format("   🕐 Khởi hành: %s\n", trip.getDeparture_time().format(timeFormatter)));

            // Highlight theo tiêu chí
            if ("CHEAPEST".equals(sortBy.toUpperCase())) {
                sb.append(String.format("   💰 **Giá: %,.0f VNĐ**\n", trip.getPrice_per_seat()));
            } else {
                sb.append(String.format("   💰 Giá: %,.0f VNĐ\n", trip.getPrice_per_seat()));
            }

            if ("MOST_AVAILABLE".equals(sortBy.toUpperCase())) {
                sb.append(String.format("   🪑 **Còn trống: %d/%d ghế**\n",
                        trip.getAvailable_seats(), trip.getTotal_seats()));
            } else {
                sb.append(String.format("   🪑 Còn trống: %d/%d ghế\n",
                        trip.getAvailable_seats(), trip.getTotal_seats()));
            }

            if (trip.getAverage_rating() != null && trip.getAverage_rating() > 0) {
                if ("BEST_RATED".equals(sortBy.toUpperCase())) {
                    sb.append(String.format("   ⭐ **Đánh giá: %.1f/5**\n", trip.getAverage_rating()));
                } else {
                    sb.append(String.format("   ⭐ Đánh giá: %.1f/5\n", trip.getAverage_rating()));
                }
            }

            sb.append("\n");
        }

        sb.append("💡 Bạn muốn đặt chuyến nào? Cho mình biết số thứ tự hoặc ID chuyến nhé!");

        return sb.toString();
    }

    /**
     * Tìm chuyến khứ hồi
     */
    private String searchRoundTrip(Map<String, Object> args) {
        String startCity = (String) args.get("startCity");
        String endCity = (String) args.get("endCity");
        String departureDateStr = (String) args.get("departureDate");
        String returnDateStr = (String) args.get("returnDate");
        int limit = args.get("limit") != null ? ((Number) args.get("limit")).intValue() : 3;

        if (startCity == null || endCity == null || departureDateStr == null || returnDateStr == null) {
            return "❌ Vui lòng cung cấp đầy đủ: điểm đi, điểm đến, ngày đi và ngày về.";
        }

        LocalDate departureDate;
        LocalDate returnDate;
        try {
            departureDate = LocalDate.parse(departureDateStr, DateTimeFormatter.ISO_DATE);
            returnDate = LocalDate.parse(returnDateStr, DateTimeFormatter.ISO_DATE);
        } catch (Exception e) {
            return "❌ Định dạng ngày không hợp lệ. Vui lòng dùng format: yyyy-MM-dd (VD: 2025-12-10)";
        }

        if (returnDate.isBefore(departureDate)) {
            return "❌ Ngày về phải sau ngày đi.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("🔄 **CHUYẾN KHỨ HỒI: %s ↔ %s**\n\n", startCity, endCity));

        // Tìm chuyến đi
        TripFilterRequestDTO outboundFilter = new TripFilterRequestDTO();
        outboundFilter.setStartCity(startCity);
        outboundFilter.setEndCity(endCity);
        outboundFilter.setDepartureDate(departureDate.atStartOfDay());
        outboundFilter.setSortBy("departureTime");
        outboundFilter.setSortDirection("ASC");

        FilterResponseDTO outboundResult = tripService.filterTrips(outboundFilter, 0, limit);

        sb.append(String.format("📤 **CHIỀU ĐI** (%s → %s, ngày %s):\n",
                startCity, endCity, departureDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        if (outboundResult == null || outboundResult.getData() == null || outboundResult.getData().isEmpty()) {
            sb.append("   ❌ Không tìm thấy chuyến xe phù hợp.\n\n");
        } else {
            sb.append(formatTripsForRoundTrip(outboundResult.getData(), "ĐI"));
        }

        // Tìm chuyến về (đảo điểm đi/đến)
        TripFilterRequestDTO returnFilter = new TripFilterRequestDTO();
        returnFilter.setStartCity(endCity);
        returnFilter.setEndCity(startCity);
        returnFilter.setDepartureDate(returnDate.atStartOfDay());
        returnFilter.setSortBy("departureTime");
        returnFilter.setSortDirection("ASC");

        FilterResponseDTO returnResult = tripService.filterTrips(returnFilter, 0, limit);

        sb.append(String.format("📥 **CHIỀU VỀ** (%s → %s, ngày %s):\n",
                endCity, startCity, returnDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        if (returnResult == null || returnResult.getData() == null || returnResult.getData().isEmpty()) {
            sb.append("   ❌ Không tìm thấy chuyến xe phù hợp.\n\n");
        } else {
            sb.append(formatTripsForRoundTrip(returnResult.getData(), "VỀ"));
        }

        sb.append("💡 Bạn muốn đặt chuyến đi và chuyến về nào? Cho mình biết ID chuyến nhé!");

        return sb.toString();
    }

    /**
     * Format trips cho round trip display
     */
    private String formatTripsForRoundTrip(List<TripFilterResponseDTO> trips, String direction) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (int i = 0; i < trips.size(); i++) {
            TripFilterResponseDTO trip = trips.get(i);
            sb.append(String.format("   %d. [%s-%d] %s - %s | %,.0f VNĐ | %d ghế trống\n",
                    i + 1,
                    direction,
                    trip.getTrip_id(),
                    trip.getOperator_name(),
                    trip.getDeparture_time().format(timeFormatter),
                    trip.getPrice_per_seat(),
                    trip.getAvailable_seats()));
        }
        sb.append("\n");

        return sb.toString();
    }

    // ======================= CARGO BOOKING FUNCTIONS =======================

    /**
     * Tính phí gửi hàng
     */
    private String calculateCargoFee(Map<String, Object> args) {
        Long tripId = args.get("tripId") != null ? ((Number) args.get("tripId")).longValue() : null;
        String cargoTypeStr = (String) args.get("cargoType");
        Double weight = args.get("weight") != null ? ((Number) args.get("weight")).doubleValue() : null;
        Boolean needsInsurance = (Boolean) args.getOrDefault("needsInsurance", false);

        if (tripId == null) {
            return "❌ Vui lòng cung cấp ID chuyến xe để tính phí gửi hàng.";
        }
        if (cargoTypeStr == null || weight == null) {
            return "❌ Vui lòng cung cấp loại hàng và cân nặng để tính phí.";
        }

        // Kiểm tra trip
        Optional<Trip> tripOpt = tripRepository.findById(tripId);
        if (tripOpt.isEmpty()) {
            return String.format("❌ Không tìm thấy chuyến xe với ID: %d", tripId);
        }

        Trip trip = tripOpt.get();

        // Parse cargo type
        CargoType cargoType;
        try {
            cargoType = CargoType.valueOf(cargoTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return "❌ Loại hàng không hợp lệ. Vui lòng chọn: DOCUMENT, PACKAGE, FRAGILE, ELECTRONICS, OTHER";
        }

        // Tính phí (dựa trên logic từ CargoType)
        BigDecimal baseFee = BigDecimal.valueOf(50000); // 50k base
        BigDecimal weightFee = BigDecimal.valueOf(weight * 10000); // 10k/kg
        BigDecimal multiplier = BigDecimal.valueOf(cargoType.getFeeMultiplier());
        BigDecimal cargoFee = baseFee.add(weightFee).multiply(multiplier);

        BigDecimal insuranceFee = BigDecimal.ZERO;
        if (needsInsurance) {
            insuranceFee = cargoFee.multiply(BigDecimal.valueOf(0.05)); // 5% phí bảo hiểm
        }

        BigDecimal totalFee = cargoFee.add(insuranceFee);

        StringBuilder sb = new StringBuilder();
        sb.append("📦 **BẢNG TÍNH PHÍ GỬI HÀNG**\n\n");
        sb.append(String.format("🚌 Chuyến xe ID: %d\n", tripId));
        if (trip.getRoute() != null) {
            sb.append(String.format("📍 Tuyến: %s → %s\n",
                    trip.getRoute().getStartLocation().getName(),
                    trip.getRoute().getEndLocation().getName()));
        }
        sb.append("\n");
        sb.append(String.format("📋 Loại hàng: %s (hệ số: x%.1f)\n", formatCargoType(cargoType),
                cargoType.getFeeMultiplier()));
        sb.append(String.format("⚖️ Cân nặng: %.1f kg\n", weight));
        sb.append("\n");
        sb.append(String.format("💰 Phí vận chuyển: %,.0f VNĐ\n", cargoFee));
        if (needsInsurance) {
            sb.append(String.format("🛡️ Phí bảo hiểm (5%%): %,.0f VNĐ\n", insuranceFee));
        }
        sb.append(String.format("💵 **TỔNG CỘNG: %,.0f VNĐ**\n\n", totalFee));

        sb.append("📝 Để tạo đơn gửi hàng, vui lòng truy cập:\n");
        sb.append("🔗 **Website**: busify.vn/cargo\n");
        sb.append("📱 Hoặc liên hệ hotline: **1900 xxxx**");

        return sb.toString();
    }

    /**
     * Tra cứu đơn gửi hàng
     */
    private String getCargoInfo(Map<String, Object> args) {
        String cargoCode = (String) args.get("cargoCode");

        if (cargoCode == null || cargoCode.isEmpty()) {
            return "❌ Vui lòng cung cấp mã vận đơn để tra cứu.";
        }

        try {
            CargoDetailResponseDTO cargo = cargoService.getCargoBookingByCode(cargoCode);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("📦 **THÔNG TIN ĐƠN GỬI HÀNG: %s**\n\n", cargoCode));

            sb.append(String.format("📊 Trạng thái: %s\n", formatCargoStatus(cargo.getStatus())));

            // Cargo info từ nested class
            if (cargo.getCargo() != null) {
                sb.append(String.format("📋 Loại hàng: %s\n", cargo.getCargo().getTypeDisplay()));
                sb.append(String.format("⚖️ Cân nặng: %.1f kg\n", cargo.getCargo().getWeight()));
                if (cargo.getCargo().getDescription() != null) {
                    sb.append(String.format("📝 Mô tả: %s\n", cargo.getCargo().getDescription()));
                }
            }
            sb.append("\n");

            // Sender/Receiver từ nested class
            if (cargo.getSender() != null) {
                sb.append(String.format("👤 Người gửi: %s - %s\n", cargo.getSender().getName(),
                        cargo.getSender().getPhone()));
            }
            if (cargo.getReceiver() != null) {
                sb.append(String.format("👥 Người nhận: %s - %s\n", cargo.getReceiver().getName(),
                        cargo.getReceiver().getPhone()));
            }
            sb.append("\n");

            // Trip và Location từ nested class
            if (cargo.getTrip() != null) {
                sb.append(String.format("📍 Tuyến: %s\n", cargo.getTrip().getRouteName()));
            }
            if (cargo.getPickup() != null) {
                sb.append(String.format("🏢 Điểm lấy: %s\n", cargo.getPickup().getLocationName()));
            }
            if (cargo.getDropoff() != null) {
                sb.append(String.format("🏁 Điểm giao: %s\n", cargo.getDropoff().getLocationName()));
            }
            sb.append("\n");

            // Payment từ nested class
            if (cargo.getPayment() != null) {
                sb.append(String.format("💵 Tổng tiền: %,.0f VNĐ\n", cargo.getPayment().getTotalAmount()));
            }
            sb.append(String.format("📅 Ngày tạo: %s\n", cargo.getCreatedAt().format(formatter)));

            // Trip arrival time
            if (cargo.getTrip() != null && cargo.getTrip().getArrivalTime() != null) {
                sb.append(String.format("🕐 Dự kiến đến: %s\n", cargo.getTrip().getArrivalTime().format(formatter)));
            }

            return sb.toString();

        } catch (Exception e) {
            log.error("Error getting cargo info: {}", e.getMessage(), e);
            return String.format("❌ Không tìm thấy đơn gửi hàng với mã: %s", cargoCode);
        }
    }

    /**
     * Hủy đơn gửi hàng
     */
    private String cancelCargo(Map<String, Object> args) {
        String cargoCode = (String) args.get("cargoCode");
        String reason = (String) args.getOrDefault("reason", "Khách hàng yêu cầu hủy qua AI Chat");

        if (cargoCode == null || cargoCode.isEmpty()) {
            return "❌ Vui lòng cung cấp mã vận đơn cần hủy.";
        }

        try {
            // Kiểm tra trạng thái trước
            CargoDetailResponseDTO cargo = cargoService.getCargoBookingByCode(cargoCode);

            if (cargo.getStatus() != CargoStatus.PENDING && cargo.getStatus() != CargoStatus.CONFIRMED) {
                return String.format("❌ Không thể hủy đơn hàng ở trạng thái: %s\n" +
                        "Chỉ có thể hủy khi đơn đang ở trạng thái PENDING hoặc CONFIRMED.",
                        formatCargoStatus(cargo.getStatus()));
            }

            CargoCancelRequestDTO request = new CargoCancelRequestDTO();
            request.setReason(reason);

            CargoBookingResponseDTO response = cargoService.cancelCargoBooking(cargoCode, request);

            StringBuilder sb = new StringBuilder();
            sb.append("✅ **HỦY ĐƠN GỬI HÀNG THÀNH CÔNG!**\n\n");
            sb.append(String.format("📦 Mã vận đơn: %s\n", cargoCode));
            sb.append(String.format("📊 Trạng thái: %s\n", formatCargoStatus(response.getStatus())));
            sb.append(String.format("📝 Lý do: %s\n\n", reason));

            sb.append("💰 Nếu bạn đã thanh toán, tiền sẽ được hoàn lại theo chính sách hoàn tiền.");

            return sb.toString();

        } catch (Exception e) {
            log.error("Error cancelling cargo: {}", e.getMessage(), e);
            return String.format("❌ Lỗi khi hủy đơn: %s", e.getMessage());
        }
    }

    /**
     * Format loại hàng hóa
     */
    private String formatCargoType(CargoType type) {
        return switch (type) {
            case DOCUMENT -> "📄 Tài liệu";
            case PACKAGE -> "📦 Hàng hóa thường";
            case FRAGILE -> "⚠️ Hàng dễ vỡ";
            case ELECTRONICS -> "📱 Thiết bị điện tử";
            case OTHER -> "📋 Khác";
        };
    }

    /**
     * Format trạng thái đơn gửi hàng
     */
    private String formatCargoStatus(CargoStatus status) {
        return switch (status) {
            case PENDING -> "⏳ Chờ xác nhận";
            case CONFIRMED -> "✅ Đã xác nhận";
            case PICKED_UP -> "📦 Đã lấy hàng";
            case IN_TRANSIT -> "🚌 Đang vận chuyển";
            case ARRIVED -> "🏁 Đã đến nơi";
            case DELIVERED -> "✔️ Đã giao hàng";
            case CANCELLED -> "❌ Đã hủy";
            case REJECTED -> "🚫 Bị từ chối";
            case RETURNED -> "↩️ Đã hoàn trả";
        };
    }
}
