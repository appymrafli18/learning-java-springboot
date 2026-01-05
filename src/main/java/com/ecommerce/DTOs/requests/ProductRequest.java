package com.ecommerce.DTOs.requests;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

  @NotBlank(message = "name is required")
  @Size(min = 6, message = "name must be at least 6 characters")
  private String name;

  @NotNull(message = "price is required")
  @Positive(message = "price must be greater than 0")
  private Long price;

  @NotNull(message = "stock is required")
  // @Min(value = 0, message = "Stock must be >= 0")
  @Positive(message = "Stock must be greater than 0")
  private int stock;

  @Positive(message = "categoryId must be greater than 0")
  @NotNull(message = "categoryId is required")
  private Long categoryId;

  private MultipartFile image;

}
