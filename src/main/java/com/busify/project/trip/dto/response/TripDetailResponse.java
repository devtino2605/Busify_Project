package com.busify.project.trip.dto.response;

import java.time.Instant;

public interface TripDetailResponse {
    Long getId(); // trip_id

    Long getRouteId(); // route.route_id

    String getStartCity();

    String getStartName();

    String getStartAddress();

    Double getStartLongitude();

    Double getStartLatitude();

    String getEndCity();

    String getEndName();

    String getEndAddress();

    Double getEndLongitude();

    Double getEndLatitude();

    Integer getEstimatedDurationMinutes();

    Long getBusId();

    String getBusName();

    String getBusAmenities();

    Integer getBusSeats();

    String getBusLicensePlate();

    Long getOperatorId();

    String getOperatorName();

    Instant getDepartureTime();

    Double getPricePerSeat();

    Instant getEstimatedArrivalTime(); // arrival_time

    Integer getAvailableSeats(); // available_seats

    // --- Thông tin đánh giá ---
    Double getAverageRating(); // average_rating

    Integer getTotalReviews(); // total_reviews
}
