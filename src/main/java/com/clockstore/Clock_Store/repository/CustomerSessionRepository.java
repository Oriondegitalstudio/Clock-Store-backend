package com.clockstore.Clock_Store.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clockstore.Clock_Store.entity.CustomerSession;

public interface CustomerSessionRepository extends JpaRepository<CustomerSession, UUID> {

    List<CustomerSession> findByCustomerIdAndRevokedFalse(UUID customerId);

    void deleteByCustomerId(UUID customerId);

    Optional<CustomerSession> findByRefreshTokenId(UUID refreshTokenId);
}