package com.ecommerce.services;

import com.ecommerce.DTOs.requests.CartItemRequest;
import com.ecommerce.DTOs.responses.CartResponse;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.mappers.CartMapper;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repositories.CartRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for managing shopping cart operations.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    /**
     * Retrieves cart for a specific user.
     *
     * @param userId the user ID
     * @return the user's cart
     * @throws NotFoundException if cart or user not found
     */
    public CartResponse getCartByUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new NotFoundException(AppConstants.INVALID_REQUEST);
        }

        // Verify user exists
        if (!userRepository.existsById(userId)) {
            log.warn("User not found for cart retrieval: {}", userId);
            throw new NotFoundException(AppConstants.USER_NOT_FOUND + userId);
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Cart not found for user: {}", userId);
                    return new NotFoundException(AppConstants.CART_NOT_FOUND);
                });

        return cartMapper.toDTO(cart);
    }

    /**
     * Adds an item to user's cart. Creates a new cart if it doesn't exist.
     *
     * @param userId  the user ID
     * @param request the item to add
     * @return updated cart
     * @throws NotFoundException if user or product not found
     */
    public CartResponse addItemToCart(Long userId, CartItemRequest request) {
        if (userId == null || userId <= 0) {
            throw new NotFoundException(AppConstants.INVALID_REQUEST);
        }

        if (request == null || request.productId() == null || request.quantity() == null || request.quantity() <= 0) {
            throw new NotFoundException(AppConstants.INVALID_REQUEST);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for cart operation: {}", userId);
                    return new NotFoundException(AppConstants.USER_NOT_FOUND + userId);
                });

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> {
                    log.warn("Product not found: {}", request.productId());
                    return new NotFoundException(AppConstants.PRODUCT_NOT_FOUND + request.productId());
                });

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        // Check if product already in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(request.productId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
            log.debug("Updated cart item quantity for product: {}", request.productId());
        } else {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.quantity())
                    .build();
            cart.getItems().add(cartItem);
            log.debug("Added new item to cart for product: {}", request.productId());
        }

        Cart savedCart = cartRepository.save(cart);
        log.info("Item added to cart for user: {}", userId);

        return cartMapper.toDTO(savedCart);
    }

    /**
     * Removes an item from user's cart.
     *
     * @param userId the user ID
     * @param itemId the cart item ID to remove
     * @return updated cart
     * @throws NotFoundException if cart item not found
     */
    public CartResponse removeItemFromCart(Long userId, Long itemId) {
        if (userId == null || userId <= 0 || itemId == null || itemId <= 0) {
            throw new NotFoundException(AppConstants.INVALID_REQUEST);
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("Cart not found for user: {}", userId);
                    return new NotFoundException(AppConstants.CART_NOT_FOUND);
                });

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Cart item not found: {}", itemId);
                    return new NotFoundException(AppConstants.CART_ITEM_NOT_FOUND);
                });

        cart.getItems().remove(itemToRemove);
        Cart savedCart = cartRepository.save(cart);

        log.info("Item removed from cart for user: {}", userId);
        return cartMapper.toDTO(savedCart);
    }

}
