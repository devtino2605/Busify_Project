package com.busify.project.seat_layout.dto.response;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatLayoutFilterTripResponse implements Serializable {
    private Integer id;
    
    @NotBlank(message = "Tên layout không được để trống")
    @Size(min = 2, max = 100, message = "Tên layout phải từ 2-100 ký tự")
    private String name;
}
