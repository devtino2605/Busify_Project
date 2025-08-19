package com.busify.project.employee.dto.request;

import com.busify.project.employee.enums.EmployeeType;
import com.busify.project.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDriverRequest {
    
    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;
    
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại phải có 10-11 chữ số")
    private String phoneNumber;
    
    private String address;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
    
    private String driverLicenseNumber;
    
    @Builder.Default
    private EmployeeType employeeType = EmployeeType.DRIVER;
    
    @Builder.Default
    private UserStatus status = UserStatus.active;
    
    // Có thể dùng operatorId hoặc operatorName, priority cho operatorId
    private Long operatorId;
    private String operatorName;
    
    // Password mặc định cho tài khoản mới
    private String password;
}
