package com.busify.project.common.security.service;

import com.busify.project.common.security.principal.UserPrincipal;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElse(null);
        
        if (user == null) {
            try {
                user = userRepository.findByEmailIgnoreCase(username).orElse(null);
            } catch (Exception e) {
                // Nếu có lỗi multiple results, lấy user đầu tiên
                List<User> users = userRepository.findAllByEmailIgnoreCase(username);
                user = users.isEmpty() ? null : users.get(0);
            }
        }
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        
        return new UserPrincipal(user);
    }
}
