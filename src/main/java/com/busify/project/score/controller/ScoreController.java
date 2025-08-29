package com.busify.project.score.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.score.dto.response.ScoreAddResponseDTO;
import com.busify.project.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping("/{tripId}")
    public ApiResponse<List<ScoreAddResponseDTO>> addPointsByTrip(@PathVariable Long tripId) {
        List<ScoreAddResponseDTO> updatedScores = scoreService.addPointsByTripId(tripId);
        return ApiResponse.success("Điểm đã được cộng dồn cho khách hàng trong trip " + tripId, updatedScores);
    }
}


