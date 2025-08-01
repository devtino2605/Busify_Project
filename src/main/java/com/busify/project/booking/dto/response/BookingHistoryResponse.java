package com.busify.project.booking.dto.response;

import com.busify.project.booking.enums.BookingStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class BookingHistoryResponse {
    private String route_name;
    private Instant departureTime;
    private String booking_code;
    private BookingStatus status;
}
