package com.busify.project.auth.handler;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.common.security.principal.UserPrincipal;
import com.busify.project.common.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        try {
            User user = processOAuth2User(email, name);

            // Generate JWT tokens
            UserPrincipal userPrincipal = new UserPrincipal(user);
            String accessToken = jwtUtils.generateToken(userPrincipal);
            String refreshToken = jwtUtils.generateRefreshToken(userPrincipal);

            // Update refresh token in database
            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            // Create response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("access_token", accessToken);
            responseData.put("refresh_token", refreshToken);
            responseData.put("user", createUserResponse(user));
            responseData.put("message", "Google login successful");

            // Set response headers
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Write response
            response.getWriter().write(objectMapper.writeValueAsString(responseData));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication failed: " + e.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }

    private User processOAuth2User(String email, String name) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Update auth provider if needed
            if (!user.getAuthProvider().equals(AuthProvider.GOOGLE)) {
                user.setAuthProvider(AuthProvider.GOOGLE);
                user.setEmailVerified(true); // Google accounts are pre-verified
                return userRepository.save(user);
            }
            return user;
        } else {
            // Create new user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPasswordHash(""); // No password for OAuth users
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            newUser.setEmailVerified(true);

            // Set default role (assuming you have a default role)
            Role defaultRole = roleRepository.findByName("CUSTOMER")
                    .orElseGet(() -> roleRepository.findAll().stream().findFirst().orElse(null));
            newUser.setRole(defaultRole);

            return userRepository.save(newUser);
        }
    }

    private Map<String, Object> createUserResponse(User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("email", user.getEmail());
        userResponse.put("emailVerified", user.isEmailVerified());
        userResponse.put("authProvider", user.getAuthProvider().toString());
        if (user.getRole() != null) {
            userResponse.put("role", user.getRole().getName());
        }
        return userResponse;
    }
}
