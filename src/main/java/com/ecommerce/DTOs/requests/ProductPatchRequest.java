package com.ecommerce.DTOs.requests;

import com.ecommerce.constants.AppConstants;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPatchRequest {

  @Size(min = AppConstants.MIN_NAME_LENGTH, message = "name must be at least 6 characters")
  private String name;

  @Positive(message = "price must be greater than 0")
  private Long price;

  @PositiveOrZero(message = "stock must be >= 0")
  private Integer stock;
}
