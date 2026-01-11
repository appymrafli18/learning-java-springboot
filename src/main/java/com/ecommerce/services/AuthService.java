package com.ecommerce.services;

import com.ecommerce.DTOs.requests.LoginRequest;
import com.ecommerce.DTOs.requests.RegisterType;
import com.ecommerce.DTOs.responses.JwtUserPayload;
import com.ecommerce.DTOs.responses.RegisterResponse;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.exceptions.DuplicateResourceException;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.entity.User;
import com.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service for handling authentication operations including login and
 * registration.
 */
@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Authenticates a user with email and password.
     *
     * @param request the login credentials
     * @return JWT token if authentication is successful
     * @throws NotFoundException if email not found or password is incorrect
     */
    public String login(LoginRequest request) {
        if (request == null || request.email() == null || request.password() == null) {
            throw new NotFoundException(AppConstants.INVALID_REQUEST);
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Login attempt with non-existent email: {}", request.email());
                    return new NotFoundException(AppConstants.USER_NOT_FOUND_EMAIL + request.email());
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Failed login attempt for user: {}", request.email());
            throw new NotFoundException(AppConstants.INVALID_PASSWORD);
        }

        JwtUserPayload payload = JwtUserPayload.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        log.info("User logged in successfully: {}", user.getEmail());
        return jwtService.generateToken(payload);
    }

    /**
     * Registers a new user with the provided details.
     *
     * @param request the registration details
     * @return registration response with user details (excluding password hash)
     * @throws DuplicateResourceException if email already exists
     * @throws NotFoundException          if passwords do not match
     */
    public RegisterResponse register(RegisterType request) {
        if (request == null || request.email() == null || request.password() == null) {
            throw new NotFoundException(AppConstants.INVALID_REQUEST);
        }

        if (userRepository.existsByEmail(request.email())) {
            log.warn("Registration attempt with existing email: {}", request.email());
            throw new DuplicateResourceException(AppConstants.EMAIL_ALREADY_EXISTS + request.email());
        }

        if (!Objects.equals(request.password(), request.confirmPassword())) {
            log.warn("Password mismatch during registration");
            throw new NotFoundException(AppConstants.PASSWORD_MISMATCH);
        }

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .role(request.role())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(newUser);

        log.info("New user registered: {}", newUser.getEmail());

        return RegisterResponse.builder()
                .name(newUser.getName())
                .email(newUser.getEmail())
                .role(newUser.getRole())
                .build();
    }

}
