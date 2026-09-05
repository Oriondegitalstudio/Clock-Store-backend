package com.clockstore.Clock_Store.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.PasswordResetToken;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByCustomerId(UUID customerId);
}