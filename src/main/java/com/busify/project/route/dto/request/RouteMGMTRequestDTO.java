package com.busify.project.route.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteMGMTRequestDTO {
    @NotNull(message = "Điểm bắt đầu không được để trống")
    private Long startLocationId;

    @NotNull(message = "Điểm kết thúc không được để trống")
    private Long endLocationId;

    @NotNull(message = "Thời gian mặc định không được để trống")
    @Min(value = 1, message = "Thời gian mặc định phải lớn hơn 0 phút")
    private Integer defaultDurationMinutes;

    @NotNull(message = "Giá mặc định không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá mặc định phải lớn hơn 0")
    private BigDecimal defaultPrice;
}
