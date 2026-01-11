package com.ecommerce.DTOs.responses;

import com.ecommerce.constants.UserRole;
import lombok.Builder;

@Builder
public record UserResponse(

  Long id,
  String name,
  String email,
  UserRole role,
  String created,
  String updated

) {
}
