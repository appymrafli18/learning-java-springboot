package com.ecommerce.DTOs.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
  @Schema(
    example = "sofia@gmail.com",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @Email(message = "email is not valid")
  String email,

  @Schema(
    example = "sofia12345",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "password cannot be blank")
  String password) {
}
