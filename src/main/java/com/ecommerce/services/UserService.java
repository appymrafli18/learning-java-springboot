package com.ecommerce.services;

import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

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
    User newUser = new User();

    newUser.setName(request.getName());
    newUser.setEmail(request.getEmail());

    // Encode the password before saving
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    newUser.setPassword(encodedPassword);

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
    };


    existingUser.setUpdated(Instant.now());

    return userRepository.save(existingUser);
  }

  public void delete(Long id /* 1 */) {
    User existingUser = this.findById(id);
    userRepository.deleteById(existingUser.getId());
  }

}
