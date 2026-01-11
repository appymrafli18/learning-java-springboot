package com.ecommerce.DTOs.responses;

import com.ecommerce.constants.UserRole;
import lombok.Builder;

import java.util.List;


@Builder
public record UserSpesificResponse(

  Long id,
  String name,
  String email,
  UserRole role,
  String created,
  String updated,
  List<CartItemResponse> carts
) {
}