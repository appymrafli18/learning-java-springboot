package com.ecommerce;

import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTests {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  public void testFindAll() {

    Instant created = new Date().toInstant();

    // 1. preparation data
    List<User> mockUsers = new ArrayList<>();
    mockUsers.add(new User(1L, "Joko", "Joko@gmail.com", "joko", created, created));
    mockUsers.add(new User(2L, "Hendro", "Hendro@gmail.com", "hendro", created, created));

    // 2. mock method determine
    when(userRepository.findAll()).thenReturn(mockUsers);

    // 3. call method
    List<User> result = userService.findAll();

    // 4. verification
    assertEquals(2, result.size()); // checking size
    assertEquals("Joko", result.getFirst().getName());
    verify(userRepository, times(1)).findAll(); // checking for one call method
  }

  @Test
  public void testFindById() {

    Instant created = new Date().toInstant();

    // 1. preparation data
    User mockUsers = new User(1L, "Joko", "Joko@gmail.com", "joko", created, created);

    // 2. mock method determine
    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUsers));

    // 3. call method
    User result = userService.findById(1L);

    // 4. verification
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Joko", result.getName());

    // Pastikan repository benar-benar dipanggil dengan ID yang tepat
    verify(userRepository, times(1)).findById(1L);

  }

//  @Test
//  public void testFindByEmail(){
//    Instant created = new Date().toInstant();
//
//    // 1. preparation data
//    User mockUsers = new User(1L, "Joko", "Joko@gmail.com", "joko", created, created);
//
//    // 2. mock method determine
//    when(userRepository.findByEmail("Joko@gmail.com")).thenReturn(Optional.of(mockUsers));
//
//    // 3. call method
//    User result = userService.findByEmail("Joko@gmail.com");
//
//    // 4. verification
//    assertNotNull(result);
//    assertEquals(1L, result.getId());
//    assertEquals("Joko", result.getName());
//    assertThat(result.getEmail()).contains("@gmail.com");
//
//    // Pastikan repository benar-benar dipanggil dengan ID yang tepat
//    verify(userRepository, times(1)).findByEmail("joko@gmail.com");
//  }

  @Test
  void findByEmail_Success() {
    // 1. Given (Persiapan)
    String email = "joko@gmail.com";
    User mockUser = new User(1L, "Joko", email, "pass", Instant.now(), Instant.now());

    // Kita arahkan Mockito untuk membungkus objek dalam Optional.of()
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

    // 2. When (Aksi)
    User result = userService.findByEmail(email);

    // 3. Then (Verifikasi)
    assertNotNull(result);
    assertEquals(email, result.getEmail());
    assertEquals("Joko", result.getName());
    verify(userRepository, times(1)).findByEmail(email);
  }

  @Test
  void findByEmail_NotFound_ShouldThrowException() {
    // 1. Given
    String email = "nyasar@gmail.com";

    // Simulasikan database kosong untuk email ini
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    // 2. When & 3. Then (Gabung menggunakan assertThrows)
    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      userService.findByEmail(email);
    });

    // Verifikasi pesan errornya benar (opsional tapi bagus dilakukan)
    assertEquals("User not found with email " + email, exception.getMessage());
    verify(userRepository, times(1)).findByEmail(email);
  }

}
