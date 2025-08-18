package com.busify.project.bus_operator.dto.request;

import com.busify.project.bus_operator.enums.OperatorStatus;
import jakarta.validation.constraints.Email;
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
public class UpdateBusOperatorRequest {
    @Size(max = 100, message = "Operator name must not exceed 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Size(max = 20, message = "Hotline must not exceed 20 characters")
    private String hotline;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private OperatorStatus status;

    // File upload for license instead of path
    private MultipartFile licenseFile;
}
