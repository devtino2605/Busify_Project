package com.busify.project.score.controller;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.score.dto.request.ScoreMinusRequestDTO;
import com.busify.project.score.dto.response.ScoreAddResponseDTO;
import com.busify.project.score.dto.response.ScoreResponseDTO;
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

    @GetMapping()
    public ApiResponse<ScoreResponseDTO> getScoreByUserId() {
        ScoreResponseDTO score = scoreService.getScoreByUserId();
        return ApiResponse.success("Lấy điểm thành công cho user ", score);
    }

    @PostMapping("/use")
    public ApiResponse<ScoreResponseDTO> usePoints(@RequestBody ScoreMinusRequestDTO request) {
        ScoreResponseDTO updatedScore = scoreService.usePoints(request);
        return ApiResponse.success("Sử dụng điểm thành công", updatedScore);
    }
}


