package com.busify.project.review.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.review.dto.ReviewAddDTO;
import com.busify.project.review.dto.response.ReviewResponseDTO;
import com.busify.project.review.dto.response.ReviewResponseListDTO;
import com.busify.project.review.service.ReviewServiceImpl;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    @GetMapping()
    public ApiResponse<ReviewResponseListDTO> getAllReviews() {
        try {
            return ApiResponse.success("Lấy danh sách đánh giá thành công", reviewService.getAllReviews());
        } catch (Exception e) {
            return ApiResponse.error(500, "Đã xảy ra lỗi khi lấy danh sách đánh giá: " + e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ApiResponse<ReviewResponseListDTO> filterReviews(
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        // Ưu tiên lọc theo khoảng rating nếu có min và max
        if (minRating != null && maxRating != null) {
            return ApiResponse.success("Lọc đánh giá theo khoảng rating thành công",
                    reviewService.findByRatingBetween(minRating, maxRating));
        }
        // Nếu có rating và khoảng thời gian
        if (rating != null && startDate != null && endDate != null) {
            return ApiResponse.success("Lọc đánh giá theo rating và khoảng thời gian thành công",
                    reviewService.findByRatingAndCreatedAtBetween(rating, startDate, endDate));
        }
        // Nếu chỉ có rating
        if (rating != null) {
            return ApiResponse.success("Lọc đánh giá theo rating thành công",
                    reviewService.findByRating(rating));
        }
        // Nếu chỉ có khoảng thời gian
        if (startDate != null && endDate != null) {
            return ApiResponse.success("Lọc đánh giá theo khoảng thời gian thành công",
                    reviewService.findByCreatedAtBetween(startDate, endDate));
        }
        // Nếu không có điều kiện lọc nào
        return ApiResponse.success("Không có điều kiện lọc, trả về tất cả đánh giá",
                reviewService.getAllReviews());
    }

    @GetMapping("/search")
    public ApiResponse<ReviewResponseListDTO> searchReviews(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String comment) {

        // Tìm kiếm kết hợp
        if (customerName != null && comment != null) {
            return ApiResponse.success("Tìm kiếm đánh giá theo tên khách hàng và nội dung thành công",
                    reviewService.findByCustomerFullNameAndCommentContaining(customerName, comment));
        }

        // Tìm theo comment
        if (comment != null) {
            return ApiResponse.success("Tìm kiếm đánh giá theo nội dung thành công",
                    reviewService.findByCommentContainingIgnoreCase(comment));
        }

        // Tìm theo tên khách hàng
        if (customerName != null) {
            return ApiResponse.success("Tìm kiếm đánh giá theo khách hàng thành công",
                    reviewService.findByCustomerFullName(customerName));
        }

        return ApiResponse.error(400, "Vui lòng cung cấp ít nhất một điều kiện tìm kiếm");
    }

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

    @GetMapping("/bus-operator/{busOperatorId}")
    public ApiResponse<ReviewResponseListDTO> getReviewsByBusOperatorId(@PathVariable Long busOperatorId) {
        return ApiResponse.success("Lấy danh sách đánh giá theo nhà điều hành xe thành công",
                reviewService.getReviewsByBusOperatorId(busOperatorId));
    }
}
