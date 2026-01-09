package com.ecommerce.DTOs.responses;

import lombok.Builder;

@Builder
public record CartItemResponse(
        Long id,
        String productName,
        Long productPrice,
        Integer quantity
) {
}
