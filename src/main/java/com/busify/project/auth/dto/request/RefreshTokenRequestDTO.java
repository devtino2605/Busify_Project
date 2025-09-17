package com.busify.project.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
    @NotBlank(message = "Refresh token is required")
    @Size(max = 500, message = "Refresh token too long")
    private String refresh_token;
}
