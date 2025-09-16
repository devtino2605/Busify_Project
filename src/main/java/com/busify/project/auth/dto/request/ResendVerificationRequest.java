package com.busify.project.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResendVerificationRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    @Size(max = 255)
    private String email;

}