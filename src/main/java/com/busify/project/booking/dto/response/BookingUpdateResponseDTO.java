package com.busify.project.booking.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.busify.project.booking.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateResponseDTO {
    private Long id;
    private String bookingCode;
    private String guestFullName;
    private String guestEmail;
    private String guestPhone;
    private String seatNumber;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private Instant updatedAt;
    private Long tripId;
}
