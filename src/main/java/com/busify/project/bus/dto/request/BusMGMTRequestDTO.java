package com.busify.project.bus.dto.request;

import com.busify.project.bus.enums.BusStatus;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusMGMTRequestDTO {

    @NotBlank(message = "Biển số xe không được để trống")
    @Pattern(
            regexp = "^[0-9]{2}[A-Z]-[0-9]{3}\\.[0-9]{2}$",
            message = "Biển số xe phải theo đúng định dạng: 88A-888.88"
    )
    private String licensePlate;

    @NotBlank(message = "Mẫu xe không được để trống")
    @Size(min = 2, max = 50, message = "Mẫu xe phải có độ dài từ 2 - 50")
    private String model;

    @NotNull(message = "Mã nhà xe không được trống")
    @Positive(message = "Mã nhà xe phải là số nguyên")
    private Long operatorId;

    @NotNull(message = "Mã bố cục ghế không được trống")
    @Positive(message = "Mã bố cục ghế phải là số nguyên")
    private Integer seatLayoutId;

    @NotNull(message = "Tiện ích không được để trống")
    private Map<String, Object> amenities;

    @NotNull(message = "Trạng thái xe không được để trống")
    private BusStatus status;
}
