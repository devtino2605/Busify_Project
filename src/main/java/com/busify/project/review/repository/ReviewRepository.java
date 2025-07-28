package com.busify.project.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busify.project.review.entity.Review;
import java.util.List;
import com.busify.project.user.entity.User;


public interface ReviewRepository extends JpaRepository<Review,Long>{
        public List<Review> findByCustomerId(Long customerId);
        public List<Review> findByTripId(Long tripId);
        public List<Review> findByTripIdAndCustomer(Long tripId, User customer);
        public List<Review> findByTripIdAndCustomerId(Long tripId, Long customerId);
}