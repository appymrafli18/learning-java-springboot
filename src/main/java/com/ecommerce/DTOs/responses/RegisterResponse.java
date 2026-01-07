package com.ecommerce.DTOs.responses;

import com.ecommerce.constants.UserRole;
import jdk.jshell.Snippet;
import lombok.Builder;

@Builder
public record RegisterResponse(String name, String email, UserRole role, String password) {
}
