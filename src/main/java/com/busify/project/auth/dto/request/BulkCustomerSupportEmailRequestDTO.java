package com.busify.project.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BulkCustomerSupportEmailRequestDTO {

    @NotNull(message = "Trip ID không được để trống")
    private Long tripId;

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 200, message = "Tiêu đề không được vượt quá 200 ký tự")
    private String subject;

    @NotBlank(message = "Nội dung tin nhắn không được để trống")
    @Size(max = 1000, message = "Nội dung tin nhắn không được vượt quá 1000 ký tự")
    private String message;

    @NotBlank(message = "Tên nhân viên CS không được để trống")
    @Size(max = 100, message = "Tên nhân viên CS không được vượt quá 100 ký tự")
    private String csRepName;
}