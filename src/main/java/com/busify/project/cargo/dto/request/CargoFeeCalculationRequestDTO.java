package com.busify.project.cargo.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * CargoFeeCalculationRequestDTO
 * 
 * Request DTO for calculating cargo fee BEFORE booking
 * Lightweight DTO with only necessary fields for fee calculation
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoFeeCalculationRequestDTO {

    @NotNull(message = "Trip ID không được để trống")
    @Positive(message = "Trip ID phải là số dương")
    private Long tripId;

    @NotNull(message = "Điểm lấy hàng không được để trống")
    @Positive(message = "Điểm lấy hàng không hợp lệ")
    private Long pickupLocationId;

    @NotNull(message = "Điểm giao hàng không được để trống")
    @Positive(message = "Điểm giao hàng không hợp lệ")
    private Long dropoffLocationId;

    @NotNull(message = "Cân nặng không được để trống")
    @DecimalMin(value = "0.1", message = "Cân nặng tối thiểu 0.1 kg")
    @DecimalMax(value = "50.0", message = "Cân nặng tối đa 50 kg")
    @Digits(integer = 8, fraction = 2, message = "Cân nặng không hợp lệ")
    private BigDecimal weight;

    @DecimalMin(value = "0", message = "Giá trị khai báo phải >= 0")
    @Digits(integer = 10, fraction = 2, message = "Giá trị khai báo không hợp lệ")
    private BigDecimal declaredValue; // Optional - for insurance calculation
}
