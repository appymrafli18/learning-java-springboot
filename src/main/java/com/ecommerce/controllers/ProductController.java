package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.ProductPatchRequest;
import com.ecommerce.DTOs.requests.ProductRequest;
import com.ecommerce.models.Product;
import com.ecommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/product")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<Product>>> index() {
    List<Product> products = productService.findAll();

    return ResponseEntity.ok(new ApiResponse<>(200, "Success Get Products", products));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<Product>> findById(@PathVariable Long id) {
    Product search = productService.findById(id);

    ResponseEntity<ApiResponse<Product>> response;
    if (search == null) {
      response = ResponseEntity.status(404).body(new ApiResponse<>(404, "Product not found", null));
    } else {
      response = ResponseEntity.ok(new ApiResponse<>(200, "Success Selected Product", search));
    }

    return response;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Product>> create(@Valid @RequestBody ProductRequest request) {
    Product productCreate = productService.create(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, "Success Create Product", productCreate));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ApiResponse<Product>> update(@PathVariable("id") Long id, @Valid @RequestBody ProductPatchRequest request) {
    Product updateProduct = productService.update(id, request);

    if (updateProduct == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Product not found", null));
    }

    return ResponseEntity.ok(new ApiResponse<>(200, "Success Update Product", updateProduct));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> delete(@PathVariable("id") Long id) {
    productService.delete(id);
    return ResponseEntity.ok(new ApiResponse<>(200, "Success Delete Product", null));
  }
}
