package com.ecommerce.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.DTOs.requests.LoginRequest;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String Login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("email is not registrated"));

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new NotFoundException("Password is not correct");

        // generate JWT token
        return jwtService.generateToken(user);
    }

}
