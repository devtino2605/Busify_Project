package com.busify.project.review.service;

import org.springframework.stereotype.Service;
import com.busify.project.review.repository.ReviewRepository;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public abstract class ReviewService {
    protected final ReviewRepository reviewRepository;

    protected final UserRepository userRepository;

    protected final TripRepository tripRepository;
}
