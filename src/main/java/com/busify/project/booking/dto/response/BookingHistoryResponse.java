package com.busify.project.booking.dto.response;

import com.busify.project.booking.enums.BookingStatus;
import com.busify.project.booking.enums.SellingMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class BookingHistoryResponse {
    private Long trip_id;
    private Long booking_id;
    private String route_name;
    private Instant departure_time;
    private Instant arrival_time;
    private String departure_name;
    private String arrival_name;
    private String booking_code;
    private BookingStatus status;
    private BigDecimal total_amount;
    private Instant booking_date;
    private Integer ticket_count;
    private String payment_method;
    private SellingMethod selling_method; // Added selling_method field
}
