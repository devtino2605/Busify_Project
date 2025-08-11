package com.busify.project.auth.dto.request;

import lombok.Data;

@Data
public class RefreshTokenRequestDTO {
    private String refresh_token;
}
