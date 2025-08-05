package com.busify.project.trip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripListResponse {
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
    private List<TripResponse> trips;
}
