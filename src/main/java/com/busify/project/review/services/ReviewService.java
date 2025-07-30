package com.busify.project.review.services;

import com.busify.project.review.dto.ReviewResponseDTO;
import com.busify.project.review.entity.Review;
import com.busify.project.user.entity.User;
import com.busify.project.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

   public List<ReviewResponseDTO> getReviewsByTripId(Long tripId) {
        List<Review> reviews = reviewRepository.findByTripId(tripId);
        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found for trip ID: " + tripId);
        }
        return reviews.stream().map(review -> {
            User user = review.getCustomer();
            if (user == null) {
                throw new RuntimeException("User not found for review ID: " + review.getReviewId());
            }
            return new ReviewResponseDTO(
                    user.getFullName(),
                    review.getRating(),
                    review.getComment(),
                    review.getCreatedAt()
            );
        }).collect(Collectors.toList());
    }
}