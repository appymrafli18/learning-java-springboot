package com.ecommerce.services;

import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserUpdateTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Mock
  private PasswordEncoder passwordEncoder;

  /*
   * TODO: Skenario User Tidak Ditemukan
   * */
  @Test
  void update_UserNotFound_ShouldThrowException() {
    // Given
    Long id = 99L;
    when(userRepository.findById(id)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(NotFoundException.class, () -> userService.update(id, new UserPatchRequest()));
    verify(userRepository, never()).save(any()); // Pastikan data tidak pernah tersimpan
  }

  /*
   * TODO: Skenario Sukses: Update Parsial (Nama Saja)
   * */
  @Test
  void update_OnlyName_ShouldNotEncodePassword() {
    // Given
    Long id = 1L;
    User existingUser = new User(id, "Lama", "lama@mail.com", "pass-lama", null, null);
    UserPatchRequest request = new UserPatchRequest();
    request.setName("Baru"); // Hanya set nama

    when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

    // When
    User result = userService.update(id, request);

    // Then
    assertEquals("Baru", result.getName());
    assertEquals("pass-lama", result.getPassword()); // Password tetap yang lama
    verify(passwordEncoder, never()).encode(anyString()); // Verifikasi encoder TIDAK dipanggil
  }

  /*
   * TODO: Skenario Sukses: Update Password
   * */
  @Test
  void update_Password_ShouldBeEncoded() {
    // Given
    Long id = 1L;
    User existingUser = new User(id, "Joko", "joko@mail.com", "old-pass", null, null);
    UserPatchRequest request = new UserPatchRequest();
    request.setPassword("new-secret");

    when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
    when(passwordEncoder.encode("new-secret")).thenReturn("encoded-secret-123");
    when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

    // When
    User result = userService.update(id, request);

    // Then
    assertEquals("encoded-secret-123", result.getPassword());
    verify(passwordEncoder, times(1)).encode("new-secret"); // Pastikan di-encode 1x
  }
}
