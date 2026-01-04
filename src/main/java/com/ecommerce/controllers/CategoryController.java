package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.models.Category;
import com.ecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> index() {
        List<Category> categories = categoryService.findAll();

        return ResponseEntity.ok(
                ApiResponse.<List<Category>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Categories Retrieved Successfully")
                        .data(categories)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> create(@RequestBody Category category) {
        Category createdCategory = categoryService.create(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<Category>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Success create category")
                        .data(createdCategory)
                        .build()
        );

    }

}
