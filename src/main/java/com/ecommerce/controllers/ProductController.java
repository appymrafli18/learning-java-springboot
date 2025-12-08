package com.ecommerce.controllers;

import com.ecommerce.models.Product;
import com.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping
  public Iterable<Product> index() {
    return productService.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable Long id) {
    Product search = productService.findById(id);
    if (search == null) {
      return ResponseEntity.status(404).body("Product not found");
    }

    return ResponseEntity.ok(search);
  }

  @PostMapping
  public Product save(@RequestBody Product product) {
    return productService.create(product);
  }

  @PatchMapping
  public Product update(@RequestBody Product product) {
    return productService.update(product);
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") Long id) {
    productService.delete(id);
    return "Success Delete";
  }

}
