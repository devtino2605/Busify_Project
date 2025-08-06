package com.busify.project.booking.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingAddRequestDTO {

    @NotNull
    private Long tripId;

    private Long customerId;

    private String guestFullName;
    private String guestEmail;
    private String guestPhone;
    private String guestAddress;

    @NotNull
    @Size(min = 3)
    private String seatNumber;

    @NotNull
    @PositiveOrZero
    private BigDecimal totalAmount;
}
