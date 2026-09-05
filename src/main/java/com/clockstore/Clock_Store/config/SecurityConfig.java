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
                                                .requestMatchers(HttpMethod.POST, "/api/auth/resend-verification")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/auth/forgot-password")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/api/auth/reset-password")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/orders/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/customers/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/cart/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/wishlist/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/addresses/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/payments/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/brands/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/discounts/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/shipping/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/analytics/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/reports/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/settings/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/notifications/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/logs/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/health/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/docs/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/swagger-ui/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v3/api-docs/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/openapi/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }
}