package com.busify.project.auth.controller;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.busify.project.auth.dto.request.ForgotPasswordRequestDTO;
import com.busify.project.auth.dto.request.LoginGoogleDTO;
import com.busify.project.auth.dto.request.LoginRequestDTO;
import com.busify.project.auth.dto.request.RefreshTokenRequestDTO;
import com.busify.project.auth.dto.request.ResendVerificationRequest;
import com.busify.project.auth.dto.request.ResetPasswordRequestDTO;
import com.busify.project.auth.dto.response.LoginResponseDTO;
import com.busify.project.auth.dto.response.MessageResponse;
import com.busify.project.auth.service.AuthService;
import com.busify.project.auth.service.impl.EmailVerificationServiceImpl;
import com.busify.project.common.dto.response.ApiResponse;
import com.busify.project.user.dto.request.RegisterRequestDTO;
import com.busify.project.user.dto.response.RegisterResponseDTO;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {
    private final AuthService authService;
    private final EmailVerificationServiceImpl emailVerificationService;

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

    @GetMapping("/login/google")
    public ResponseEntity<Void> googleLogin() {
        // This endpoint will be handled by Spring Security OAuth2
        // Users will be redirected to Google OAuth consent screen
        // After successful authentication, they will be redirected back to success
        // handler
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(java.net.URI.create("/oauth2/authorization/google"))
                .build();
    }

    @PostMapping("/google-signin")
    public ApiResponse<LoginResponseDTO> googleSignIn(@RequestBody LoginGoogleDTO loginGoogleDTO) {
        LoginResponseDTO response = authService.googleSignIn(loginGoogleDTO.getEmail());
        if (response == null) {
            return ApiResponse.badRequest("Google sign-in failed");
        }
        return ApiResponse.success("Google sign-in successful", response);
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

    @PostMapping("/register")
    public ApiResponse<RegisterResponseDTO> registerUser(@RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = authService.register(request);
        if (response == null) {
            return ApiResponse.badRequest("register user failed");
        }
        return ApiResponse.success("register user successful", response);
    }

    @GetMapping("/verify-email")
    public ApiResponse<MessageResponse> verifyEmail(@RequestParam String token) {
        emailVerificationService.verifyEmail(token);
        return ApiResponse.success("Email verified successfully", new MessageResponse("Email verified successfully"));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<MessageResponse> resendVerification(@RequestBody ResendVerificationRequest request) {
        emailVerificationService.resendVerificationEmail(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("Verification email sent"));
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        try {
            authService.forgotPassword(request);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Password reset email sent successfully")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Void>error(HttpStatus.BAD_REQUEST.value(),
                    "Failed to send password reset email: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        try {
            authService.resetPassword(request);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Password reset successfully")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<Void>error(HttpStatus.BAD_REQUEST.value(),
                    "Failed to reset password: " + e.getMessage());
        }
    }
}
