package com.busify.project.ticket.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
public class ValidateBookingTripRequestDTO {
    
    @NotNull(message = "Trip ID không được để trống")
    private Long tripId;
    
    @NotBlank(message = "Booking code không được để trống")
    private String bookingCode;
}
