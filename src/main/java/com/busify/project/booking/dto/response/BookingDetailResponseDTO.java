package com.busify.project.booking.dto.response;

import java.time.LocalDateTime;

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
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String departureName;
    private String arrivalName;
    private String bookingCode;
    private BookingStatus status;
    private LocalDateTime bookingDate;
}
