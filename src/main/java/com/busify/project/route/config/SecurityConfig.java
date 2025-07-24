package com.busify.project.route.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF (để dễ test bằng Postman)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/popular-routes").permitAll() // Cho phép không cần đăng nhập
                .anyRequest().authenticated() // Những API khác vẫn cần xác thực
            )
            .httpBasic(); // Cho phép sử dụng basic auth nếu cần

        return http.build();
    }
}
