package com.busify.project.auth.handler;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import com.busify.project.common.config.EmailConfig;
import com.busify.project.common.security.principal.UserPrincipal;
import com.busify.project.common.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
    private final EmailConfig emailConfig;

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
            Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(15 * 60);
            // accessTokenCookie.setAttribute("SameSite", "Strict");
            response.addCookie(accessTokenCookie);

            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
            // refreshTokenCookie.setAttribute("SameSite", "Strict");
            response.addCookie(refreshTokenCookie);

            response.sendRedirect(emailConfig.getFrontendUrl() + "/api/google-callback");

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
            newUser.setPasswordHash(null);
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            newUser.setEmailVerified(true);

            Role defaultRole = roleRepository.findByName("CUSTOMER")
                    .orElseGet(() -> roleRepository.findAll().stream().findFirst().orElse(null));
            newUser.setRole(defaultRole);

            return userRepository.save(newUser);
        }
    }

}
