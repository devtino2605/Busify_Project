package com.busify.project.score.mapper;


import com.busify.project.score.dto.response.ScoreAddResponseDTO;
import com.busify.project.score.entity.Score;

public class ScoreMapper {

    public static ScoreAddResponseDTO toDTO(Score score) {
        if (score == null) return null;

        return ScoreAddResponseDTO.builder()
                .scoreId(score.getScoreId())
                .userId(score.getUser() != null ? score.getUser().getId() : null)
                .points(score.getPoints())
                .createdAt(score.getCreatedAt())
                .updatedAt(score.getUpdatedAt())
                .build();
    }
}
