package com.ecommerce.dto.requests;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class ProductPatchRequest {

  @Size(min = 6, message = "name must be at least 6 characters")
  private String name;

  @Positive(message = "price must be greater than 0")
  private Long price;

  @PositiveOrZero(message = "stock must be >= 0")
  private Integer stock;

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

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }
}
