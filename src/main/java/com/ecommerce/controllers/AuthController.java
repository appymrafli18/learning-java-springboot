package com.ecommerce.controllers;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.DTOs.requests.LoginRequest;
import com.ecommerce.services.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        return Map.of("token", authService.Login(request));
    }

}
