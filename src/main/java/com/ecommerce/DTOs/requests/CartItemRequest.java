package com.ecommerce.DTOs.requests;

import jakarta.validation.constraints.NotNull;

public record CartItemRequest(
        @NotNull(message = "productId cannot be null")
        Long productId,
        @NotNull(message = "quantity cannot be null")
        int quantity) {
}
