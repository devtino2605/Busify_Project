package com.busify.project.cargo.dto.request;

import com.busify.project.cargo.enums.CargoStatus;
import com.busify.project.cargo.enums.CargoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CargoSearchRequestDTO
 * 
 * Request DTO for searching cargo bookings
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoSearchRequestDTO {

    // Search by code/phone/name
    private String keyword; // Search in cargo_code, sender_phone, receiver_phone, sender_name,
                            // receiver_name

    // Filter by status
    private CargoStatus status;

    // Filter by cargo type
    private CargoType cargoType;

    // Filter by trip
    private Long tripId;

    // Filter by locations
    private Long pickupLocationId;
    private Long dropoffLocationId;

    // Filter by user
    private Long bookingUserId;

    // Filter by date range
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;

    // Filter by weight range
    private BigDecimal minWeight;
    private BigDecimal maxWeight;

    // Sort options
    private String sortBy = "createdAt"; // createdAt, weight, totalAmount, status
    private String sortDirection = "DESC"; // ASC or DESC
}
