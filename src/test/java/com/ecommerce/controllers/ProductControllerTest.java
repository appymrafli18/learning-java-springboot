package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.ProductPatchRequest;
import com.ecommerce.DTOs.requests.ProductRequest;
import com.ecommerce.models.Product;
import com.ecommerce.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController Unit Tests")
class ProductControllerTest {

  @Mock
  private ProductService productService;

  @InjectMocks
  private ProductController productController;

  private Product laptop;
  private Product phone;
  private ProductRequest createRequest;
  private ProductPatchRequest patchRequest;

  @BeforeEach
  void setUp() {
    laptop = Product.builder()
      .id(1L)
      .name("MacBook Pro 16")
      .price((long) 2499.99)
      .stock(15)
      .build();

    phone = Product.builder()
      .id(2L)
      .name("iPhone 15 Pro")
      .price((long) 1299.00)
      .stock(30)
      .build();

    createRequest = new ProductRequest();
    createRequest.setName("Wireless Mouse");
    createRequest.setPrice((long) 29.99);
    createRequest.setStock(100);

    patchRequest = new ProductPatchRequest();
    patchRequest.setName("Updated Mouse");
    patchRequest.setStock(85);
  }

  @Nested
  @DisplayName("GET /api/product")
  class GetAllProducts {

    @Test
    @DisplayName("should return list of products when products exist")
    void shouldReturnAllProducts() {
      given(productService.findAll()).willReturn(List.of(laptop, phone));

      ResponseEntity<ApiResponse<List<Product>>> response = productController.index();

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getStatusCode()).isEqualTo(200);
      assertThat(response.getBody().getMessage()).isEqualTo("Products Retrieved Successfully");
      assertThat(response.getBody().getData()).hasSize(2);
      assertThat(response.getBody().getData()).containsExactlyInAnyOrder(laptop, phone);

      verify(productService).findAll();
    }

    @Test
    @DisplayName("should return empty list when no products exist")
    void shouldReturnEmptyListWhenNoProducts() {
      given(productService.findAll()).willReturn(List.of());

      ResponseEntity<ApiResponse<List<Product>>> response = productController.index();

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody().getData()).isEmpty();
    }
  }

  @Nested
  @DisplayName("GET /api/product/{id}")
  class GetProductById {

    @Test
    @DisplayName("should return product when found by id")
    void shouldReturnProductWhenFound() {
      given(productService.findById(1L)).willReturn(laptop);

      ResponseEntity<ApiResponse<Product>> response = productController.findById(1L);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody().getStatusCode()).isEqualTo(200);
      assertThat(response.getBody().getMessage()).isEqualTo("Products Retrieved Successfully");
      assertThat(response.getBody().getData()).isEqualTo(laptop);

      verify(productService).findById(1L);
    }
  }

  @Nested
  @DisplayName("POST /api/product")
  class CreateProduct {

    @Test
    @DisplayName("should create product and return 201 Created")
    void shouldCreateProductSuccessfully() {
      Product createdProduct = Product.builder()
        .id(100L)
        .name(createRequest.getName())
        .price(createRequest.getPrice())
        .stock(createRequest.getStock())
        .build();

      given(productService.create(any(ProductRequest.class))).willReturn(createdProduct);

      ResponseEntity<ApiResponse<Product>> response = productController.create(createRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getStatusCode()).isEqualTo(201);
      assertThat(response.getBody().getMessage()).isEqualTo("Success create product");
      assertThat(response.getBody().getData().getId()).isEqualTo(100L);
      assertThat(response.getBody().getData().getName()).isEqualTo("Wireless Mouse");

      verify(productService).create(createRequest);
    }
  }

  @Nested
  @DisplayName("PATCH /api/product/{id}")
  class UpdateProduct {

    @Test
    @DisplayName("should update product fields successfully")
    void shouldUpdateProductSuccessfully() {
      Product updatedProduct = Product.builder()
        .id(1L)
        .name("Updated Mouse")
        .price(laptop.getPrice())
        .stock(85)
        .build();

      given(productService.update(eq(1L), any(ProductPatchRequest.class)))
        .willReturn(updatedProduct);

      ResponseEntity<ApiResponse<Product>> response = productController.update(1L, patchRequest);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody().getStatusCode()).isEqualTo(200);
      assertThat(response.getBody().getMessage()).isEqualTo("Products Update Successfully");
      assertThat(response.getBody().getData().getName()).isEqualTo("Updated Mouse");
      assertThat(response.getBody().getData().getStock()).isEqualTo(85);

      verify(productService).update(1L, patchRequest);
    }
  }

  @Nested
  @DisplayName("DELETE /api/product/{id}")
  class DeleteProduct {

    @Test
    @DisplayName("should delete product and return 200 OK with no data")
    void shouldDeleteProductSuccessfully() {
      doNothing().when(productService).delete(5L);

      ResponseEntity<ApiResponse<Void>> response = productController.delete(5L);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getStatusCode()).isEqualTo(200);
      assertThat(response.getBody().getMessage()).isEqualTo("Products Update Successfully");
      // Note: message is inconsistent with action ("Update" vs "Delete")
      // consider fixing in controller to "Product Deleted Successfully"

      verify(productService).delete(5L);
    }
  }
}