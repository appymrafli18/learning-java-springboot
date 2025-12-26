package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.models.Category;
import com.ecommerce.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<Category>>> index() {
    return ResponseEntity.ok(new ApiResponse<>(200, "Categories retrieved successfully", categoryService.findAll()));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<Category>> create(@RequestBody Category category) {
    Category createdCategory = categoryService.create(category);
    ApiResponse<Category> response = new ApiResponse<>(200, "Category created successfully", createdCategory);
    return ResponseEntity.ok(response);
  }

}
