package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.CartItemRequest;
import com.ecommerce.DTOs.responses.CartResponse;
import com.ecommerce.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@PathVariable Long userId){

        return ResponseEntity.ok(new ApiResponse<>(
                200,
                "Success get cart",
                cartService.getCartByUserId(userId),
                null
        ));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<ApiResponse<CartResponse>> addItem(@PathVariable Long userId, @RequestBody @Valid CartItemRequest request) {

        return ResponseEntity.ok(new ApiResponse<>(
                200,
                "Success add item to cart",
                cartService.addItemToCart(userId, request),
                null
        ));
    }
}
