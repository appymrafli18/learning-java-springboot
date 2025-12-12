package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.requests.ProductPatchRequest;
import com.ecommerce.dto.requests.ProductRequest;
import com.ecommerce.models.Product;
import com.ecommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.Error;
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
  public ResponseEntity<ApiResponse<Iterable<Product>>> index() {
    Iterable<Product> products = productService.findAll();
    ApiResponse<Iterable<Product>> response = new ApiResponse<>(200, "Success Get All Products", products);
    return ResponseEntity.ok(response);
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
  public Product create(@Valid @RequestBody ProductRequest req) {
    return productService.create(req);
  }

  @PatchMapping("/{id}")
  public Product update(@PathVariable("id") Long id, @Valid @RequestBody ProductPatchRequest req) {
    return productService.update(id, req);
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") Long id) {
    productService.delete(id);
    return "Success Delete";
  }

}
