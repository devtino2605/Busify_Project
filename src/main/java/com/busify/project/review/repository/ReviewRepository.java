package com.busify.project.review.repository;

import com.busify.project.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Trip, Long> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.trip.id = :tripId")
    Double findAverageRatingByTripId(@Param("tripId") Long tripId);
}
