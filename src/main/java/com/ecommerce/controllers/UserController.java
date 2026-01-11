package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.DTOs.responses.UserResponse;
import com.ecommerce.DTOs.responses.UserSpesificResponse;
import com.ecommerce.entity.User;
import com.ecommerce.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
        List<UserResponse> users = userService.findAll();

        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Get Users")
                .data(users)
                .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserSpesificResponse>> findById(@PathVariable("id") Long id) {
        UserSpesificResponse user = userService.findById(id);

        return ResponseEntity.ok(ApiResponse.<UserSpesificResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Get User By Id")
                .data(user)
                .build()
        );
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserResponse>> findByEmail(@PathVariable("email") String email) {
        UserResponse user = userService.findByEmail(email);

        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Get User by email")
                .data(user)
                .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> create(@Valid @RequestBody UserRequest request) {
        User createdUser = userService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<User>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Success Create User")
                        .data(createdUser)
                        .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> update(@PathVariable("id") Long id, @Valid @RequestBody UserPatchRequest request) {
        User updatedUser = userService.update(id, request);

        return ResponseEntity.ok(ApiResponse.<User>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Update User")
                .data(updatedUser)
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        userService.delete(id);

        return ResponseEntity.ok(ApiResponse.<User>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Success Delete User")
                .data(null)
                .build()
        );
    }

}
