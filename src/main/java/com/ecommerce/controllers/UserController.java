package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.models.User;
import com.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("")
  public ResponseEntity<ApiResponse<List<User>>> findAll() {
    List<User> users = userService.findAll();

    return ResponseEntity.ok(new ApiResponse<>(200, "Success Get Users", users));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<User>> findById(@PathVariable("id") Long id) {
    User user = userService.findById(id);

    return ResponseEntity.ok(new ApiResponse<>(200, "Success Get User", user));
  }

  @PostMapping("")
  public ResponseEntity<ApiResponse<User>> create(@Valid @RequestBody UserRequest request) {
    User createdUser = userService.create(request);
    return ResponseEntity.status(201).body(new ApiResponse<>(201, "Success Create User", createdUser));
  }

}
