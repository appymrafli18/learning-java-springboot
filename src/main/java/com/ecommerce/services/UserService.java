package com.ecommerce.services;

import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.DTOs.responses.UserResponse;
import com.ecommerce.DTOs.responses.UserSpesificResponse;
import com.ecommerce.exceptions.DuplicateCategoryException;
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

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public List<UserResponse> findAll() {
    return userRepository.findAll().stream().map(userMapper::toDTO).toList();
  }

  public UserSpesificResponse findById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found with id " + id));
    return userMapper.spesificToDTO(user);
  }

  public UserResponse findByEmail(String email) {
    User user = userRepository.findByEmail(email)
      .orElseThrow(() -> new NotFoundException("User not found with email " + email));

    return userMapper.toDTO(user);
  }

  public User create(UserRequest request) {

    if (userRepository.existsByEmail(request.getEmail()))
      throw new DuplicateCategoryException("Email " + request.getEmail() + " Sudah terdaftar!");

    String encodedPassword = passwordEncoder.encode(request.getPassword());

    User newUser = userMapper.toEntity(request);
    newUser.setPassword(encodedPassword);

    return userRepository.save(newUser);
  }

  public User update(Long id, UserPatchRequest request) {
    User existingUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

    userMapper.updateEntity(request, existingUser);

    if (request.getPassword() != null) {
      existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    return userRepository.save(existingUser);
  }

  public void delete(Long id /* 1 */) {
    if (!userRepository.existsById(id))
      throw new NotFoundException("User not found with id" + id);
    userRepository.deleteById(id);
  }

}
