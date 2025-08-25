package com.busify.project.employee.dto.request;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.user.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeMGMTRequestDTO {
    @NotBlank(message = "Số GPLX không được để trống")
    @Size(max = 20, message = "Số GPLX không được vượt quá 20 ký tự")
    private String driverLicenseNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotNull(message = "Trạng thái không được để trống")
    private UserStatus status;
}
