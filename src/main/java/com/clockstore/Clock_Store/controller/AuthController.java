package com.clockstore.Clock_Store.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clockstore.Clock_Store.dto.request.LoginRequest;
import com.clockstore.Clock_Store.dto.request.RefreshTokenRequest;
import com.clockstore.Clock_Store.dto.request.RegisterRequest;
import com.clockstore.Clock_Store.dto.response.LoginResponse;
import com.clockstore.Clock_Store.dto.response.RefreshTokenResponse;
import com.clockstore.Clock_Store.dto.response.RegisterResponse;
import com.clockstore.Clock_Store.dto.response.SessionResponse;
import com.clockstore.Clock_Store.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(
            @Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        return authService.login(request, httpRequest);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            @Valid @RequestBody RefreshTokenRequest request) {

        authService.logout(request);
    }

    @GetMapping("/sessions")
    public List<SessionResponse> getActiveSessions() {
        return authService.getActiveSessions();
    }
}