package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.LoginRequest;
import com.ecommerce.DTOs.requests.RegisterType;
import com.ecommerce.DTOs.responses.RegisterResponse;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.services.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling authentication endpoints (login and
 * registration).
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoint for authentication management e-commerce")
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request  the login credentials
     * @param response the HTTP response to set cookie
     * @return API response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(
      @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        try {
            String token = authService.login(request);

            // Set secure HTTP-only cookie
            ResponseCookie cookie = ResponseCookie
                    .from(AppConstants.COOKIE_ACCESS_TOKEN, token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(15 * 60) // 15 minutes
                    .sameSite("strict")
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            log.info("Login successful for user: {}", request.email());

            return ResponseEntity.ok(ApiResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(AppConstants.LOGIN_SUCCESS)
                    .data(new LoginResponse(token))
                    .build());
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Registers a new user account.
     *
     * @param request the registration details
     * @return API response with registered user details
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterType request) {
        try {
            RegisterResponse registerResponse = authService.register(request);

            log.info("User registered successfully: {}", request.email());

            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<RegisterResponse>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message(AppConstants.REGISTER_SUCCESS)
                    .data(registerResponse)
                    .build());
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Inner class to represent login response with token.
     */
    private record LoginResponse(String token) {
    }

}
