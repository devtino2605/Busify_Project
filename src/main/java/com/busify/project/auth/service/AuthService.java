package com.busify.project.auth.service;

import com.busify.project.auth.dto.request.LoginRequestDTO;
import com.busify.project.auth.dto.request.RefreshTokenRequestDTO;
import com.busify.project.auth.dto.response.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    String refreshToken(RefreshTokenRequestDTO refreshToken);

    void logout();

    public String generateToken(String username);

    public String generateRefreshToken(String username);
}
