package com.busify.project.score.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreMinusRequestDTO {
    @NotNull(message = "Booking ID không được null")
    @Positive(message = "Booking ID phải là số dương")
    private Long bookingId;
    
    @NotNull(message = "Số điểm sử dụng không được null")
    @Min(value = 1, message = "Số điểm sử dụng phải ít nhất 1")
    private Integer pointsToUse;
}
