package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.RegisterType;
import com.ecommerce.DTOs.responses.RegisterResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.DTOs.requests.LoginRequest;
import com.ecommerce.services.AuthService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {

        String login = authService.Login(request);

        // set cookie
        ResponseCookie cookie = ResponseCookie.from("accessToken", "TOKEN_AJA_INI_MAH_MUAHAHAA")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(15 * 60)
                .sameSite("strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        log.info("Created cookie: {}", cookie);

        Map<String, Object> res = new HashMap<>();
        res.put("statusCode", 200); // Set the HTTP status code (e.g., 200 for OK)
        res.put("message", "Success Login"); // Set a custom message or any additional information you want to include in the res")
        res.put("token", login);

        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterType request) {
        RegisterResponse register = authService.register(request);

        ApiResponse<RegisterResponse> response = ApiResponse.
                <RegisterResponse>builder()
                .statusCode(201)
                .message("Success register")
                .data(register).build();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
