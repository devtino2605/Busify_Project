package com.busify.project.auth.service;

import com.busify.project.auth.enums.AuthProvider;
import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oauth2User = super.loadUser(userRequest);

            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");

            // Process user - create or update
            processOAuth2User(email, name);

            return oauth2User;
        } catch (Exception e) {
            // Log the error and throw a more specific exception
            System.err.println("OAuth2 User loading failed: " + e.getMessage());
            throw new OAuth2AuthenticationException("OAuth2 authentication failed: " + e.getMessage());
        }
    }

    private User processOAuth2User(String email, String name) {
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Update auth provider if needed
            if (!user.getAuthProvider().equals(AuthProvider.GOOGLE)) {
                user.setAuthProvider(AuthProvider.GOOGLE);
                user.setEmailVerified(true);
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

            // Set default role
            Role defaultRole = roleRepository.findByName("CUSTOMER")
                    .orElseGet(() -> roleRepository.findAll().stream().findFirst().orElse(null));
            newUser.setRole(defaultRole);

            return userRepository.save(newUser);
        }
    }
}
