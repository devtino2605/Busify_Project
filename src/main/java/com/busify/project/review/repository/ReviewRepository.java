package com.busify.project.review.repository;

import com.busify.project.review.entity.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTripId(Long tripId);
}