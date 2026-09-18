package com.clockstore.Clock_Store.dto.Response;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import com.clockstore.Clock_Store.entity.enums.CustomerStatus;
import com.clockstore.Clock_Store.entity.enums.Gender;

public record CustomerResponse(
                UUID id,
                String firstName,
                String lastName,
                String email,
                String phone,
                LocalDate birthDate,
                Gender gender,
                String avatar,
                boolean emailVerified,
                CustomerStatus status,
                Instant createdAt,
                Instant updatedAt) {
}