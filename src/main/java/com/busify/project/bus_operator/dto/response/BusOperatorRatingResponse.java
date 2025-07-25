// DTO cho response
package com.busify.project.bus_operator.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusOperatorRatingResponse {
    private Long id;
    private String name;
    private String email;
    private String hotline;
    private BigDecimal averageRating;
    private Long totalReviews;
}