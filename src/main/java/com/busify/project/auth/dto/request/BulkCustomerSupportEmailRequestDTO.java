package com.busify.project.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BulkCustomerSupportEmailRequestDTO {

    @NotNull(message = "Trip ID mustn't be null")
    @Positive(message = "Trip ID must be positive")
    private Long tripId;

    @NotBlank(message = "Subject mustn't be null")
    @Size(max = 200, message = "Subject must less than 200")
    @Size(min = 3, message = "Subject must greater than 3")
    private String subject;

    @NotBlank(message = "Message mustn't be null")
    @Size(max = 1000, message = "Message must less than 200")
    @Size(min = 5, message = "Nội dung tin nhắn tối thiểu 5 kí tự")
    private String message;

    @NotBlank(message = "Tên nhân viên CS không được để trống")
    @Size(max = 100, message = "Tên nhân viên CS không được vượt quá 100 ký tự")
    private String csRepName;
}