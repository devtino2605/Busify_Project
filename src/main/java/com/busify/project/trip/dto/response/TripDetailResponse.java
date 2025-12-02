package com.busify.project.trip.dto.response;

import java.time.LocalDateTime;

public interface TripDetailResponse {

    // --- Thông tin cơ bản về chuyến đi ---
    Long getId(); // trip_id

    Long getRouteId(); // route.route_id

    Long getOperatorId(); // operator.operator_id

    String getOperatorName(); // operator_name

    LocalDateTime getDepartureTime(); // departure_time

    LocalDateTime getEstimatedArrivalTime(); // arrival_time

    Integer getEstimatedDurationMinutes(); // Dữ liệu gốc cho estimated_duration

    Integer getAvailableSeats(); // available_seats

    Integer getBusSeats(); // total_seats

    Double getPricePerSeat(); // price_per_seat

    Double getOriginalPrice(); // original_price

    Double getDiscountAmount(); // discount_amount

    // --- Thông tin đánh giá ---
    Double getAverageRating(); // average_rating

    Integer getTotalReviews(); // total_reviews

    // --- Địa điểm xuất phát ---
    Long getStartLocationId(); // route.start_location.id

    String getStartCity(); // route.start_location.city

    String getStartAddress(); // route.start_location.address

    Double getStartLongitude(); // route.start_location.longitude

    Double getStartLatitude(); // route.start_location.latitude

    // --- Địa điểm đến ---
    Long getEndLocationId(); // route.end_location.id

    String getEndCity(); // route.end_location.city

    String getEndAddress(); // route.end_location.address

    Double getEndLongitude(); // route.end_location.longitude

    Double getEndLatitude(); // route.end_location.latitude

    // --- Chi tiết về xe buýt ---
    Long getBusId(); // bus.id

    String getBusName(); // bus.name

    Integer getBusLayoutId(); // bus.layout_id

    String getBusLicensePlate(); // bus.license_plate

    String getBusAmenities(); // bus.amenities

    // --- Thông tin tài xế ---
    Long getDriverId(); // driver.id

    String getDriverName(); // driver.full_name
}
