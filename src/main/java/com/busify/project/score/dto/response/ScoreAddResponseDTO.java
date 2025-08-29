package com.busify.project.score.dto.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreAddResponseDTO {
    private Long scoreId;
    private Long userId;
    private Integer points;
    private Instant createdAt;
    private Instant updatedAt;
}
