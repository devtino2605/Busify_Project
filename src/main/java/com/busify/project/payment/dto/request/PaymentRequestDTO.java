package com.busify.project.payment.dto.request;

import com.busify.project.payment.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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
    @NotNull(message = "Booking ID không được null")
    @Positive(message = "Booking ID phải là số dương")
    private Long bookingId;
    
    @NotNull(message = "Phương thức thanh toán không được null")
    private PaymentMethod paymentMethod;

    public HttpServletRequest getHttpServletRequest() {
        throw new UnsupportedOperationException("Unimplemented method 'getHttpServletRequest'");
    }
}
