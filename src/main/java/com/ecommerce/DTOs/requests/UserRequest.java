package com.ecommerce.DTOs.requests;

import com.ecommerce.constants.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

  @NotBlank(message = "name is required")
  @Size(min = 6, message = "name must be at least 6 characters")
  private String name;

  @NotBlank(message = "email is required")
  @Email(message = "email format is not valid")
  private String email;

  @NotBlank(message = "role is required")
  private UserRole role;

  @NotBlank(message = "password is required")
  @Size(min = 6, message = "password must be at least 6 characters")
  private String password;
}
