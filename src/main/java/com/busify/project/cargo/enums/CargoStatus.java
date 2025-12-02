package com.busify.project.cargo.enums;

/**
 * CargoStatus Enum
 * 
 * Represents the various states of a cargo booking throughout its lifecycle
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
public enum CargoStatus {
    /**
     * Initial state when cargo booking is created, awaiting confirmation
     */
    PENDING("Chờ xác nhận"),

    /**
     * Cargo booking has been confirmed by staff/system
     */
    CONFIRMED("Đã xác nhận"),

    /**
     * Driver has picked up the cargo from sender
     */
    PICKED_UP("Đã lấy hàng"),

    /**
     * Cargo is currently being transported on the trip
     */
    IN_TRANSIT("Đang vận chuyển"),

    /**
     * Cargo has arrived at destination location
     */
    ARRIVED("Đã đến nơi"),

    /**
     * Cargo has been successfully delivered to receiver
     */
    DELIVERED("Đã giao hàng"),

    /**
     * Cargo booking has been cancelled by customer
     */
    CANCELLED("Đã hủy"),

    /**
     * Cargo booking rejected by staff after inspection
     * (e.g., prohibited items, wrong description, oversized, etc.)
     */
    REJECTED("Bị từ chối"),

    /**
     * Cargo returned to sender (delivery failed or refused)
     */
    RETURNED("Hoàn trả");

    private final String displayName;

    CargoStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Check if the status represents a completed state (delivered, cancelled,
     * rejected, or returned)
     */
    public boolean isCompleted() {
        return this == DELIVERED || this == CANCELLED || this == REJECTED || this == RETURNED;
    }

    /**
     * Check if the status allows cancellation
     */
    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }

    /**
     * Check if the status allows status update
     */
    public boolean canBeUpdated() {
        return !isCompleted();
    }
}
