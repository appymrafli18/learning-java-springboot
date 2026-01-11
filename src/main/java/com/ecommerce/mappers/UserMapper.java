package com.ecommerce.mappers;

import com.ecommerce.DTOs.requests.UserPatchRequest;
import com.ecommerce.DTOs.requests.UserRequest;
import com.ecommerce.DTOs.responses.CartItemResponse;
import com.ecommerce.DTOs.responses.UserResponse;
import com.ecommerce.DTOs.responses.UserSpesificResponse;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

  // mapping to DTO
  UserResponse toDTO(User user);

  // mapping to spesifik DTO
  @Mapping(source = "cart.items", target = "carts")
  UserSpesificResponse spesificToDTO(User user);

  // mapping to entity
  User toEntity(UserRequest request);

  // MapStruct otomatis pakai method ini untuk setiap item di list
  @Mapping(source = "product.id", target = "id")
  @Mapping(source = "product.name", target = "productName")
  @Mapping(source = "product.price", target = "productPrice")
  CartItemResponse toCartItemDTO(CartItem cartItem);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntity(UserPatchRequest request, @MappingTarget User user);

}
