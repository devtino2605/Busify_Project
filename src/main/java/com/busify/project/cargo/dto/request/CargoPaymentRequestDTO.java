package com.busify.project.cargo.dto.request;

import com.busify.project.payment.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CargoPaymentRequestDTO
 * 
 * Request DTO for initiating cargo payment
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CargoPaymentRequestDTO {

    @NotNull(message = "Cargo booking ID không được null")
    @Positive(message = "Cargo booking ID phải là số dương")
    private Long cargoBookingId;

    @NotNull(message = "Phương thức thanh toán không được null")
    private PaymentMethod paymentMethod;
}
