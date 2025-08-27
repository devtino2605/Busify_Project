package com.busify.project.common.config;

import com.busify.project.auth.handler.OAuth2LoginFailureHandler;
import com.busify.project.auth.handler.OAuth2LoginSuccessHandler;
import com.busify.project.auth.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.busify.project.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {
                }) // Bật CORS để Spring Security sử dụng CorsConfig
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/trips/**", "/api/routes/**",
                                "/api/bus-operators/**", "/api/reviews/**",
                                "/api/complaints/**", "swagger-ui.html",
                                "/v3/api-docs/**", "/swagger-ui/**",
                                "/api/auth/login", "/api/auth/register",
                                "/api/auth/verify-email",
                                "/api/auth/resend-verification",
                                "/api/auth/login/google", "/oauth2/**",
                                "/login/oauth2/**")
                        .permitAll()
                        .requestMatchers("/api/auth/logout").authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}