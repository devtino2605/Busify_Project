package com.busify.project.bus_operator.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBusOperatorRequest {
    @NotBlank(message = "Tên")
    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Hotline là bắt buộc")
    @Size(max = 20, message = "Hotline không được vượt quá 20 ký tự")
    private String hotline;

    @NotBlank(message = "Địa chỉ là bắt buộc")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String address;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    // File upload for license instead of path
    private MultipartFile licenseFile;
}
