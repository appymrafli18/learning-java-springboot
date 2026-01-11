package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.DTOs.responses.UserResponse;
import com.ecommerce.DTOs.responses.UserSpesificResponse;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.entity.User;
import com.ecommerce.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        /**
         * Retrieves all users.
         *
         * @return API response with list of all users
         */
        @GetMapping
        public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
                log.info("Fetching all users");
                List<UserResponse> users = userService.findAll();

                return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(AppConstants.USER_RETRIEVED)
                                .data(users)
                                .build());
        }

        /**
         * Retrieves a user by ID.
         *
         * @param id the user ID
         * @return API response with user details
         */
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<UserSpesificResponse>> findById(@PathVariable Long id) {
                log.info("Fetching user with id: {}", id);
                UserSpesificResponse user = userService.findById(id);

                return ResponseEntity.ok(ApiResponse.<UserSpesificResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(AppConstants.USER_RETRIEVED)
                                .data(user)
                                .build());
        }

        /**
         * Retrieves a user by email.
         *
         * @param email the user email
         * @return API response with user details
         */
        @GetMapping("/email/{email}")
        public ResponseEntity<ApiResponse<UserResponse>> findByEmail(@PathVariable String email) {
                log.info("Fetching user with email: {}", email);
                UserResponse user = userService.findByEmail(email);

                return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(AppConstants.USER_RETRIEVED)
                                .data(user)
                                .build());
        }

        /**
         * Creates a new user.
         *
         * @param request the user creation request
         * @return API response with the created user
         */
        @PostMapping
        public ResponseEntity<ApiResponse<User>> create(@Valid @RequestBody UserRequest request) {
                log.info("Creating new user: {}", request.getEmail());
                User createdUser = userService.create(request);

                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<User>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .message(AppConstants.USER_CREATED)
                                .data(createdUser)
                                .build());
        }

        /**
         * Updates an existing user.
         *
         * @param id      the user ID
         * @param request the update request
         * @return API response with the updated user
         */
        @PatchMapping("/{id}")
        public ResponseEntity<ApiResponse<User>> update(
                        @PathVariable Long id,
                        @Valid @RequestBody UserPatchRequest request) {
                log.info("Updating user with id: {}", id);
                User updatedUser = userService.update(id, request);

                return ResponseEntity.ok(ApiResponse.<User>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(AppConstants.USER_UPDATED)
                                .data(updatedUser)
                                .build());
        }

        /**
         * Deletes a user.
         *
         * @param id the user ID
         * @return API response indicating successful deletion
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
                log.info("Deleting user with id: {}", id);
                userService.delete(id);

                return ResponseEntity.ok(ApiResponse.<Void>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(AppConstants.USER_DELETED)
                                .data(null)
                                .build());
        }

}
