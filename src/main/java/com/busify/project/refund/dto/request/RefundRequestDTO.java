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

    /**
     * Flag to bypass refund policy validation
     * Used for special cases like cargo rejection by staff (100% refund regardless
     * of time)
     */
    private boolean bypassPolicy = false;

    // Constructor without bypassPolicy for backward compatibility
    public RefundRequestDTO(Long paymentId, String refundReason, String notes) {
        this.paymentId = paymentId;
        this.refundReason = refundReason;
        this.notes = notes;
        this.bypassPolicy = false;
    }
}
