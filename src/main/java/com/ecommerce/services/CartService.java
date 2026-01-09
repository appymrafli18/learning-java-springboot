package com.ecommerce.services;

import com.ecommerce.DTOs.requests.CartItemRequest;
import com.ecommerce.DTOs.responses.CartItemResponse;
import com.ecommerce.DTOs.responses.CartResponse;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.mappers.CartMapper;
import com.ecommerce.models.Cart;
import com.ecommerce.models.CartItem;
import com.ecommerce.models.Product;
import com.ecommerce.models.User;
import com.ecommerce.repositories.CartRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CartService {


    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartResponse getCartByUserId(Long userId) throws NotFoundException {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));

        return cartMapper.toDTO(cart);
    }

    public CartResponse addItemToCart(Long userId, CartItemRequest request) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        Product product = productRepository.findById(request.productId()).orElseThrow(() -> new NotFoundException("Product not found!"));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            // log.info("New cart created. CartId={}, UserId={}", newCart.getId(), userId);
            return cartRepository.save(newCart);
        });

        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(request.productId())).findFirst().orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
        } else {
            CartItem cartItem = CartItem.builder().cart(cart).product(product).quantity(request.quantity()).build();
            cart.getItems().add(cartItem);
        }

        Cart saveCart = cartRepository.save(cart);

        return cartMapper.toDTO(saveCart);
    }

    public CartResponse removeItemFromCart(Long userId, Long itemId){
        return null;
    }


}
