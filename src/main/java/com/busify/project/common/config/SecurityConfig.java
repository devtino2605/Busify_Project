// package com.busify.project.common.config;

// import com.busify.project.auth.handler.OAuth2LoginFailureHandler;
// import com.busify.project.auth.handler.OAuth2LoginSuccessHandler;
// import com.busify.project.auth.service.CustomOAuth2UserService;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import com.busify.project.common.security.JwtAuthenticationFilter;
// import lombok.RequiredArgsConstructor;

// /**
//  * Simple Security Config - Chỉ dùng roles với URL patterns
//  * Đơn giản, dễ hiểu và dễ quản lý
//  */
// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize cho flexibility
// @RequiredArgsConstructor
// public class SecurityConfig {
    
//     private final JwtAuthenticationFilter jwtAuthenticationFilter;
//     private final CustomOAuth2UserService customOAuth2UserService;
//     private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//     private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .cors(cors -> {})
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 // ========== PUBLIC ENDPOINTS ==========
//                 .requestMatchers(
//                     // Auth endpoints
//                     "/api/auth/login", "/api/auth/register", "/api/auth/verify-email",
//                     "/api/auth/resend-verification", "/api/auth/forgot-password", "/api/auth/reset-password",
                    
//                     // Public viewing endpoints (no auth needed)
//                     "/api/trips/public/**", "/api/routes/public/**", "/api/bus-operators/public/**",
//                     "/api/reviews/public/**",
                    
//                     // Documentation
//                     "/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**",
                    
//                     // OAuth2
//                     "/oauth2/**", "/login/oauth2/**"
//                 ).permitAll()
                
//                 // ========== ADMIN ONLY ==========
//                 .requestMatchers(
//                     "/api/admin/**",           // Admin panel
//                     "/api/users/admin/**",     // User management by admin
//                     "/api/roles/**",           // Role management  
//                     "/api/reports/admin/**",   // Admin reports
//                     "/api/system/**"           // System management
//                 ).hasRole("ADMIN")
                
//                 // ========== BUS OPERATOR ==========
//                 .requestMatchers(
//                     "/api/trips/operator/**",      // Operator trip management
//                     "/api/buses/**",               // Bus management
//                     "/api/seat-layouts/**",        // Seat layout management
//                     "/api/employees/**",           // Employee management
//                     "/api/bookings/operator/**",   // Operator booking management
//                     "/api/reports/operator/**"     // Operator reports
//                 ).hasAnyRole("ADMIN", "BUS_OPERATOR")
                
//                 // ========== STAFF (Customer Service) ==========
//                 .requestMatchers(
//                     "/api/bookings/staff/**",      // Staff booking support
//                     "/api/tickets/staff/**",       // Ticket validation/support
//                     "/api/complaints/staff/**",    // Handle complaints
//                     "/api/reviews/staff/**",       // Review management
//                     "/api/support/**"              // Customer support tools
//                 ).hasAnyRole("ADMIN", "STAFF")
                
//                 // ========== CUSTOMER ==========
//                 .requestMatchers(
//                     "/api/bookings/customer/**",   // Customer booking management
//                     "/api/tickets/customer/**",    // Customer ticket viewing
//                     "/api/payments/**",            // Payment processing
//                     "/api/reviews/customer/**",    // Customer reviews
//                     "/api/complaints/customer/**"  // Customer complaints
//                 ).hasAnyRole("ADMIN", "CUSTOMER")
                
//                 // ========== AUTHENTICATED (All logged in users) ==========
//                 .requestMatchers(
//                     "/api/auth/logout",            // Logout
//                     "/api/profile/**",             // User profile management
//                     "/api/notifications/**",       // Notifications
//                     "/api/trips/search",           // Trip search (for all users)
//                     "/api/routes/search"           // Route search (for all users)
//                 ).authenticated()
                
//                 // ========== FALLBACK ==========
//                 .anyRequest().authenticated()
//             )
//             .sessionManagement(management -> management
//                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .oauth2Login(oauth2 -> oauth2
//                 .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
//                 .successHandler(oAuth2LoginSuccessHandler)
//                 .failureHandler(oAuth2LoginFailureHandler))
//             .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
//         return http.build();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//         return http.getSharedObject(AuthenticationManagerBuilder.class).build();
//     }
// }


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
                        .failureHandler(oAuth2LoginFailureHandler))
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
