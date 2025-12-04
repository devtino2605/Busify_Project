package com.busify.project.payment.dto.response;

import com.busify.project.payment.enums.PaymentStatus;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Long paymentId;

    private PaymentStatus status;

    private String paymentUrl;

    private Long bookingId;

    private Long cargoBookingId;
}
