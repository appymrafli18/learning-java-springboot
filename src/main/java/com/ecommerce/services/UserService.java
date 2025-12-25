package com.ecommerce.services;

import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
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

  public User create(UserRequest user) {
    User newUser = new User();

    newUser.setName(user.getName());
    newUser.setEmail(user.getEmail());
    newUser.setPassword(user.getPassword());

    return userRepository.save(newUser);
  }

  public User update(Long id, UserPatchRequest request) {
    User existingUser = this.findById(id);

    if (request.getName() != null) existingUser.setName(request.getName());
    if (request.getEmail() != null) existingUser.setEmail(request.getEmail());
    if (request.getPassword() != null) existingUser.setPassword(request.getPassword());

    existingUser.setUpdated(Instant.now());

    return userRepository.save(existingUser);
  }

  public void delete(Long id /* 1 */) {
    User existingUser = this.findById(id);
    userRepository.deleteById(existingUser.getId());
  }

}
