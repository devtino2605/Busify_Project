package com.busify.project.trip_seat.dto;

import com.busify.project.trip_seat.enums.TripSeatStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatStatus {
    @NotBlank(message = "Số ghế không được để trống")
    @Pattern(regexp = "^[A-Z][0-9]{1,2}$", message = "Số ghế phải có định dạng như A1, B12")
    private String seatNumber;
    
    @NotNull(message = "Trạng thái ghế không được null")
    private TripSeatStatus status;
}