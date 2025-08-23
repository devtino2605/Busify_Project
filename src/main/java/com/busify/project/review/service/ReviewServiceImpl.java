package com.busify.project.review.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.busify.project.review.dto.ReviewAddDTO;
import com.busify.project.review.dto.response.ReviewResponseAddDTO;
import com.busify.project.review.dto.response.ReviewResponseDTO;
import com.busify.project.review.dto.response.ReviewResponseGetDTO;
import com.busify.project.review.dto.response.ReviewResponseListDTO;
import com.busify.project.review.entity.Review;
import com.busify.project.review.mapper.ReviewDTOMapper;
import com.busify.project.review.repository.ReviewRepository;
import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.entity.User;
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
                                                .map(ReviewDTOMapper::toResponseGetDTO)
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
                                                .map(ReviewDTOMapper::toResponseGetDTO)
                                                .collect(Collectors.toList()));
        }

        /**
         * Adds a new review based on the provided ReviewAddDTO.
         *
         * @param reviewAddDTO the DTO containing review details
         */
        public ReviewResponseDTO addReview(ReviewAddDTO reviewAddDTO) {
                final User user = userRepository.findById(reviewAddDTO.getCustomerId())
                                .orElseThrow(
                                                () -> new IllegalArgumentException("User not found with ID: "
                                                                + reviewAddDTO.getCustomerId()));
                final Trip trip = tripRepository.findById(reviewAddDTO.getTripId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Trip not found with ID: " + reviewAddDTO.getTripId()));
                return toResponseAddDTO(reviewRepository.save(ReviewDTOMapper.toEntity(reviewAddDTO, user, trip)));
        }

        public ReviewResponseGetDTO getReview(Long id) {
                return reviewRepository.findById(id)
                                .map(ReviewDTOMapper::toResponseGetDTO)
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
                final Review review = reviewRepository.getReferenceById(id);
                review.setComment(reviewAddDTO.getComment());
                review.setRating(reviewAddDTO.getRating());
                reviewRepository.save(review);
                return new ReviewResponseAddDTO("Review updated successfully");
        }

        public ReviewResponseListDTO getReviewsByBusOperatorId(Long busOperatorId) {
                return new ReviewResponseListDTO(
                                reviewRepository.findByBusOperatorReviews(busOperatorId, 5).stream()
                                                .map(ReviewDTOMapper::toResponseGetDTO)
                                                .collect(Collectors.toList()));
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

        public ReviewResponseListDTO getAllReviews() {
                try {
                        System.err.println("Fetching all reviews");
                        return new ReviewResponseListDTO(
                                        reviewRepository.findAll().stream()
                                                        .map(ReviewDTOMapper::toResponseGetDTO)
                                                        .collect(Collectors.toList()));
                } catch (Exception e) {
                        // You can customize the error handling as needed
                        return new ReviewResponseListDTO(Collections.emptyList());
                }
        }

        public ReviewResponseListDTO findByRating(Integer rating) {
                return new ReviewResponseListDTO(
                                reviewRepository.findByRating(rating).stream()
                                                .map(ReviewDTOMapper::toResponseGetDTO)
                                                .collect(Collectors.toList()));
        }

        public ReviewResponseListDTO findByRatingBetween(Integer minRating, Integer maxRating) {
                return new ReviewResponseListDTO(
                                reviewRepository.findByRatingBetween(minRating, maxRating).stream()
                                                .map(ReviewDTOMapper::toResponseGetDTO)
                                                .collect(Collectors.toList()));
        }

        public ReviewResponseListDTO findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
                return new ReviewResponseListDTO(
                                reviewRepository.findByCreatedAtBetween(startDate, endDate).stream()
                                                .map(ReviewDTOMapper::toResponseGetDTO)
                                                .collect(Collectors.toList()));
        }

        public ReviewResponseListDTO findByRatingAndCreatedAtBetween(Integer rating, LocalDateTime startDate,
                        LocalDateTime endDate) {
                return new ReviewResponseListDTO(
                                reviewRepository.findByRatingAndCreatedAtBetween(rating, startDate, endDate).stream()
                                                .map(ReviewDTOMapper::toResponseGetDTO)
                                                .collect(Collectors.toList()));
        }

        public ReviewResponseListDTO findByCustomerFullName(String fullName) {
                return new ReviewResponseListDTO(
                                reviewRepository.findByCustomerFullName(fullName).stream()
                                                .map(ReviewDTOMapper::toResponseGetDTO)
                                                .collect(Collectors.toList()));
        }

        public ReviewResponseListDTO findByCommentContainingIgnoreCase(String keyword) {
                return new ReviewResponseListDTO(
                                reviewRepository.findByCommentContainingIgnoreCase(keyword).stream()
                                                .map(ReviewDTOMapper::toResponseGetDTO)
                                                .collect(Collectors.toList()));
        }

        public ReviewResponseListDTO findByCustomerFullNameAndCommentContaining(String fullName, String keyword) {
                return new ReviewResponseListDTO(
                                reviewRepository.findByCustomerFullNameAndCommentContaining(fullName, keyword).stream()
                                                .map(ReviewDTOMapper::toResponseGetDTO)
                                                .collect(Collectors.toList()));
        }
}