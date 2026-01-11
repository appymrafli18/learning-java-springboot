package com.ecommerce.DTOs.requests;

import com.ecommerce.constants.AppConstants;
import com.ecommerce.constants.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchRequest {

  @Size(min = AppConstants.MIN_NAME_LENGTH, message = "name must be at least 6 characters")
  private String name;

  @Email(message = "email format is invalid")
  private String email;

  @Size(min = AppConstants.MIN_PASSWORD_LENGTH, message = "password must be at least 6 characters")
  private String password;

  private UserRole role;
}
