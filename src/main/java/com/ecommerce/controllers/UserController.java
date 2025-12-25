package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.models.User;
import com.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    return ResponseEntity.ok(new ApiResponse<>(200, "Success Get User by id", user));
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<ApiResponse<User>> findByEmail(@PathVariable("email") String email) {
    User user = userService.findByEmail(email);
    return ResponseEntity.ok(new ApiResponse<>(200, "Success Get User by email", user));
  }

  @PostMapping("")
  public ResponseEntity<ApiResponse<User>> create(@Valid @RequestBody UserRequest request) {
    User createdUser = userService.create(request);
    return ResponseEntity.status(201).body(new ApiResponse<>(201, "Success Create User", createdUser));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<User>> update(@PathVariable("id") Long id, @Valid @RequestBody UserPatchRequest request) {
    User updatedUser = userService.update(id, request);
    return ResponseEntity.status(200).body(new ApiResponse<>(200, "Success Update User", updatedUser));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Map<String, Object>>> delete(@PathVariable("id") Long id) {
    userService.delete(id);
    return ResponseEntity.status(200).body(new ApiResponse<>(200, "Success Delete User", null));
  }

}
