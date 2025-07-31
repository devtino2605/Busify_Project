package com.busify.project.review.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
/**
 * DTO for representing a list of review responses.
 */
@Getter
@Setter
@AllArgsConstructor
public class ReviewResponseListDTO extends ReviewResponseDTO {
    private List<ReviewResponseGetDTO> reviews;
}
