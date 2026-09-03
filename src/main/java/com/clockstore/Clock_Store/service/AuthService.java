package com.clockstore.Clock_Store.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clockstore.Clock_Store.dto.request.LoginRequest;
import com.clockstore.Clock_Store.dto.request.RefreshTokenRequest;
import com.clockstore.Clock_Store.dto.request.RegisterRequest;
import com.clockstore.Clock_Store.dto.response.CustomerResponse;
import com.clockstore.Clock_Store.dto.response.LoginResponse;
import com.clockstore.Clock_Store.dto.response.RefreshTokenResponse;
import com.clockstore.Clock_Store.dto.response.RegisterResponse;
import com.clockstore.Clock_Store.dto.response.SessionResponse;
import com.clockstore.Clock_Store.entity.Customer;
import com.clockstore.Clock_Store.entity.CustomerSession;
import com.clockstore.Clock_Store.entity.EmailVerificationToken;
import com.clockstore.Clock_Store.entity.RefreshToken;
import com.clockstore.Clock_Store.entity.enums.CustomerStatus;
import com.clockstore.Clock_Store.exception.ConflictException;
import com.clockstore.Clock_Store.exception.ForbiddenException;
import com.clockstore.Clock_Store.exception.NotFoundException;
import com.clockstore.Clock_Store.exception.UnauthorizedException;
import com.clockstore.Clock_Store.repository.CustomerRepository;
import com.clockstore.Clock_Store.repository.CustomerSessionRepository;
import com.clockstore.Clock_Store.repository.EmailVerificationTokenRepository;
import com.clockstore.Clock_Store.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

        private final CustomerRepository customerRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final RefreshTokenRepository refreshTokenRepository;
        private final CustomerSessionRepository customerSessionRepository;
        private final EmailVerificationTokenRepository emailVerificationTokenRepository;
        @Value("${jwt.email-verification-expiration}")
        private long emailVerificationExpiration;
        public AuthService(
                        CustomerRepository customerRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService,
                        RefreshTokenRepository refreshTokenRepository,
                        CustomerSessionRepository customerSessionRepository,
                        EmailVerificationTokenRepository emailVerificationTokenRepository) {

                this.customerRepository = customerRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtService = jwtService;
                this.refreshTokenRepository = refreshTokenRepository;
                this.customerSessionRepository = customerSessionRepository;
                this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        }

        public RegisterResponse register(RegisterRequest request) {

                String email = request.email()
                                .trim()
                                .toLowerCase();

                // Email already exists
                if (customerRepository.existsByEmail(email)) {
                        throw new ConflictException(
                                        "This email is already registered");
                }

                Customer customer = Customer.builder()
                                .firstName(request.firstName().trim())
                                .lastName(request.lastName().trim())
                                .email(email)
                                .password(passwordEncoder.encode(request.password()))
                                .emailVerified(false)
                                .status(CustomerStatus.ACTIVE)
                                .build();

                Customer savedCustomer = customerRepository.save(customer);

                return new RegisterResponse(
                                new CustomerResponse(
                                                savedCustomer.getId(),
                                                savedCustomer.getFirstName(),
                                                savedCustomer.getLastName(),
                                                savedCustomer.getEmail(),
                                                savedCustomer.getPhone(),
                                                savedCustomer.getBirthDate(),
                                                savedCustomer.getGender(),
                                                savedCustomer.getAvatar(),
                                                savedCustomer.isEmailVerified(),
                                                savedCustomer.getStatus(),
                                                savedCustomer.getCreatedAt(),
                                                savedCustomer.getUpdatedAt()));
        }

        public LoginResponse login(
                        LoginRequest request,
                        HttpServletRequest httpRequest) {

                String email = request.email()
                                .trim()
                                .toLowerCase();

                // Wrong email
                Customer customer = customerRepository.findByEmail(email)
                                .orElseThrow(() -> new UnauthorizedException(
                                                "Invalid email or password"));

                // Wrong password
                if (!passwordEncoder.matches(
                                request.password(),
                                customer.getPassword())) {

                        throw new UnauthorizedException(
                                        "Invalid email or password");
                }

                // Inactive account
                if (customer.getStatus() != CustomerStatus.ACTIVE) {

                        throw new ForbiddenException(
                                        "Customer account is not active");
                }

                CustomerResponse customerResponse = new CustomerResponse(
                                customer.getId(),
                                customer.getFirstName(),
                                customer.getLastName(),
                                customer.getEmail(),
                                customer.getPhone(),
                                customer.getBirthDate(),
                                customer.getGender(),
                                customer.getAvatar(),
                                customer.isEmailVerified(),
                                customer.getStatus(),
                                customer.getCreatedAt(),
                                customer.getUpdatedAt());

                String accessToken = jwtService.generateAccessToken(customer);

                String refreshToken = jwtService.generateRefreshToken(customer, request.rememberMe());

                RefreshToken refreshTokenEntity = RefreshToken.builder()
                                .token(refreshToken)
                                .customer(customer)
                                .expiresAt(jwtService.extractExpiration(refreshToken).toInstant())
                                .rememberMe(request.rememberMe())
                                .build();

                refreshTokenRepository.save(refreshTokenEntity);

                // Create a new session for the customer
                CustomerSession session = CustomerSession.builder()
                                .customer(customer)
                                .refreshToken(refreshTokenEntity)
                                .ipAddress(httpRequest.getRemoteAddr())
                                .userAgent(httpRequest.getHeader("User-Agent"))
                                .expiresAt(refreshTokenEntity.getExpiresAt())
                                .build();

                customerSessionRepository.save(session);

                String verificationToken = UUID.randomUUID().toString();

                EmailVerificationToken verificationTokenEntity = EmailVerificationToken.builder()
                                .token(verificationToken)
                                .customer(customer)
                                .expiresAt(Instant.now().plusMillis(emailVerificationExpiration))
                                .build();

                emailVerificationTokenRepository.save(verificationTokenEntity);

                return new LoginResponse(
                                customerResponse,
                                accessToken,
                                refreshToken);
        }

        public RefreshTokenResponse refreshToken(
                        RefreshTokenRequest request) {

                String refreshToken = request.refreshToken();

                // Wrong token type
                if (!jwtService.isRefreshToken(refreshToken)) {
                        throw new UnauthorizedException(
                                        "Invalid or expired refresh token");
                }

                RefreshToken storedToken = refreshTokenRepository
                                .findByToken(refreshToken)
                                .orElseThrow(() -> new UnauthorizedException(
                                                "Invalid or expired refresh token"));

                // Revoked token
                if (storedToken.isRevoked()) {
                        throw new UnauthorizedException(
                                        "Refresh token has been revoked");
                }

                UUID customerId = jwtService.extractCustomerId(refreshToken);

                // Customer doesn't exist
                Customer customer = customerRepository
                                .findById(customerId)
                                .orElseThrow(() -> new NotFoundException(
                                                "Customer not found"));

                // Inactive account
                if (customer.getStatus() != CustomerStatus.ACTIVE) {
                        throw new ForbiddenException(
                                        "Customer account is not active");
                }

                boolean rememberMe = storedToken.isRememberMe();

                String newAccessToken = jwtService.generateAccessToken(customer);

                String newRefreshToken = jwtService.generateRefreshToken(
                                customer,
                                rememberMe);

                // Revoke old refresh token
                storedToken.setRevoked(true);
                refreshTokenRepository.save(storedToken);

                // Create new refresh token
                RefreshToken newRefreshTokenEntity = RefreshToken.builder()
                                .token(newRefreshToken)
                                .customer(customer)
                                .expiresAt(jwtService.extractExpiration(newRefreshToken).toInstant())
                                .rememberMe(rememberMe)
                                .build();

                refreshTokenRepository.save(newRefreshTokenEntity);

                // Update customer session
                CustomerSession session = customerSessionRepository
                                .findByRefreshTokenId(storedToken.getId())
                                .orElseThrow(() -> new NotFoundException(
                                                "Customer session not found"));

                session.setRefreshToken(newRefreshTokenEntity);
                session.setLastActivityAt(java.time.Instant.now());
                session.setExpiresAt(
                                newRefreshTokenEntity.getExpiresAt());

                customerSessionRepository.save(session);

                return new RefreshTokenResponse(
                                newAccessToken,
                                newRefreshToken);
        }

        public void logout(RefreshTokenRequest request) {

                String refreshToken = request.refreshToken();

                RefreshToken storedToken = refreshTokenRepository
                                .findByToken(refreshToken)
                                .orElseThrow(() -> new UnauthorizedException(
                                                "Invalid refresh token"));

                if (storedToken.isRevoked()) {
                        throw new UnauthorizedException(
                                        "Refresh token has already been revoked");
                }

                UUID authenticatedCustomerId = UUID.fromString(
                                SecurityContextHolder.getContext()
                                                .getAuthentication()
                                                .getName());

                if (!storedToken.getCustomer().getId()
                                .equals(authenticatedCustomerId)) {

                        throw new ForbiddenException(
                                        "You cannot revoke this refresh token");
                }

                storedToken.setRevoked(true);

                refreshTokenRepository.save(storedToken);
        }

        public List<SessionResponse> getActiveSessions() {

                UUID customerId = UUID.fromString(
                                SecurityContextHolder.getContext()
                                                .getAuthentication()
                                                .getName());

                return customerSessionRepository
                                .findByCustomerIdAndRevokedFalse(customerId)
                                .stream()
                                .map(session -> new SessionResponse(
                                                session.getId(),
                                                session.getCreatedAt(),
                                                session.getExpiresAt(),
                                                session.isRevoked()))
                                .toList();
        }
}