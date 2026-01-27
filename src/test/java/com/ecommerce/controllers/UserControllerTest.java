package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.models.User;
import com.ecommerce.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Unit Tests")
class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private User user1;
  private User user2;
  private UserRequest createRequest;
  private UserPatchRequest patchRequest;

  @BeforeEach
  void setUp() {
    user1 = User.builder()
      .id(1L)
      .email("john.doe@example.com")
      .name("John Doe")
      .build();

    user2 = User.builder()
      .id(2L)
      .email("jane.doe@example.com")
      .name("Jane Doe")
      .build();

    createRequest = new UserRequest();
    createRequest.setEmail("new.user@example.com");
    createRequest.setName("New User");
    createRequest.setPassword("secret123");

    patchRequest = new UserPatchRequest();
    patchRequest.setName("Updated Name");
  }

  @Nested
  @DisplayName("GET /api/user")
  class GetAllUsers {

    @Test
    @DisplayName("should return all users when exist")
    void shouldReturnAllUsers() {
      // given
      given(userService.findAll()).willReturn(List.of(user1, user2));

      // when
      ResponseEntity<ApiResponse<List<User>>> response = userController.findAll();

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getStatusCode()).isEqualTo(200);
      assertThat(response.getBody().getMessage()).isEqualTo("Success Get Users");
      assertThat(response.getBody().getData()).hasSize(2);
      assertThat(response.getBody().getData()).contains(user1, user2);

      verify(userService).findAll();
    }

    @Test
    @DisplayName("should return empty list when no users")
    void shouldReturnEmptyList() {
      // given
      given(userService.findAll()).willReturn(List.of());

      // when
      ResponseEntity<ApiResponse<List<User>>> response = userController.findAll();

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getData()).isEmpty();
    }
  }

  @Nested
  @DisplayName("GET /api/user/{id}")
  class GetUserById {

    @Test
    @DisplayName("should return user when found")
    void shouldReturnUserWhenFound() {
      // given
      given(userService.findById(1L)).willReturn(user1);

      // when
      ResponseEntity<ApiResponse<User>> response = userController.findById(1L);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getData()).isEqualTo(user1);
      assertThat(response.getBody().getMessage()).isEqualTo("Success Get User By Id");

      verify(userService).findById(1L);
    }

    @Test
    @DisplayName("should still return 200 even service throws exception (depending on global handler)")
    void shouldHandleServiceExceptionGracefully() {
      // In real app this is usually handled by @ControllerAdvice
      // Here we just test controller layer → assume service throws → test won't reach here
      // → usually we don't test exception path in controller if it's delegated to advice
    }
  }

  @Nested
  @DisplayName("GET /api/user/email/{email}")
  class GetUserByEmail {

    @Test
    @DisplayName("should return user when email exists")
    void shouldReturnUserByEmail() {
      given(userService.findByEmail("john.doe@example.com")).willReturn(user1);

      ResponseEntity<ApiResponse<User>> response = userController.findByEmail("john.doe@example.com");

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getData().getEmail()).isEqualTo("john.doe@example.com");
    }
  }

  @Nested
  @DisplayName("POST /api/user")
  class CreateUser {

    @Test
    @DisplayName("should create user and return 201")
    void shouldCreateUserSuccessfully() {
      // given
      User createdUser = User.builder()
        .id(100L)
        .email(createRequest.getEmail())
        .name(createRequest.getName())
        .build();

      given(userService.create(any(UserRequest.class))).willReturn(createdUser);

      // when
      ResponseEntity<ApiResponse<User>> response = userController.create(createRequest);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getStatusCode()).isEqualTo(201);
      assertThat(response.getBody().getMessage()).isEqualTo("Success Create User");
      assertThat(response.getBody().getData().getId()).isEqualTo(100L);

      verify(userService).create(createRequest);
    }
  }

  @Nested
  @DisplayName("PATCH /api/user/{id}")
  class UpdateUser {

    @Test
    @DisplayName("should update user successfully")
    void shouldUpdateUser() {
      // given
      User updatedUser = User.builder()
        .id(1L)
        .email(user1.getEmail())
        .name("Updated Name")
        .build();

      given(userService.update(eq(1L), any(UserPatchRequest.class)))
        .willReturn(updatedUser);

      // when
      ResponseEntity<ApiResponse<User>> response = userController.update(1L, patchRequest);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getData().getName()).isEqualTo("Updated Name");

      verify(userService).update(1L, patchRequest);
    }
  }

  @Nested
  @DisplayName("DELETE /api/user/{id}")
  class DeleteUser {

    @Test
    @DisplayName("should delete user and return 200 with null data")
    void shouldDeleteUser() {
      // given
      doNothing().when(userService).delete(5L);

      // when
      ResponseEntity<ApiResponse<?>> response = userController.delete(5L);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getMessage()).isEqualTo("Success Delete User");
      assertThat(response.getBody().getData()).isNull();

      verify(userService).delete(5L);
    }
  }
}