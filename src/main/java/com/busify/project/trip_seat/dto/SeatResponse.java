package com.busify.project.trip_seat.dto;

public class SeatResponse {
    private String seatNumber;
    private String status; // "booked" or "available"
    private boolean isBooked;
    private String row; // Thêm trường row
    private String column; // Thêm trường column
    private String floor; // Thêm trường floor

    // Constructor đầy đủ
    public SeatResponse(String seatNumber, String status, boolean isBooked, String row, String column, String floor) {
        this.seatNumber = seatNumber;
        this.status = status;
        this.isBooked = isBooked;
        this.row = row;
        this.column = column;
        this.floor = floor;
    }

   
    // Getters and Setters
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean isBooked) { this.isBooked = isBooked; }
    public String getRow() { return row; }
    public void setRow(String row) { this.row = row; }
    public String getColumn() { return column; }
    public void setColumn(String column) { this.column = column; }
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
}