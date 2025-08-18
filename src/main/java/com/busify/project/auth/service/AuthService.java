package com.busify.project.auth.service;

import com.busify.project.auth.dto.request.ForgotPasswordRequestDTO;
import com.busify.project.auth.dto.request.LoginRequestDTO;
import com.busify.project.auth.dto.request.RefreshTokenRequestDTO;
import com.busify.project.auth.dto.request.ResetPasswordRequestDTO;
import com.busify.project.auth.dto.response.LoginResponseDTO;
import com.busify.project.user.dto.request.RegisterRequestDTO;
import com.busify.project.user.dto.response.RegisterResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    String refreshToken(RefreshTokenRequestDTO refreshToken);

    void logout();

    public String generateToken(String username);

    public String generateRefreshToken(String username);

    public RegisterResponseDTO register(RegisterRequestDTO registerDTO);

    public LoginResponseDTO googleSignIn(String email);

    void forgotPassword(ForgotPasswordRequestDTO request);

    void resetPassword(ResetPasswordRequestDTO request);
}