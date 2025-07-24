package com.busify.project.trip.dto.response;

import java.util.List;

public class TripListResponse {
    private Integer page;
    private Integer pageSeize;
    private Integer totalPages;
    private List<TripResponse> trips;
}
