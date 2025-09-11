package com.busify.project.refund.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDTO {

    @NotNull(message = "Payment ID is required")
    private Long paymentId;

    @NotBlank(message = "Refund reason is required")
    private String refundReason;

    private String notes;
}
