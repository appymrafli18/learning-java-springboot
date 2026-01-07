package com.ecommerce.DTOs.responses;

import com.ecommerce.constants.UserRole;
import lombok.Builder;

@Builder
public record JwtUserPayload(Long id, String name, String email, UserRole role) {

}
