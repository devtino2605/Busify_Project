package com.busify.project.trip_seat.dto; // Điều chỉnh package theo dự án của bạn

import java.util.List;

public class SeatLayoutResponse {
    private int rows;
    private int columns;
    private List<SeatResponse> seats;

    // Constructor mặc định (cho deserialization nếu cần)
    public SeatLayoutResponse() {
    }

    // Constructor đầy đủ
    public SeatLayoutResponse(int rows, int columns, List<SeatResponse> seats) {
        this.rows = rows;
        this.columns = columns;
        this.seats = seats;
    }

    // Getters and Setters
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<SeatResponse> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatResponse> seats) {
        this.seats = seats;
    }
}