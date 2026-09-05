package com.clockstore.Clock_Store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        private final SecurityExceptionHandler securityExceptionHandler;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(SecurityExceptionHandler securityExceptionHandler,
                        JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.securityExceptionHandler = securityExceptionHandler;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> {
                                })
                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(securityExceptionHandler)
                                                .accessDeniedHandler(securityExceptionHandler))
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/auth/verify-email").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/auth/resend-verification").permitAll()
                                                .requestMatchers(HttpMethod.POST,"/api/auth/forgot-password").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}