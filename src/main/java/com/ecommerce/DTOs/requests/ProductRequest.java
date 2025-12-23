package com.ecommerce.DTOs.requests;


import jakarta.validation.constraints.*;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }
}
