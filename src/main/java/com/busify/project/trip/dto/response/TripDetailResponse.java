package com.busify.project.trip.dto.response;

import java.time.Instant;

public interface TripDetailResponse {

    // --- Thông tin cơ bản về chuyến đi ---
    Long getId(); // trip_id

    Long getRouteId(); // route.route_id

    String getOperatorName(); // operator_name

    Instant getDepartureTime(); // departure_time

    Instant getEstimatedArrivalTime(); // arrival_time

    Integer getEstimatedDurationMinutes(); // Dữ liệu gốc cho estimated_duration

    Integer getAvailableSeats(); // available_seats

    Integer getBusSeats(); // total_seats

    Double getPricePerSeat(); // price_per_seat

    // --- Thông tin đánh giá ---
    Double getAverageRating(); // average_rating

    Integer getTotalReviews(); // total_reviews

    // --- Địa điểm xuất phát ---
    String getStartCity(); // route.start_location.city

    String getStartAddress(); // route.start_location.address

    Double getStartLongitude(); // route.start_location.longitude

    Double getStartLatitude(); // route.start_location.latitude

    // --- Địa điểm đến ---
    String getEndCity(); // route.end_location.city

    String getEndAddress(); // route.end_location.address

    Double getEndLongitude(); // route.end_location.longitude

    Double getEndLatitude(); // route.end_location.latitude

    // --- Chi tiết về xe buýt ---
    String getBusName(); // bus.name

    Integer getBusLayoutId(); // bus.layout_id

    String getBusLicensePlate(); // bus.license_plate

    String getBusAmenities(); // bus.amenities
}
