package com.busify.project.score.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreResponseDTO {
    private Long scoreId;
    private Long userId;
    private Integer points;
}
