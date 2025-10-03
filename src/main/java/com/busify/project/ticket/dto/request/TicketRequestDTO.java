package com.busify.project.ticket.dto.request;

import com.busify.project.ticket.enums.SellMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class TicketRequestDTO {
    @NotNull(message = "Booking ID không được để trống")
    @Positive(message = "Booking ID phải là số dương")
    private Long bookingId;
    
    @NotNull(message = "Phương thức bán vé không được để trống")
    private SellMethod sellMethod;
}
