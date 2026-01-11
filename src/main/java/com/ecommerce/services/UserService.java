package com.ecommerce.services;

import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.DTOs.responses.UserResponse;
import com.ecommerce.DTOs.responses.UserSpesificResponse;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.exceptions.DuplicateResourceException;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.mappers.UserMapper;
import com.ecommerce.entity.User;
import com.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling user-related operations.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  /**
   * Retrieves all users.
   *
   * @return list of all users
   */
  public List<UserResponse> findAll() {
    log.debug("Fetching all users");
    return userRepository.findAll().stream()
        .map(userMapper::toDTO)
        .toList();
  }

  /**
   * Retrieves a user by ID.
   *
   * @param id the user ID
   * @return user details
   * @throws NotFoundException if user not found
   */
  public UserSpesificResponse findById(Long id) {
    if (id == null || id <= 0) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    User user = userRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("User not found with id: {}", id);
          return new NotFoundException(AppConstants.USER_NOT_FOUND + id);
        });
    return userMapper.spesificToDTO(user);
  }

  /**
   * Retrieves a user by email.
   *
   * @param email the user email
   * @return user details
   * @throws NotFoundException if user not found
   */
  public UserResponse findByEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> {
          log.warn("User not found with email: {}", email);
          return new NotFoundException(AppConstants.USER_NOT_FOUND_EMAIL + email);
        });

    return userMapper.toDTO(user);
  }

  /**
   * Creates a new user.
   *
   * @param request the user creation request
   * @return the created user
   * @throws DuplicateResourceException if email already exists
   */
  public User create(UserRequest request) {
    if (request == null || request.getEmail() == null) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      log.warn("Duplicate email during user creation: {}", request.getEmail());
      throw new DuplicateResourceException(AppConstants.EMAIL_ALREADY_EXISTS + request.getEmail());
    }

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User newUser = userMapper.toEntity(request);
    newUser.setPassword(encodedPassword);

    userRepository.save(newUser);
    log.info("User created successfully: {}", request.getEmail());

    return newUser;
  }

  /**
   * Updates an existing user.
   *
   * @param id      the user ID
   * @param request the update request
   * @return the updated user
   * @throws NotFoundException if user not found
   */
  public User update(Long id, UserPatchRequest request) {
    if (id == null || id <= 0) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    User existingUser = userRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("User not found with id: {}", id);
          return new NotFoundException(AppConstants.USER_NOT_FOUND + id);
        });

    userMapper.updateEntity(request, existingUser);

    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    userRepository.save(existingUser);
    log.info("User updated successfully: {}", id);

    return existingUser;
  }

  /**
   * Deletes a user.
   *
   * @param id the user ID
   * @throws NotFoundException if user not found
   */
  public void delete(Long id) {
    if (id == null || id <= 0) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    if (!userRepository.existsById(id)) {
      log.warn("User not found for deletion with id: {}", id);
      throw new NotFoundException(AppConstants.USER_NOT_FOUND + id);
    }

    userRepository.deleteById(id);
    log.info("User deleted successfully: {}", id);
  }

}
