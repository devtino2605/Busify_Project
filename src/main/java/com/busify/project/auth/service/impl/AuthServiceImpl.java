package com.busify.project.auth.service.impl;

import com.busify.project.auth.dto.request.LoginRequestDTO;
import com.busify.project.auth.dto.request.RefreshTokenRequestDTO;
import com.busify.project.auth.dto.response.LoginResponseDTO;
import com.busify.project.auth.service.AuthService;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.dto.request.RegisterRequestDTO;
import com.busify.project.user.dto.response.RegisterResponseDTO;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtils jwtUtil;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final EmailVerificationServiceImpl emailVerificationService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        Authentication authenticationManager = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager);

        User user = userRepository.findByEmail(loginRequestDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Kiểm tra xác thực email
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email chưa được xác thực. Vui lòng kiểm tra email để xác thực tài khoản.");
        }

        String token = generateToken(loginRequestDTO.getUsername());
        String refreshToken = generateRefreshToken(loginRequestDTO.getUsername());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        return LoginResponseDTO.builder()
                .email(user.getEmail())
                .role(user.getRole().getName())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public String refreshToken(RefreshTokenRequestDTO refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken.getRefresh_token())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String newAccessToken = generateToken(user.getEmail());
        return newAccessToken;
    }

    @Override
    public void logout() {
        String email = jwtUtil.getCurrentUserLogin().isPresent() ? jwtUtil.getCurrentUserLogin().get() : "";
        if (email == null || email.isEmpty()) {
            throw new UsernameNotFoundException("No authenticated user found for logout");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    @Override
    public String generateToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public String generateRefreshToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return jwtUtil.generateRefreshToken(userDetails);
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO registerDTO) {
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Profile user = new Profile();
        user.setEmail(registerDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setFullName(registerDTO.getName());
        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("CUSTOMER");
                    return roleRepository.save(newRole);
                });
        user.setRole(customerRole);
        userRepository.save(user);

        // Gửi email xác thực - user đã là Profile nên không cần cast
        emailVerificationService.sendVerificationEmail(user);

        return RegisterResponseDTO.builder()
                .email(user.getEmail())
                .message("Verification email sent")
                .build();
    }
}
