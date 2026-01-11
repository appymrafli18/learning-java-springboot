package com.ecommerce.mappers;

import com.ecommerce.DTOs.responses.CartItemResponse;
import com.ecommerce.DTOs.responses.CartResponse;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Agar bisa di-@Autowired di Controller
public interface CartMapper {

    // Mapping dari satu object
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "items", target = "items")
    CartResponse toDTO(Cart cart);

    // Mapping ke DTO dengan nama yang berbeda
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    CartItemResponse toItemDTO(CartItem item);
}
