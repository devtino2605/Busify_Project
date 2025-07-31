package com.busify.project.auth.service.impl;

import com.busify.project.auth.dto.request.LoginRequestDTO;
import com.busify.project.auth.dto.request.RefreshTokenRequestDTO;
import com.busify.project.auth.dto.response.LoginResponseDTO;
import com.busify.project.auth.service.AuthService;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtils jwtUtil;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        Authentication authenticationManager = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager);

        User user = userRepository.findByEmail(loginRequestDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = generateToken(loginRequestDTO.getUsername());
        String refreshToken = generateRefreshToken(loginRequestDTO.getUsername());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return LoginResponseDTO.builder()
                .username(user.getFullName())
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
        System.out.println("Logging out user: " + email);
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
}
