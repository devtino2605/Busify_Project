package com.busify.project.complaint.dto.response;

import com.busify.project.complaint.enums.ComplaintStatus;
import com.busify.project.booking.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ComplaintResponseDetailDTO extends ComplaintResponseDTO {
    private Long id;
    private String title;
    private String description;
    private ComplaintStatus status;
    private String createdAt;
    private String updatedAt;

    // Customer information
    private CustomerInfo customer;

    // Booking information
    private BookingInfo booking;

    // Assigned agent information
    private AgentInfo assignedAgent;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomerInfo {
        private Long customerId;
        private String customerName;
        private String customerEmail;
        private String customerPhone;
        private String customerAddress;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookingInfo {
        private Long bookingId;
        private String bookingCode;
        private BookingStatus bookingStatus;
        private BigDecimal totalAmount;
        private String seatNumber;
        private Instant bookingDate;
        private String routeName;
        private String startLocation;
        private String endLocation;
        private Instant departureTime;
        private Instant arrivalTime;
        private String operatorName;
        private String busLicensePlate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AgentInfo {
        private Long agentId;
        private String agentName;
        private String agentEmail;
    }
}
