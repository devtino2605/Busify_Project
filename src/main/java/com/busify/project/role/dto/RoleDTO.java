package com.busify.project.role.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDTO implements Serializable {
    private Integer id;
    
    @NotBlank(message = "Tên role không được để trống")
    @Size(min = 2, max = 50, message = "Tên role phải từ 2-50 ký tự")
    @Pattern(regexp = "^[A-Z_]+$", message = "Tên role chỉ được chứa chữ hoa và dấu gạch dưới")
    private String name;
}