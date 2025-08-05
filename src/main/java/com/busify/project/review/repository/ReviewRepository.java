package com.busify.project.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.busify.project.review.entity.Review;
import java.util.List;
import com.busify.project.user.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {
        public List<Review> findByCustomerId(Long customerId);

        public List<Review> findByTripId(Long tripId);

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
}