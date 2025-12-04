package com.busify.project.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.busify.project.booking.dto.response.BookingDetailResponseDTO;
import com.busify.project.cargo.dto.response.CargoDetailResponseDTO;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetailResponseDTO {
    private Long paymentId;

    private BigDecimal amount;

    private String transactionCode;

    private PaymentMethod paymentMethod;

    // One of these will be populated based on payment type
    private BookingDetailResponseDTO bookingDetails; // For ticket booking payment
    private CargoDetailResponseDTO cargoBookingDetails; // For cargo booking payment

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private PaymentStatus status;

    private LocalDateTime paidAt;
}
