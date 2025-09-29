package com.busify.project.trip.dto.response;

public interface NextTripSeatStatusDTO {

    Long getTripId();

    int getBusSeatsCount();

    int getCheckedSeatsCount();

    int getBookedSeatsCount();
}