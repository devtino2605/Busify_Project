package com.busify.project.review.controllers;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.review.dto.ReviewResponseDTO;
import com.busify.project.review.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews/trip/{tripId}")
   public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getReviewsByTripId(@PathVariable Long tripId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByTripId(tripId);
        ApiResponse<List<ReviewResponseDTO>> apiResponse = new ApiResponse<>(200, "Reviews fetched successfully", reviews);
        return ResponseEntity.ok(apiResponse);
    }
}