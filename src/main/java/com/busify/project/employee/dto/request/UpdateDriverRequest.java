package com.busify.project.employee.dto.request;

import com.busify.project.employee.enums.EmployeeType;
import com.busify.project.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDriverRequest {
    
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
    private String phoneNumber;
    
    private String address;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    private String driverLicenseNumber;
    
    private EmployeeType employeeType;
    
    private UserStatus status;
    private String operatorName;
    
    private Long operatorId;
}
