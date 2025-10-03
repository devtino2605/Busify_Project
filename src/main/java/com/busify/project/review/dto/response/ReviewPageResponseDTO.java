package com.busify.project.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPageResponseDTO {
    private List<ReviewResponseGetDTO> reviews;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;

    public static ReviewPageResponseDTO fromPage(Page<ReviewResponseGetDTO> reviewPage) {
        return ReviewPageResponseDTO.builder()
                .reviews(reviewPage.getContent())
                .currentPage(reviewPage.getNumber())
                .totalPages(reviewPage.getTotalPages())
                .totalElements(reviewPage.getTotalElements())
                .pageSize(reviewPage.getSize())
                .hasNext(reviewPage.hasNext())
                .hasPrevious(reviewPage.hasPrevious())
                .build();
    }
}
