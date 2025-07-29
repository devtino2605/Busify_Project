package com.busify.project.review.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.common.dto.response.ApiResponse;
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
    public ApiResponse<ReviewResponseDTO> getReviewById(@PathVariable Long id) {
        return ApiResponse.success("Lấy đánh giá theo ID thành công", reviewService.getReview(id));
    }

    @GetMapping("/trip/{tripId}")
    public ApiResponse<ReviewResponseListDTO> getAllReviewsByTrip(@PathVariable Long tripId) {
        return ApiResponse.success("Lấy danh sách đánh giá theo chuyến đi thành công",
                reviewService.getAllReviewsByTrip(tripId));
    }

    @PostMapping("/trip")
    public ApiResponse<ReviewResponseDTO> addReview(@RequestBody ReviewAddDTO reviewAddDTO) {
        return ApiResponse.success("Thêm đánh giá thành công", reviewService.addReview(reviewAddDTO));
    }

    @GetMapping("/customer/{customerId}")
    public ApiResponse<ReviewResponseListDTO> getAllReviewsByCustomer(@PathVariable Long customerId) {
        return ApiResponse.success("Lấy danh sách đánh giá thành công",
                reviewService.getAllReviewsByCustomer(customerId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ReviewResponseDTO> deleteReview(@PathVariable Long id) {
        return ApiResponse.success("Xóa đánh giá thành công", reviewService.deleteReview(id));
    }

    @PatchMapping("/{id}")
    public ApiResponse<ReviewResponseDTO> updateReview(@PathVariable Long id, @RequestBody ReviewAddDTO reviewAddDTO) {
        return ApiResponse.success("Cập nhật đánh giá thành công", reviewService.updateReview(id, reviewAddDTO));
    }
}
