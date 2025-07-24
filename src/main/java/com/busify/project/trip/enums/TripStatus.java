package com.busify.project.trip.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TripStatus {
    SCHEDULED("scheduled"),
    ON_TIME("on_time"),
    DELAYED("delayed"),
    DEPARTED("departed"),
    ARRIVED("arrived"),
    CANCELLED("cancelled");

    private final String label;
    TripStatus(String label) {
        this.label = label;
    }

    @JsonCreator
    public static TripStatus fromLabel(String label) {
        for (TripStatus status : TripStatus.values()) {
            if (status.label.equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown trip status: " + label);
    }
}
