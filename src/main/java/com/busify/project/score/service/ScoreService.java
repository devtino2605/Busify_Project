package com.busify.project.score.service;

import com.busify.project.score.dto.request.ScoreMinusRequestDTO;
import com.busify.project.score.dto.response.ScoreAddResponseDTO;
import com.busify.project.score.dto.response.ScoreResponseDTO;

import java.util.List;

public interface ScoreService {
    List<ScoreAddResponseDTO> addPointsByTripId(Long tripId);
    ScoreResponseDTO getScoreByUserId();
    ScoreResponseDTO usePoints(ScoreMinusRequestDTO request);

}
