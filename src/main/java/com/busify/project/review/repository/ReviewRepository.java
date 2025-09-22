package com.busify.project.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.busify.project.review.entity.Review;
import java.util.List;
import com.busify.project.user.entity.User;
import java.time.LocalDateTime;

public interface ReviewRepository extends JpaRepository<Review, Long> {
        public List<Review> findByCustomerId(Long customerId);

        @Query("SELECT r FROM Review r JOIN Profile p ON r.customer.id = p.id WHERE r.trip.id = :tripId")
        public List<Review> findByTripId(@Param("tripId") Long tripId);

        public List<Review> findByTripIdAndCustomer(Long tripId, User customer);

        public List<Review> findByTripIdAndCustomerId(Long tripId, Long customerId);

        @Query("SELECT AVG(r.rating) FROM Review r WHERE r.trip.id = :tripId")
        public Double findAverageRatingByTripId(
                        @Param("tripId") Long tripId);

        @Query("SELECT AVG(r.rating) FROM Review r WHERE r.trip.bus.operator.id = :operatorId")
        public Double findAverageRatingByOperatorId(@Param("operatorId") Long operatorId);

        @Query("SELECT r FROM Review r WHERE r.trip.bus.operator.id = :busOperatorId ORDER BY r.createdAt DESC LIMIT :limit")
        public List<Review> findByBusOperatorReviews(
                        @Param("busOperatorId") Long busOperatorId,
                        @Param("limit") int limit);

        @Query("SELECT COUNT(r) FROM Review r WHERE r.trip.id = :tripId")
        public Long countByTripId(@Param("tripId") Long tripId);

        @Query("SELECT COUNT(r) FROM Review r WHERE r.trip.bus.operator.id = :busOperatorId")
        public Long countByBusOperatorId(@Param("busOperatorId") Long busOperatorId);

        // Filter by rating (star)
        Page<Review> findByRating(Integer rating, Pageable pageable);

        // Filter by rating range
        Page<Review> findByRatingBetween(Integer minRating, Integer maxRating, Pageable pageable);

        // Filter by date range
        Page<Review> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

        // Combine filters: rating, date, processed
        Page<Review> findByRatingAndCreatedAtBetween(
                        Integer rating, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

        // Find reviews by customer full name using Profile entity
        @Query("SELECT r FROM Review r JOIN Profile p ON r.customer.id = p.id WHERE p.fullName LIKE %:fullName%")
        Page<Review> findByCustomerFullName(@Param("fullName") String fullName, Pageable pageable);

        // Find reviews by comment (exact match)
        Page<Review> findByComment(String comment, Pageable pageable);

        // Find reviews by comment containing keyword (case insensitive)
        Page<Review> findByCommentContainingIgnoreCase(String keyword, Pageable pageable);

        // Find reviews by customer full name and comment containing keyword
        @Query("SELECT r FROM Review r JOIN Profile p ON r.customer.id = p.id " +
                        "WHERE p.fullName LIKE %:fullName% AND LOWER(r.comment) LIKE LOWER(concat('%', :keyword, '%'))")
        Page<Review> findByCustomerFullNameAndCommentContaining(
                        @Param("fullName") String fullName,
                        @Param("keyword") String keyword, Pageable pageable);
}