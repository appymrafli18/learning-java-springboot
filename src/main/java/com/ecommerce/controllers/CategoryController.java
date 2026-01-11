package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.responses.CategoryType;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.mappers.CategoryMapper;
import com.ecommerce.entity.Category;
import com.ecommerce.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing categories.
 */
@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    /**
     * Retrieves all categories.
     *
     * @return API response with list of categories
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryType>>> index() {
        log.info("Fetching all categories");
        List<Category> categories = categoryService.findAll();
        List<CategoryType> categoriesTypes = categoryMapper.toTypeList(categories);

        return ResponseEntity.ok(ApiResponse.<List<CategoryType>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(AppConstants.CATEGORY_RETRIEVED)
                .data(categoriesTypes)
                .build());
    }

    /**
     * Creates a new category.
     *
     * @param category the category to create
     * @return API response with the created category
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Category>> create(@Valid @RequestBody Category category) {
        log.info("Creating new category: {}", category.getName());
        Category createdCategory = categoryService.create(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Category>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(AppConstants.CATEGORY_CREATED)
                .data(createdCategory)
                .build());
    }

}
