package com.busify.project.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String username;
    private String email;
    private String role;
    private String accessToken;
    private String refreshToken;
}
