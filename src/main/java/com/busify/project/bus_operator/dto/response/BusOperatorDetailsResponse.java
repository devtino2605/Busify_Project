package com.busify.project.bus_operator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusOperatorDetailsResponse {
    private Long id;
    private String name;
    private String email;
    private String hotline;
    private String description;
    private String logoUrl;
    private String address;
    private Double rating;
    private Long totalReviews;
}
