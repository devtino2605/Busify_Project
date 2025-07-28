package com.busify.project.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for representing a review response with detailed information.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseGetDTO extends ReviewResponseDTO {
    private Long reviewId;
    private Long tripId;
    private Long customerId;
    private Integer rating;
    private String comment;
    private String createdAt;
}
