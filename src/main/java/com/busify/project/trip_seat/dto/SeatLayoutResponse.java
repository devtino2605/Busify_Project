package com.busify.project.trip_seat.dto; // Điều chỉnh package theo dự án của bạn

import java.util.List;

public class SeatLayoutResponse {
    private int rows;
    private int columns;
    private int floors; // Thêm trường cho số tầng
    private List<SeatResponse> seats;

    // Constructor mặc định (cho deserialization nếu cần)
    public SeatLayoutResponse() {
    }

    // Constructor đầy đủ
    public SeatLayoutResponse(int rows, int columns, int floors, List<SeatResponse> seats) {
        this.rows = rows;
        this.columns = columns;
        this.floors = floors; // Khởi tạo số tầng
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
    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public List<SeatResponse> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatResponse> seats) {
        this.seats = seats;
    }
}