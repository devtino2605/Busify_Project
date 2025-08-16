package com.busify.project.booking.dto.response;

import java.time.Instant;

import com.busify.project.booking.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetailResponseDTO {
    private Long bookingId;
    private Instant departureTime;
    private Instant arrivalTime;
    private String departureName;
    private String arrivalName;
    private String bookingCode;
    private BookingStatus status;
    private Instant bookingDate;
}
