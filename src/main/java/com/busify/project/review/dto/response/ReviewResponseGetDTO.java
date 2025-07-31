package com.busify.project.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for representing a review response with detailed information.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseGetDTO extends ReviewResponseDTO {
    private Long reviewId;
    private Integer rating;
    private String customerName;
    private String comment;
    private String createdAt;
}
