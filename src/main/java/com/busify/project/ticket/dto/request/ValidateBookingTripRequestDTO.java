package com.busify.project.ticket.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
public class ValidateBookingTripRequestDTO {
    
    @NotNull(message = "Trip ID không được để trống")
    @Positive(message = "Trip ID phải là số dương")
    private Long tripId;
    
    @NotBlank(message = "Booking code không được để trống")
    @Size(min = 6, max = 20, message = "Booking code phải từ 6-20 ký tự")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Booking code chỉ được chứa chữ hoa và số")
    private String bookingCode;
}
