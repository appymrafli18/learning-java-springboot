package com.ecommerce.DTOs.responses;

import com.ecommerce.constants.UserRole;
import lombok.Builder;

@Builder
public record RegisterResponse(String name, String email, UserRole role, String password) {
}
