package com.busify.project.booking.dto.response;

import java.math.BigDecimal;

import com.busify.project.booking.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingAddResponseDTO {
    private String seatNumber;
    private BigDecimal totalAmount;
    private BookingStatus status;
}
