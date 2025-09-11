package com.busify.project.seat_layout.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatLayoutFilterTripResponse {
    @NotNull(message = "ID không được null")
    @Positive(message = "ID phải là số dương")
    private Integer id;
    
    @NotBlank(message = "Tên layout không được để trống")
    @Size(min = 2, max = 100, message = "Tên layout phải từ 2-100 ký tự")
    private String name;
}
