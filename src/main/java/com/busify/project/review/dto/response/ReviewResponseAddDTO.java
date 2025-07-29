package com.busify.project.review.dto.response;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;
/**
 * DTO for representing a review response specifically for creation.
 */
@Getter
@Setter
@AllArgsConstructor
public class ReviewResponseAddDTO extends ReviewResponseDTO {
    private String message; // Success message or any other relevant information
}
