package com.busify.project.booking.dto.response;

import com.busify.project.payment.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class BookingDetailResponse {
    private Long booking_id;
    private String passenger_name;
    private String phone;
    private String email;
    private String address; // Add this field
    // Add guest-specific fields
    private String guestFullName;
    private String guestEmail;
    private String guestPhone;
    private String guestAddress;
    private LocationInfo route_start;
    private LocationInfo route_end;
    private String operator_name;
    private Instant departure_time;
    private Instant arrival_estimate_time;
    private BusInfo bus;
    private List<TicketInfo> tickets;
    private String status;
    private PaymentInfo payment_info;

    @Data
    public static class LocationInfo {
        private String name;
        private String address;
        private String city;
    }

    @Data
    public static class BusInfo {
        private String model;
        private String license_plate;
    }

    @Data
    public static class TicketInfo {
        private String seat_number;
        private String ticket_code;
    }

    @Data
    public static class PaymentInfo {
        private BigDecimal amount;
        private PaymentMethod method;
        private Instant timestamp;
    }
}
