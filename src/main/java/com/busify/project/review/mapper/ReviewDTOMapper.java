package com.busify.project.review.mapper;

import com.busify.project.review.dto.ReviewAddDTO;
import com.busify.project.review.dto.response.ReviewResponseGetDTO;
import com.busify.project.review.entity.Review;
import com.busify.project.trip.entity.Trip;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;

public class ReviewDTOMapper {
    public static Review toEntity(ReviewAddDTO reviewAddDTO, User user, Trip trip) {
        final Review review = new Review();
        review.setComment(reviewAddDTO.getComment());
        review.setRating(reviewAddDTO.getRating());
        review.setCustomer(user);
        review.setTrip(trip);
        return review;
    }

    public static ReviewResponseGetDTO toResponseGetDTO(Review review) {
        User customer = review.getCustomer();
        String fullName = null;
        if (customer instanceof Profile) {
            fullName = ((Profile) customer).getFullName();
        } else {
            fullName = customer.getEmail(); // or any other fallback
        }
        return new ReviewResponseGetDTO(
                review.getReviewId(),
                review.getRating(),
                fullName,
                review.getComment(),
                review.getCreatedAt().toString());
    }
}
