package com.ecommerce.DTOs.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO for adding items to shopping cart.
 */
public record CartItemRequest(
  @NotNull(message = "Product ID is required")
  @Positive(message = "Product ID must be greater than 0")
  Long productId,
  @NotNull(message = "Quantity is required")
  @Positive(message = "Quantity must be greater than 0")
  Integer quantity) {
}
