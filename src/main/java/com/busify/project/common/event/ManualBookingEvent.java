package com.busify.project.common.event;

import com.busify.project.booking.entity.Bookings;

import lombok.Getter;

@Getter
public class ManualBookingEvent extends BusifyEvent {
    public ManualBookingEvent(Object source, String message, Bookings booking) {
        super(source, message);
        this.booking = booking;
    }

    private final Bookings booking;
}