package com.busify.project.booking.util;

import java.util.UUID;

public class BookingCodeGen {
    public static String generateBookingCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
    }
}
