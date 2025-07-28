package com.busify.project.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long tripId;
    @NotNull
    private Long customerId;

    @Max(5)
    @NotNull
    @Min(1)
    private Integer rating;

    @NotNull
    @Size(max = 500, min = 10)
    private String comment;
}
