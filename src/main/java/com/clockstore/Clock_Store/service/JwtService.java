package com.clockstore.Clock_Store.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.clockstore.Clock_Store.entity.Customer;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final long rememberMeExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
            @Value("${jwt.remember-me-expiration}") long rememberMeExpiration) {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));

        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.rememberMeExpiration = rememberMeExpiration;
    }

    public String generateAccessToken(Customer customer) {
        return generateToken(customer, accessTokenExpiration, "access");
    }
    
    public String generateRefreshToken(Customer customer) {
        return generateToken(
                customer,
                refreshTokenExpiration,
                "refresh");
    }

    public String generateRefreshToken(Customer customer, boolean rememberMe) {
        long expiration = rememberMe ? rememberMeExpiration : refreshTokenExpiration;
        return generateToken(customer, expiration, "refresh");
    }

    private String generateToken(
            Customer customer,
            long expiration,
            String tokenType) {

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(customer.getId().toString())
                .claim("email", customer.getEmail())
                .claim("type", tokenType)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractClaims(token);
        return "refresh".equals(claims.get("type", String.class));
    }

    public UUID extractCustomerId(String token) {
        Claims claims = extractClaims(token);
        return UUID.fromString(claims.getSubject());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }
    
    public boolean isAccessToken(String token) {
        Claims claims = extractClaims(token);
        return "access".equals(claims.get("type", String.class));
    }
}