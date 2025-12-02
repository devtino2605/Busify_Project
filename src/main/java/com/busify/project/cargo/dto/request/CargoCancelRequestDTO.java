package com.busify.project.cargo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CargoCancelRequestDTO
 * 
 * Request DTO for cancelling a cargo booking
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoCancelRequestDTO {
    @NotBlank(message = "Lý do hủy không được để trống")
    @Size(min = 10, max = 500, message = "Lý do hủy phải từ 10-500 ký tự")
    private String reason;
}
