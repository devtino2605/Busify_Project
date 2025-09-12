package com.busify.project.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAddDTO  {
    @NotNull(message = "Trip ID cannot be null")
    private Long tripId;

    @Max(message = "Rating cannot exceed 5", value = 5)
    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    private Integer rating;

    @NotNull(message = "Comment cannot be null")
    @Size(max = 500, min = 4)
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s.,!?]*$", message = "Comment contains invalid characters")
    private String comment;
}