package com.busify.project.employee.dto.request;

import com.busify.project.employee.enums.EmployeeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeMGMTAddRequestDTO {

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Loại nhân viên không được để trống")
    private EmployeeType employeeType;
}
