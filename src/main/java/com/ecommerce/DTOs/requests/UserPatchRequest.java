package com.ecommerce.DTOs.requests;

import com.ecommerce.constants.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchRequest {

  @Size(min = 6, message = "name must be at least 6 characters")
  private String name;

  @Email(message = "email format is invalid")
  private String email;

  @Size(min = 6, message = "password must be at least 6 characters")
  private String password;

  private UserRole role;
}
