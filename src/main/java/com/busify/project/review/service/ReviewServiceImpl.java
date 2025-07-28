package com.busify.project.review.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.busify.project.review.dto.ReviewAddDTO;
import com.busify.project.review.dto.response.ReviewResponseAddDTO;
import com.busify.project.review.dto.response.ReviewResponseDTO;
import com.busify.project.review.dto.response.ReviewResponseGetDTO;
import com.busify.project.review.dto.response.ReviewResponseListDTO;
import com.busify.project.review.entity.Review;
import com.busify.project.review.repository.ReviewRepository;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.repository.UserRepository;

@Service
public class ReviewServiceImpl extends ReviewService {

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository,
            TripRepository tripRepository) {
        super(reviewRepository, userRepository, tripRepository);
    }

    /**
     * Retrieves all reviews.
     *
     * @return a list of all reviews
     */
    public ReviewResponseListDTO getAllReviewsByTrip(Long tripId) {
        return new ReviewResponseListDTO(
                reviewRepository.findByTripId(tripId).stream()
                        .map(review -> new ReviewResponseGetDTO(
                                review.getReviewId(),
                                review.getTrip().getId(),
                                review.getCustomer().getId(),
                                review.getRating(),
                                review.getComment(),
                                review.getCreatedAt().toString()))
                        .collect(Collectors.toList()));
    }

    /**
     * Retrieves all reviews by customer ID.
     *
     * @param customerId the ID of the customer
     * @return a list of reviews for the specified customer
     */
    public ReviewResponseListDTO getAllReviewsByCustomer(Long customerId) {
        return new ReviewResponseListDTO(
                reviewRepository.findByCustomerId(customerId).stream()
                        .map(review -> new ReviewResponseGetDTO(
                                review.getReviewId(),
                                review.getTrip().getId(),
                                review.getCustomer().getId(),
                                review.getRating(),
                                review.getComment(),
                                review.getCreatedAt().toString()))
                        .collect(Collectors.toList()));
    }

    /**
     * Adds a new review based on the provided ReviewAddDTO.
     *
     * @param reviewAddDTO the DTO containing review details
     */
    public ReviewResponseDTO addReview(ReviewAddDTO reviewAddDTO) {
        return toResponseAddDTO(reviewRepository.save(toEntity(reviewAddDTO)));
    }

    public ReviewResponseGetDTO getReview(Long id) {
        return reviewRepository.findById(id)
                .map(review -> new ReviewResponseGetDTO(
                        review.getReviewId(),
                        review.getTrip().getId(),
                        review.getCustomer().getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getCreatedAt().toString()))
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + id));
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id the ID of the review to delete
     */
    public ReviewResponseAddDTO deleteReview(Long id) {
        reviewRepository.deleteById(id);
        return new ReviewResponseAddDTO("Review deleted successfully");
    }

    /**
     * Updates an existing review.
     *
     * @param id           the ID of the review to update
     * @param reviewAddDTO the DTO containing updated review details
     * @return the updated ReviewResponseDTO
     */
    public ReviewResponseDTO updateReview(Long id, ReviewAddDTO reviewAddDTO) {
        final Review review = new Review();
        review.setReviewId(id);
        review.setComment(reviewAddDTO.getComment());
        review.setRating(reviewAddDTO.getRating());
        reviewRepository.save(review);
        return new ReviewResponseAddDTO("Review updated successfully");
    }

    @Override
    public Review toEntity(ReviewAddDTO reviewAddDTO) {
        final Review review = new Review();
        review.setComment(reviewAddDTO.getComment());
        review.setRating(reviewAddDTO.getRating());
        review.setCustomer(userRepository.findById(reviewAddDTO.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer ID")));
        review.setTrip(tripRepository.findById(reviewAddDTO.getTripId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid trip ID")));
        return review;
    }

    /**
     * Converts a Review entity to a ReviewResponseAddDTO.
     *
     * @param review the Review entity to convert
     * @return a ReviewResponseAddDTO containing the success message
     */
    public ReviewResponseDTO toResponseAddDTO(Review review) {
        return new ReviewResponseAddDTO("Review created successfully");
    }

}
