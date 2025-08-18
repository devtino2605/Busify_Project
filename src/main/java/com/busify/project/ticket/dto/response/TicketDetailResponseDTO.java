package com.busify.project.ticket.dto.response;

import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.ticket.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailResponseDTO {
    // Ticket information
    private String ticketCode;
    private String passengerName;
    private String passengerPhone;
    private String seatNumber;
    private BigDecimal price;
    private TicketStatus status;

    // Booking information
    private BookingInfo booking;

    // Trip information
    private TripInfo trip;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingInfo {
        private Long bookingId;
        private String bookingCode;
        private BookingStatus status;
        private BigDecimal totalAmount;
        private Instant bookingDate;
        private String customerEmail;
        private String customerPhone;
        private String customerAddress;
        private PaymentMethod paymentMethod;
        private Instant paidAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TripInfo {
        private Long tripId;
        private Instant departureTime;
        private Instant arrivalTime;
        private BigDecimal pricePerSeat;
        private RouteInfo route;
        private BusInfo bus;
        private OperatorInfo operator;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteInfo {
        private Long routeId;
        private String routeName;
        private LocationInfo startLocation;
        private LocationInfo endLocation;
        private Integer durationMinutes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationInfo {
        private String name;
        private String address;
        private String city;
        private Double latitude;
        private Double longitude;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusInfo {
        private Long busId;
        private String model;
        private String licensePlate;
        private Integer totalSeats;
        private Object amenities;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperatorInfo {
        private Long operatorId;
        private String operatorName;
        private String hotline;
    }
}
