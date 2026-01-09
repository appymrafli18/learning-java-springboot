package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.responses.CategoryType;
import com.ecommerce.mappers.CategoryMapper;
import com.ecommerce.models.Category;
import com.ecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CategoryType>>> index() {
        List<Category> categories = categoryService.findAll();

        // Cukup panggil method dari mapper
        List<CategoryType> categoriesTypes = categoryMapper.toTypeList(categories);

        return ResponseEntity.ok(
                ApiResponse.<List<CategoryType>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Categories Retrieved Successfully")
                        .data(categoriesTypes)
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
