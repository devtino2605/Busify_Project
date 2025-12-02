package com.busify.project.cargo.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
public class ValidateCargoTripRequestDTO {

    @NotNull(message = "Trip ID không được để trống")
    @Positive(message = "Trip ID phải là số dương")
    private Long tripId;

    @NotBlank(message = "Cargo code không được để trống")
    @Size(min = 10, max = 30, message = "Cargo code phải từ 10-30 ký tự")
    @Pattern(regexp = "^CRG-\\d{8}-[A-Z0-9]{4}$", message = "Cargo code phải có định dạng CRG-YYYYMMDD-XXXX")
    private String cargoCode;
}
