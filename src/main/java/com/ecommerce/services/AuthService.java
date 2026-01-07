package com.ecommerce.services;

import com.ecommerce.DTOs.requests.RegisterType;
import com.ecommerce.DTOs.responses.JwtUserPayload;
import com.ecommerce.DTOs.responses.RegisterResponse;
import com.ecommerce.exceptions.DuplicateCategoryException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.DTOs.requests.LoginRequest;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;

import java.util.Objects;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String Login(LoginRequest request) throws NotFoundException {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("email is not registrated"));

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new NotFoundException("Password is not correct");

        // set cookie
        JwtUserPayload payload = JwtUserPayload.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).role(user.getRole()).build();

        return jwtService.generateToken(payload);
    }

    public RegisterResponse register(RegisterType request) throws DuplicateCategoryException, NotFoundException{


        if (userRepository.existsByEmail(request.email())) throw new DuplicateCategoryException("This email already exist");

        if (!Objects.equals(request.password(), request.confirmPassword())) throw new NotFoundException("Passwords are not equal");

        User newUser = User.builder()
                .name(request.name())
                .email(request.email())
                .role(request.role())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(newUser);

        return RegisterResponse.builder()
                .name(newUser.getName())
                .email(newUser.getEmail())
                .role(newUser.getRole())
                .password(newUser.getPassword())
                .build();

    }

}
