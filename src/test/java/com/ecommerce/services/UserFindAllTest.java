package com.ecommerce.services;

import com.ecommerce.models.User;
import com.ecommerce.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserFindAllTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  /*
   * TODO: Scenario Success
   * */
  @Test
  void case_success() {
    List<User> mockUsers = new ArrayList<User>();
    Instant now = new Date().toInstant();

    for (int i = 1; i <= 3; i++) {
      String name = "hello" + i;
      new User();
      mockUsers.add(
        User.builder().id((long) i).name(name).email(name + "@gmail.com").password(name).created(now).updated(now).build()
      );
    }

    when(userRepository.findAll()).thenReturn(mockUsers);

    List<User> result = userRepository.findAll();

    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals("hello1", result.getFirst().getName());

    verify(userRepository, times(1)).findAll();

  }
}
