package com.busify.project.trip.dto.request;

import com.busify.project.trip.enums.TripStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TripUpdateStatusRequest {
    @NotNull(message = "Trạng thái không được để trống")
    private TripStatus status;
    
    private String reason; // Lý do thay đổi trạng thái (optional)
}
