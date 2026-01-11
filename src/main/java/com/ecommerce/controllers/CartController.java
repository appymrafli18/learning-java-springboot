package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.CartItemRequest;
import com.ecommerce.DTOs.responses.CartResponse;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing shopping cart operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

        private final CartService cartService;

        /**
         * Retrieves a user's cart by user ID.
         *
         * @param userId the user ID
         * @return API response with the cart
         */
        @GetMapping("/{userId}")
        public ResponseEntity<ApiResponse<CartResponse>> getCart(@PathVariable Long userId) {
                log.info("Fetching cart for user: {}", userId);

                return ResponseEntity.ok(ApiResponse.<CartResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(AppConstants.CART_RETRIEVED)
                                .data(cartService.getCartByUserId(userId))
                                .build());
        }

        /**
         * Adds an item to user's cart.
         *
         * @param userId  the user ID
         * @param request the item to add
         * @return API response with updated cart
         */
        @PostMapping("/{userId}/add")
        public ResponseEntity<ApiResponse<CartResponse>> addItem(
                        @PathVariable Long userId,
                        @Valid @RequestBody CartItemRequest request) {
                log.info("Adding item to cart for user: {}", userId);

                return ResponseEntity.ok(ApiResponse.<CartResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(AppConstants.ITEM_ADDED_TO_CART)
                                .data(cartService.addItemToCart(userId, request))
                                .build());
        }

        /**
         * Removes an item from user's cart.
         *
         * @param userId the user ID
         * @param itemId the cart item ID to remove
         * @return API response with updated cart
         */
        @DeleteMapping("/{userId}/remove/{itemId}")
        public ResponseEntity<ApiResponse<CartResponse>> removeItem(
                        @PathVariable Long userId,
                        @PathVariable Long itemId) {
                log.info("Removing item from cart for user: {}", userId);

                return ResponseEntity.ok(ApiResponse.<CartResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(AppConstants.ITEM_REMOVED_FROM_CART)
                                .data(cartService.removeItemFromCart(userId, itemId))
                                .build());
        }

}
