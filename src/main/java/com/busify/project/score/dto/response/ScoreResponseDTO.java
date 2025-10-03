package com.busify.project.score.dto.response;

import java.io.Serializable;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreResponseDTO implements Serializable {
    private Long scoreId;
    private Long userId;
    private Integer points;
}
