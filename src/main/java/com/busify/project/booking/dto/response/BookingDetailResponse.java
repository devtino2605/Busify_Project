package com.busify.project.booking.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDetailResponse {
    private Long bookingId;
    private LocationInfo routeStart;
    private LocationInfo routeEnd;
    private String operatorName;
    private Instant departureTime;
    private Instant arrivalEstimateTime;
    private BusInfo bus;
    private List<TicketInfo> tickets;
    private String status;
    private PaymentInfo paymentInfo;

    @Data
    public static class LocationInfo {
        private String name;
        private String address;
        private String city;
    }

    @Data
    public static class BusInfo {
        private String model;
        private String licensePlate;
    }

    @Data
    public static class TicketInfo {
        private String passengerName;
        private String phone;
        private String email;
        private String seatNumber;
        private String ticketCode;
    }

    @Data
    public static class PaymentInfo {
        private BigDecimal totalAmount;
        private List<PaymentDetail> payments;
        private BigDecimal remainingAmount;
        private Instant paymentDeadline;

        @Data
        public static class PaymentDetail {
            private BigDecimal amount;
            private String method;
            private Instant timestamp;
        }
    }
}
