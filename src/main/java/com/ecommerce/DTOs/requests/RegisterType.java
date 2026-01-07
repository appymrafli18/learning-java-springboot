package com.ecommerce.DTOs.requests;


import com.ecommerce.constants.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterType(
        @NotBlank(message = "name is required")
        @Size(min = 6, message = "name must be at least 6 characters")
        String name,

        @NotBlank(message = "email is required")
        @Email(message = "email format is not valid")
        String email,

        @NotNull(message = "role is required")
        UserRole role,

        @NotBlank(message = "password is required")
        @Size(min = 6, message = "password must be at least 6 characters")
        String password,

        @NotBlank(message = "password is required")
        @Size(min = 6, message = "password must be at least 6 characters")
        String confirmPassword) {
}
