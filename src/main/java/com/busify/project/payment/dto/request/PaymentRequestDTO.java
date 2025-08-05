package com.busify.project.payment.dto.request;

import com.busify.project.payment.enums.PaymentMethod;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequestDTO {
    private Long bookingId;
    private PaymentMethod paymentMethod;

    public HttpServletRequest getHttpServletRequest() {
        throw new UnsupportedOperationException("Unimplemented method 'getHttpServletRequest'");
    }
}
