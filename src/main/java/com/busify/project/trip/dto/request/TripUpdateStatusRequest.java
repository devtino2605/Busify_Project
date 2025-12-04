package com.busify.project.trip.dto.request;

import com.busify.project.trip.enums.TripCancellationReason;
import com.busify.project.trip.enums.TripStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Request DTO for updating trip status")
public class TripUpdateStatusRequest {

    @NotNull(message = "Trạng thái không được để trống")
    @Schema(description = "New status for the trip", example = "cancelled", required = true)
    private TripStatus status;

    @Schema(description = "Cancellation/delay reason category (REQUIRED when status is 'cancelled' or 'delayed')", example = "WEATHER")
    private TripCancellationReason cancellationReason;

    @Size(max = 500, message = "Chi tiết lý do không được quá 500 ký tự")
    @Schema(description = "Additional details about the reason (optional but recommended)", example = "Do bão số 5 đổ bộ vào miền Trung, đường đi bị ngập nặng")
    private String reasonDetails;

    @Schema(description = "New estimated departure time (for delayed trips only)", example = "2025-12-05T08:00:00")
    private LocalDateTime newDepartureTime;

    @Schema(description = "Whether to automatically process refunds for cancelled trips", example = "true", defaultValue = "true")
    private Boolean autoRefund = true;

    @Schema(description = "Whether to send notification emails to customers", example = "true", defaultValue = "true")
    private Boolean sendNotification = true;

    // Legacy field for backward compatibility
    @Deprecated
    @Schema(hidden = true)
    private String reason;

    /**
     * Get the display reason combining category and details
     */
    public String getDisplayReason() {
        if (cancellationReason != null) {
            String baseReason = cancellationReason.getVietnameseDescription();
            if (reasonDetails != null && !reasonDetails.isBlank()) {
                return baseReason + ": " + reasonDetails;
            }
            return baseReason;
        }
        // Fallback to legacy reason field
        return reason;
    }
}
