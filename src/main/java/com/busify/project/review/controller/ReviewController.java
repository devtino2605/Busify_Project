package com.busify.project.review.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.review.dto.ReviewAddDTO;
import com.busify.project.review.dto.response.ReviewResponseDTO;
import com.busify.project.review.dto.response.ReviewResponseListDTO;
import com.busify.project.review.service.ReviewServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    @GetMapping("/{id}")
    public ReviewResponseDTO getReviewById(@PathVariable Long id) {
        return reviewService.getReview(id);
    }

    @GetMapping("/trip/{tripId}")
    public ReviewResponseListDTO getAllReviewsByTrip(@PathVariable Long tripId) {
        return reviewService.getAllReviewsByTrip(tripId);
    }

    @PostMapping("/trip")
    public ReviewResponseDTO addReview(@RequestBody ReviewAddDTO reviewAddDTO) {
        return reviewService.addReview(reviewAddDTO);
    }

    @GetMapping("/customer/{customerId}")
    public ReviewResponseListDTO getAllReviewsByCustomer(@PathVariable Long customerId) {
        return reviewService.getAllReviewsByCustomer(customerId);
    }

    @DeleteMapping("/{id}")
    public ReviewResponseDTO deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }

    @PatchMapping("/{id}")
    public ReviewResponseDTO updateReview(@PathVariable Long id, @RequestBody ReviewAddDTO reviewAddDTO) {
        return reviewService.updateReview(id, reviewAddDTO);
    }
}
