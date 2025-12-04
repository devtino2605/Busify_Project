package com.busify.project.trip.dto.response;

import com.busify.project.trip.enums.TripCancellationReason;
import com.busify.project.trip.enums.TripStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for trip cancellation/delay operations
 * Contains detailed information about affected bookings, cargo, and refunds
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response for trip cancellation/delay operation")
public class TripCancellationResponseDTO {

    @Schema(description = "Whether the operation was successful")
    private boolean success;

    @Schema(description = "Response message")
    private String message;

    // Trip Information
    @Schema(description = "Trip ID")
    private Long tripId;

    @Schema(description = "Previous trip status")
    private TripStatus previousStatus;

    @Schema(description = "New trip status")
    private TripStatus newStatus;

    @Schema(description = "Cancellation/delay reason category")
    private TripCancellationReason cancellationReason;

    @Schema(description = "Detailed reason description")
    private String reasonDetails;

    @Schema(description = "New departure time (for delayed trips)")
    private LocalDateTime newDepartureTime;

    // Route Information
    @Schema(description = "Route information (e.g., 'Hà Nội → Đà Nẵng')")
    private String routeInfo;

    @Schema(description = "Original departure time")
    private LocalDateTime originalDepartureTime;

    @Schema(description = "Operator/Bus company name")
    private String operatorName;

    // Affected Bookings Summary
    @Schema(description = "Number of bookings affected")
    private int affectedBookingsCount;

    @Schema(description = "Number of tickets cancelled")
    private int cancelledTicketsCount;

    @Schema(description = "Total passengers affected")
    private int affectedPassengersCount;

    // Affected Cargo Summary
    @Schema(description = "Number of cargo bookings affected")
    private int affectedCargoCount;

    @Schema(description = "Total cargo weight affected (kg)")
    private Double affectedCargoWeight;

    // Refund Summary
    @Schema(description = "Whether auto-refund was processed")
    private boolean refundProcessed;

    @Schema(description = "Refund percentage applied")
    private Integer refundPercentage;

    @Schema(description = "Total booking refund amount")
    private BigDecimal totalBookingRefundAmount;

    @Schema(description = "Total cargo refund amount")
    private BigDecimal totalCargoRefundAmount;

    @Schema(description = "Total refund amount (bookings + cargo)")
    private BigDecimal totalRefundAmount;

    // Notification Summary
    @Schema(description = "Whether notifications were sent")
    private boolean notificationsSent;

    @Schema(description = "Number of email notifications sent")
    private int emailsSentCount;

    @Schema(description = "List of customer emails notified")
    private List<String> notifiedEmails;

    // Detailed Lists (optional, for admin view)
    @Schema(description = "List of affected booking codes")
    private List<String> affectedBookingCodes;

    @Schema(description = "List of affected cargo codes")
    private List<String> affectedCargoCodes;

    // Timestamps
    @Schema(description = "When the cancellation/delay was processed")
    private LocalDateTime processedAt;

    @Schema(description = "Who processed the cancellation/delay")
    private String processedBy;

    /**
     * Builder helper for error response
     */
    public static TripCancellationResponseDTO error(String message) {
        return TripCancellationResponseDTO.builder()
                .success(false)
                .message(message)
                .processedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Builder helper for success response with minimal info
     */
    public static TripCancellationResponseDTOBuilder successBuilder(Long tripId, TripStatus newStatus) {
        return TripCancellationResponseDTO.builder()
                .success(true)
                .tripId(tripId)
                .newStatus(newStatus)
                .processedAt(LocalDateTime.now());
    }
}
