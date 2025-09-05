package com.busify.project.score.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreMinusRequestDTO {
    private Long bookingId;
    private Integer pointsToUse;
}
