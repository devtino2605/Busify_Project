package com.busify.project.trip_seat.dto;

public class SeatResponse {
    private String seatNumber;
    private String status; // "booked" or "available"
    private boolean isBooked;

    public SeatResponse(String seatNumber, String status, boolean isBooked) {
        this.seatNumber = seatNumber;
        this.status = status;
        this.isBooked = isBooked;
    }

    // Getters and Setters
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean isBooked) { this.isBooked = isBooked; }
}