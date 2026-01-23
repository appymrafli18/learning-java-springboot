package com.ecommerce.services;

import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.exceptions.DuplicateCategoryException;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  /*
   * TODO CASE: (findAll) success
   * */
  @Test
  void findAll_success() {
    List<User> users = new ArrayList<User>();
    Instant justNow = new Date().toInstant();

    for (int i = 1; i < 5; i++) {
      String name = "hello" + i;
      new User();

      users.add(
        User.builder().id((long) i).name(name).email(name + "@gmail.com").password(name).created(justNow).updated(justNow).build()
      );
    }


    // check calling method findAll -> return users
    when(userRepository.findAll()).thenReturn(users);

    List<User> result = userService.findAll();

    assertNotNull(result);
    assertEquals("hello1", result.getFirst().getName());

    verify(userRepository, times(1)).findAll();

  }

  /*
   * TODO CASE: (findById) success + not found
   * */
  @Test
  void findById_success() {
    Instant justNow = new Date().toInstant();
    User user1 = User.builder().id(1L).name("hello1").email("hello1@gmail.com").password("hello1").created(justNow).updated(justNow).build();

    // check method
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

    User result = userService.findById(1L);

    assertNotNull(result);
    assertEquals("hello1@gmail.com", result.getEmail());

    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  void findById_not_found() {
    Long id = 99L;
    when(userRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.findById(id));

    verify(userRepository, times(1)).findById(id);
  }


  /*
   * TODO CASE: (findByEmail) success + not found
   * */
  @Test
  void findByEmail_success() {
    Instant justNow = new Date().toInstant();
    String emails = "hello1@gmail.com";
    User user1 = User.builder().id(1L).name("hello1").email("hello1@gmail.com").password("hello1").created(justNow).updated(justNow).build();

    when(userRepository.findByEmail(emails)).thenReturn(Optional.of(user1));

    User result = userService.findByEmail(emails);

    assertNotNull(result);
    assertEquals("hello1@gmail.com", result.getEmail());

    verify(userRepository, times(1)).findByEmail(emails);
  }

  @Test
  void findByEmail_not_found() {
    String emails = "hello1@gmail.com";

    when(userRepository.findByEmail(emails)).thenReturn(Optional.empty());

    // test error
    assertThrows(NotFoundException.class, () -> userService.findByEmail(emails));

    verify(userRepository, times(1)).findByEmail(emails);
  }


  /*
   * TODO CASE: (create) success + duplicate email
   * */
  @Test
  void create_success() {
    UserRequest request = new UserRequest();
    request.setName("joko");
    request.setEmail("joko@gmail.com");
    request.setPassword("joko");

    when(userRepository.existsByEmail("joko@gmail.com")).thenReturn(false);
    when(passwordEncoder.encode("joko")).thenReturn("encoded");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User user = userService.create(request);

    assertEquals("joko", user.getName());
    assertEquals("encoded", user.getPassword());

    verify(passwordEncoder).encode("joko");
    verify(userRepository).save(any(User.class));
  }

  @Test
  void create_duplicate_email() {
    UserRequest request = new UserRequest();
    request.setName("joko");
    request.setEmail("joko@gmail.com");
    request.setPassword("joko");


    when(userRepository.existsByEmail("joko@gmail.com")).thenReturn(true);

    DuplicateCategoryException exception = assertThrows(DuplicateCategoryException.class, () -> userService.create(request));

    assertEquals("Email " + request.getEmail() + " Sudah terdaftar!", exception.getMessage());

    verify(userRepository, never()).save(any());
  }


  /*
   * TODO CASE: (update) success + duplicate email + not found
   * */
  @Test
  void update_success() {
    // Arrange
    Instant justNow = new Date().toInstant();
    String emails = "hello1@gmail.com";
    User existingUser = User.builder()
      .id(1L)
      .name("hello1")
      .email(emails)
      .password("hello1")
      .created(justNow)
      .updated(justNow)
      .build();

    UserPatchRequest request = new UserPatchRequest();
    request.setName("new-data");

    when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // action
    User result = userService.update(1L, request);

    // assert
    assertEquals("new-data", result.getName());
    assertEquals("hello1@gmail.com", result.getEmail());
    assertEquals("hello1", result.getPassword());

    // verify
    verify(userRepository, times(1)).findById(1L);
    verify(userRepository, times(1)).save(existingUser);
    verify(passwordEncoder, never()).encode(any());

  }

  @Test
  void update_duplicate_email() {
    // Arrange
    Instant justNow = new Date().toInstant();
    Long id = 1L;
    String emails = "hello1@gmail.com";
    User existingUser = User.builder()
      .id(id)
      .name("hello1")
      .email(emails)
      .password("hello1")
      .created(justNow)
      .updated(justNow)
      .build();

    UserPatchRequest request = new UserPatchRequest();
    request.setEmail("duplicate@gmail.com");

    // user found
    when(userRepository.findById(id)).thenReturn(Optional.ofNullable(existingUser));

    // email is defined by another user
    when(userRepository.existsByEmail("duplicate@gmail.com")).thenReturn(true);

    // action + assert
    BadRequestException exception = assertThrows(
      BadRequestException.class,
      () -> userService.update(id, request)
    );

    assertEquals("Email already exists", exception.getMessage());

    // verify
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void update_not_found() {
    Long id = 10L;

    when(userRepository.findById(id)).thenReturn(Optional.empty());

    // test error
    NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findById(id));

    assertEquals("User not found with id " + id, exception.getMessage());

    verify(userRepository, times(1)).findById(id);
    verify(userRepository, never()).save(any(User.class));
    verify(passwordEncoder, never()).encode(any());
  }


  /*
   * TODO CASE: (delete) success + not found
   * */
  @Test
  void delete_success() {

    // Arrange
    Instant justNow = new Date().toInstant();
    Long id = 1L;

    when(userRepository.existsById(id)).thenReturn(true);

    assertDoesNotThrow(() -> userService.delete(id));

    verify(userRepository, times(1)).existsById(id);
    verify(userRepository, times(1)).deleteById(id);

  }

  @Test
  void delete_not_found() {
    Long userId = 99L;

    when(userRepository.existsById(userId)).thenReturn(false);

    NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.delete(userId));

    assertEquals("User not found with id" + userId, exception.getMessage());

    verify(userRepository, times(1)).existsById(userId);
    verify(userRepository, never()).deleteById(userId);
  }

}
