package com.busify.project.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerSupportEmailRequestDTO {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 50, message = "Email không được vượt quá 50 ký tự")
    private String toEmail;

    @NotBlank(message = "Tên người dùng không được để trống")
    @Size(max = 100, message = "Tên người dùng không được vượt quá 100 ký tự")
    private String userName;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề không được vượt quá 200 ký tự")
    @Size(min = 5, message = "Tiêu đề tối thiểu 5 ký tự")
    private String subject;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    @Size(max = 1000, message = "Nội dung tin nhắn không được vượt quá 1000 ký tự")
    @Size(min = 5, message = "Nội dung tin nhắn tối thiểu 5 ký tự")
    private String message;

    @Size(max = 50, message = "Mã case không được vượt quá 50 ký tự")
    private String caseNumber;

    @NotBlank(message = "Tên nhân viên CS không được để trống")
    @Size(max = 100, message = "Tên nhân viên CS không được vượt quá 100 ký tự")
    private String csRepName;
}
