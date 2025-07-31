package com.busify.project.auth.controller;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.auth.dto.request.LoginRequestDTO;
import com.busify.project.auth.dto.request.RefreshTokenRequestDTO;
import com.busify.project.auth.dto.response.LoginResponseDTO;
import com.busify.project.auth.service.AuthService;
import com.busify.project.common.dto.response.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            LoginResponseDTO response = authService.login(loginRequestDTO);
            return ApiResponse.<LoginResponseDTO>builder()
                    .code(HttpStatus.OK.value())
                    .message("Login successful")
                    .result(response)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<LoginResponseDTO>error(HttpStatus.UNAUTHORIZED.value(),
                    "Login failed, error" + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ApiResponse<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequestDTO refreshToken) {
        try {
            String response = authService.refreshToken(refreshToken);
            return ApiResponse.<Map<String, String>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Token refreshed successfully")
                    .result(Collections.singletonMap("access_token", response))
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Map<String, String>>error(HttpStatus.UNAUTHORIZED.value(),
                    "Token refresh failed, error: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        try {
            authService.logout();
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Logout successful")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Void>error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Logout failed, error: " + e.getMessage());
        }
    }
}
