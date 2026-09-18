package com.clockstore.Clock_Store.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clockstore.Clock_Store.dto.Request.EmailVerificationRequest;
import com.clockstore.Clock_Store.dto.Request.ForgotPasswordRequest;
import com.clockstore.Clock_Store.dto.Request.LoginRequest;
import com.clockstore.Clock_Store.dto.Request.RefreshTokenRequest;
import com.clockstore.Clock_Store.dto.Request.RegisterRequest;
import com.clockstore.Clock_Store.dto.Request.ResendVerificationRequest;
import com.clockstore.Clock_Store.dto.Response.CustomerResponse;
import com.clockstore.Clock_Store.dto.Response.EmailVerificationResponse;
import com.clockstore.Clock_Store.dto.Response.ForgotPasswordResponse;
import com.clockstore.Clock_Store.dto.Response.LoginResponse;
import com.clockstore.Clock_Store.dto.Response.RefreshTokenResponse;
import com.clockstore.Clock_Store.dto.Response.RegisterResponse;
import com.clockstore.Clock_Store.dto.Response.SessionResponse;
import com.clockstore.Clock_Store.entity.Customer;
import com.clockstore.Clock_Store.entity.CustomerSession;
import com.clockstore.Clock_Store.entity.EmailVerificationToken;
import com.clockstore.Clock_Store.entity.PasswordResetToken;
import com.clockstore.Clock_Store.entity.RefreshToken;
import com.clockstore.Clock_Store.entity.enums.CustomerStatus;
import com.clockstore.Clock_Store.exception.ConflictException;
import com.clockstore.Clock_Store.exception.ForbiddenException;
import com.clockstore.Clock_Store.exception.NotFoundException;
import com.clockstore.Clock_Store.exception.UnauthorizedException;
import com.clockstore.Clock_Store.repository.CustomerRepository;
import com.clockstore.Clock_Store.repository.CustomerSessionRepository;
import com.clockstore.Clock_Store.repository.EmailVerificationTokenRepository;
import com.clockstore.Clock_Store.repository.PasswordResetTokenRepository;
import com.clockstore.Clock_Store.repository.RefreshTokenRepository;

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
        private final PasswordResetTokenRepository passwordResetTokenRepository;
        @Value("${jwt.password-reset-expiration}")
        private long passwordResetExpiration;

        public AuthService(
                        CustomerRepository customerRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService,
                        RefreshTokenRepository refreshTokenRepository,
                        CustomerSessionRepository customerSessionRepository,
                        EmailVerificationTokenRepository emailVerificationTokenRepository,
                        PasswordResetTokenRepository passwordResetTokenRepository) {

                this.customerRepository = customerRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtService = jwtService;
                this.refreshTokenRepository = refreshTokenRepository;
                this.customerSessionRepository = customerSessionRepository;
                this.emailVerificationTokenRepository = emailVerificationTokenRepository;
                this.passwordResetTokenRepository = passwordResetTokenRepository;
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
                String verificationToken = UUID.randomUUID().toString();

                EmailVerificationToken verificationTokenEntity = EmailVerificationToken.builder()
                                .token(verificationToken)
                                .customer(savedCustomer)
                                .expiresAt(
                                                Instant.now()
                                                                .plusMillis(emailVerificationExpiration))
                                .build();

                emailVerificationTokenRepository.save(verificationTokenEntity);
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

        public EmailVerificationResponse verifyEmail(
                        EmailVerificationRequest request) {

                EmailVerificationToken verificationToken = emailVerificationTokenRepository
                                .findByToken(request.token())
                                .orElseThrow(() -> new UnauthorizedException(
                                                "Invalid verification token"));

                if (verificationToken.isUsed()) {
                        throw new UnauthorizedException(
                                        "Verification token has already been used");
                }

                if (verificationToken.getExpiresAt().isBefore(Instant.now())) {
                        throw new UnauthorizedException(
                                        "Verification token has expired");
                }

                Customer customer = verificationToken.getCustomer();

                if (customer.isEmailVerified()) {
                        throw new ConflictException(
                                        "Email is already verified");
                }

                customer.setEmailVerified(true);
                customerRepository.save(customer);

                verificationToken.setUsed(true);
                emailVerificationTokenRepository.save(verificationToken);

                return new EmailVerificationResponse(
                                "Email verified successfully");
        }

        public void resendVerificationEmail(ResendVerificationRequest request) {
                String email = request.email().trim().toLowerCase();
                Customer customer = customerRepository.findByEmail(email)
                                .orElseThrow(() -> new NotFoundException("Customer not found"));
                if (customer.isEmailVerified()) {
                        throw new ConflictException("Email is already verified");
                }
                emailVerificationTokenRepository.findByCustomerId(customer.getId())
                                .ifPresent(existingToken -> emailVerificationTokenRepository.delete(existingToken));
                String verificationToken = UUID.randomUUID().toString();
                EmailVerificationToken newToken = EmailVerificationToken.builder()
                                .token(verificationToken)
                                .customer(customer)
                                .expiresAt(Instant.now().plusMillis(emailVerificationExpiration))
                                .build();
                emailVerificationTokenRepository.save(newToken);
        }

        public ForgotPasswordResponse forgotPassword(
                        ForgotPasswordRequest request) {

                String email = request.email()
                                .trim()
                                .toLowerCase();

                Optional<Customer> customerOptional = customerRepository.findByEmail(email);

                if (customerOptional.isEmpty()) {
                        return new ForgotPasswordResponse(
                                        "If the email exists, a password reset link has been sent");
                }

                Customer customer = customerOptional.get();

                passwordResetTokenRepository
                                .findByCustomerId(customer.getId())
                                .ifPresent(existingToken -> passwordResetTokenRepository.delete(existingToken));

                String resetToken = UUID.randomUUID().toString();

                PasswordResetToken resetTokenEntity = PasswordResetToken.builder()
                                .token(resetToken)
                                .customer(customer)
                                .expiresAt(
                                                Instant.now()
                                                                .plusMillis(
                                                                                passwordResetExpiration))
                                .build();

                passwordResetTokenRepository.save(resetTokenEntity);

                return new ForgotPasswordResponse(
                                "If the email exists, a password reset link has been sent");
        }
}