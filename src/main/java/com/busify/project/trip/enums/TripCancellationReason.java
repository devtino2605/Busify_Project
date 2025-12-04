package com.busify.project.trip.enums;

import lombok.Getter;

/**
 * Enum representing the reasons for trip cancellation or delay.
 * Used when operator/admin needs to cancel or delay a trip.
 */
@Getter
public enum TripCancellationReason {
    WEATHER("Thời tiết xấu", "Bad weather conditions"),
    NATURAL_DISASTER("Thiên tai (bão, lũ, động đất...)", "Natural disaster (storm, flood, earthquake...)"),
    VEHICLE_BREAKDOWN("Xe bị hỏng/sự cố kỹ thuật", "Vehicle breakdown/technical issues"),
    DRIVER_UNAVAILABLE("Tài xế không khả dụng", "Driver unavailable"),
    ROAD_BLOCKED("Đường bị chặn/tắc nghẽn", "Road blocked/traffic jam"),
    ACCIDENT("Tai nạn giao thông", "Traffic accident"),
    LOW_BOOKINGS("Số lượng đặt vé quá ít", "Insufficient bookings"),
    OPERATOR_REQUEST("Yêu cầu từ nhà xe", "Operator request"),
    GOVERNMENT_ORDER("Lệnh từ cơ quan chức năng", "Government order"),
    SECURITY_CONCERN("Vấn đề an ninh", "Security concern"),
    OTHER("Lý do khác", "Other reason");

    private final String vietnameseDescription;
    private final String englishDescription;

    TripCancellationReason(String vietnameseDescription, String englishDescription) {
        this.vietnameseDescription = vietnameseDescription;
        this.englishDescription = englishDescription;
    }

    /**
     * Check if this reason qualifies for full refund (100%)
     * Weather, natural disasters, vehicle issues, and government orders qualify for
     * full refund
     */
    public boolean qualifiesForFullRefund() {
        return this == WEATHER
                || this == NATURAL_DISASTER
                || this == VEHICLE_BREAKDOWN
                || this == ACCIDENT
                || this == GOVERNMENT_ORDER
                || this == ROAD_BLOCKED
                || this == DRIVER_UNAVAILABLE
                || this == SECURITY_CONCERN;
    }

    /**
     * Check if this is an emergency/force majeure reason
     */
    public boolean isEmergency() {
        return this == WEATHER
                || this == NATURAL_DISASTER
                || this == ACCIDENT
                || this == GOVERNMENT_ORDER
                || this == SECURITY_CONCERN;
    }
}
