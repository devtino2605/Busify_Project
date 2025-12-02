package com.busify.project.cargo.dto.request;

import com.busify.project.cargo.enums.CargoStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CargoUpdateStatusRequestDTO
 * 
 * Request DTO for updating cargo status
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoUpdateStatusRequestDTO {
    @NotNull(message = "Trạng thái mới không được để trống")
    private CargoStatus status;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    private String notes;

    @Size(max = 200, message = "Vị trí không được vượt quá 200 ký tự")
    private String location;
}
