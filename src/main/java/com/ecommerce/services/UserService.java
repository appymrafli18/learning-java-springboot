package com.ecommerce.services;

import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.exceptions.DuplicateCategoryException;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found with email " + email));
    }

    public User create(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) throw new DuplicateCategoryException("Email " + request.getEmail() + " Sudah terdaftar!");

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        return userRepository.save(newUser);
    }

    public User update(Long id, UserPatchRequest request) {
        User existingUser = this.findById(id);

        if (request.getName() != null) existingUser.setName(request.getName());
        if (request.getEmail() != null) existingUser.setEmail(request.getEmail());
        if (request.getPassword() != null) {
            // Encode the password before updating
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            existingUser.setPassword(encodedPassword);
        }
        ;

        return userRepository.save(existingUser);
    }

    public void delete(Long id /* 1 */) {
        if (!userRepository.existsById(id)) throw new NotFoundException("User not found with id" + id);
        userRepository.deleteById(id);
    }

}
