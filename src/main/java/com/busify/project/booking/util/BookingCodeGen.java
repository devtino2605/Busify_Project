package com.busify.project.booking.util;

import java.util.UUID;

public class BookingCodeGen {
    public static String generateBookingCode() {
        return "BOOKING-" + UUID.randomUUID().toString();
    }
}
