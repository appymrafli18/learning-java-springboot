package com.ecommerce.DTOs.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "email is not valid")
        String email,
        @NotBlank(message = "password cannot be blank")
        String password) {
}
