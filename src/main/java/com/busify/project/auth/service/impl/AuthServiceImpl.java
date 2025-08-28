package com.busify.project.auth.service.impl;

import com.busify.project.auth.dto.request.ForgotPasswordRequestDTO;
import com.busify.project.auth.dto.request.LoginRequestDTO;
import com.busify.project.auth.dto.request.RefreshTokenRequestDTO;
import com.busify.project.auth.dto.request.ResetPasswordRequestDTO;
import com.busify.project.auth.dto.response.LoginResponseDTO;
import com.busify.project.auth.entity.VerificationToken;
import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.auth.enums.TokenType;
import com.busify.project.auth.exception.AuthenticationException;
import com.busify.project.auth.exception.PasswordResetException;
import com.busify.project.auth.exception.UserNotFoundException;
import com.busify.project.auth.exception.UserRegistrationException;
import com.busify.project.auth.repository.VerificationTokenRepository;
import com.busify.project.auth.service.AuthService;
import com.busify.project.auth.service.EmailService;
import com.busify.project.common.utils.JwtUtils;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.dto.request.RegisterRequestDTO;
import com.busify.project.user.dto.response.RegisterResponseDTO;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

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
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        Authentication authenticationManager = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager);

        User user = userRepository.findByEmail(loginRequestDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Kiểm tra xác thực email - Bỏ qua cho ADMIN và STAFF
        String roleName = user.getRole().getName();
        boolean isAdminOrStaff = "ADMIN".equals(roleName) || "STAFF".equals(roleName);

        if (!user.isEmailVerified() && !isAdminOrStaff) {
            throw AuthenticationException.emailNotVerified();
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
            throw UserRegistrationException.emailAlreadyExists();
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

    @Override
    public LoginResponseDTO googleSignIn(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Update auth provider if needed
            if (!AuthProvider.GOOGLE.equals(user.getAuthProvider())) {
                user.setAuthProvider(AuthProvider.GOOGLE);
                user.setEmailVerified(true);
            }

            // Generate tokens for existing user
            String accessToken = generateToken(user.getEmail());
            String refreshToken = generateRefreshToken(user.getEmail());
            user.setRefreshToken(refreshToken);

            userRepository.save(user);

            return LoginResponseDTO.builder()
                    .email(user.getEmail())
                    .role(user.getRole().getName())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            // Create new Profile (not User) for Google OAuth
            Profile newUser = new Profile();
            newUser.setEmail(email);
            newUser.setPasswordHash(""); // No password for OAuth users
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            newUser.setEmailVerified(true);

            // Set basic profile info for Google OAuth users
            newUser.setFullName(""); // Will be updated later when user completes profile
            newUser.setPhoneNumber("");
            newUser.setAddress("");

            // Set default role (CUSTOMER)
            Role defaultRole = roleRepository.findByName("CUSTOMER")
                    .orElseThrow(() -> UserRegistrationException.defaultRoleNotFound());
            newUser.setRole(defaultRole);

            // SAVE USER FIRST before generating tokens
            userRepository.save(newUser);

            // Generate tokens for new user AFTER saving
            String accessToken = generateToken(email);
            String refreshToken = generateRefreshToken(email);
            newUser.setRefreshToken(refreshToken);

            // Save again to update with refresh token
            userRepository.save(newUser);

            return LoginResponseDTO.builder()
                    .email(newUser.getEmail())
                    .role(newUser.getRole().getName())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }

    @Override
    public void forgotPassword(ForgotPasswordRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> UserNotFoundException.notFound());

        // Check if user is Profile (not just base User)
        if (!(user instanceof Profile)) {
            throw PasswordResetException.notAvailable();
        }
        Profile profile = (Profile) user;

        String token = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(profile);
        verificationToken.setExpiryDate(LocalDateTime.now().plus(1, ChronoUnit.HOURS));
        verificationToken.setCreatedDate(LocalDateTime.now());
        verificationToken.setTokenType(TokenType.PASSWORD_RESET);

        verificationTokenRepository.save(verificationToken);

        emailService.sendPasswordResetEmail(profile, token);
    }

    @Override
    public void resetPassword(ResetPasswordRequestDTO request) {
        // Get current authenticated user
        VerificationToken token = verificationTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> AuthenticationException.invalidPasswordResetToken());

        User user = userRepository.findByEmail(token.getUser().getEmail())
                .orElseThrow(() -> UserNotFoundException.notFound());

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
