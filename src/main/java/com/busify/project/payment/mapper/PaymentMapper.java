package com.busify.project.payment.mapper;

import com.busify.project.payment.dto.response.PaymentResponseDTO;
import com.busify.project.payment.entity.Payment;

public class PaymentMapper {

    public static PaymentResponseDTO toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .status(payment.getStatus())
                .paymentUrl(null)
                .bookingId(payment.getBooking() != null ? payment.getBooking().getId() : null)
                .cargoBookingId(
                        payment.getCargoBooking() != null ? payment.getCargoBooking().getCargoBookingId() : null)
                .build();
    }
}
