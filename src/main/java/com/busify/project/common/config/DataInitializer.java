package com.busify.project.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.busify.project.role.entity.Role;
import com.busify.project.role.repository.RoleRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@gmail.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User adminUser = new User();
                adminUser.setFullName("Admin");
                adminUser.setEmail(adminEmail);
                adminUser.setPasswordHash(passwordEncoder.encode("admin123"));

                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName("ADMIN");
                            return roleRepository.save(newRole);
                        });
                adminUser.setRole(adminRole);

                userRepository.save(adminUser);

                System.out.print("✅ admin created successfully");
            } else {
                System.out.println("admin already exists, skipping creation");
            }
        };
    }
}
