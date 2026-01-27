package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.responses.CategoryType;
import com.ecommerce.DTOs.responses.ProductType;
import com.ecommerce.mappers.CategoryMapper;
import com.ecommerce.models.Category;
import com.ecommerce.models.Product;
import com.ecommerce.services.CategoryService;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryController Unit Tests")
class CategoryControllerTest {

  @Mock
  private CategoryService categoryService;

  @Mock
  private CategoryMapper categoryMapper;

  @InjectMocks
  private CategoryController categoryController;

  private Category electronics;
  private Category books;
  private CategoryType electronicsType;
  private CategoryType booksType;

  @BeforeEach
  void setUp() {
    // Sample domain entities
    Product laptop = Product.builder()
      .id(101L)
      .name("MacBook Pro")
      .price(2500000L)   // sesuai ProductType → Long
      .stock(12)
      .build();

    Product novel = Product.builder()
      .id(201L)
      .name("Clean Code")
      .price(450000L)
      .stock(45)
      .build();

    electronics = Category.builder()
      .id(1L)
      .name("Electronics")
      .products(List.of(laptop))
      .build();

    books = Category.builder()
      .id(2L)
      .name("Books")
      .products(List.of(novel))
      .build();

    // Corresponding DTOs (record)
    electronicsType = new CategoryType(
      1L,
      "Electronics",
      List.of(new ProductType(101L, "MacBook Pro", 2500000L, 12))
    );

    booksType = new CategoryType(
      2L,
      "Books",
      List.of(new ProductType(201L, "Clean Code", 450000L, 45))
    );
  }

  @Nested
  @DisplayName("GET /api/categories")
  class GetAllCategories {

    @Test
    @DisplayName("should return CategoryType list with nested ProductType (price as Long)")
    void shouldReturnMappedCategoryTypes() {
      // given
      List<Category> categories = List.of(electronics, books);
      List<CategoryType> expected = List.of(electronicsType, booksType);

      given(categoryService.findAll()).willReturn(categories);
      given(categoryMapper.toTypeList(categories)).willReturn(expected);

      // when
      ResponseEntity<ApiResponse<List<CategoryType>>> response = categoryController.index();

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getStatusCode()).isEqualTo(200);
      assertThat(response.getBody().getMessage()).isEqualTo("Categories Retrieved Successfully");

      List<CategoryType> data = response.getBody().getData();
      assertThat(data).hasSize(2);

      // Verify first category + product
      CategoryType cat1 = data.getFirst();
      assertThat(cat1.id()).isEqualTo(1L);
      assertThat(cat1.name()).isEqualTo("Electronics");
      assertThat(cat1.products()).hasSize(1);

      ProductType prod1 = cat1.products().getFirst();
      assertThat(prod1.id()).isEqualTo(101L);
      assertThat(prod1.name()).isEqualTo("MacBook Pro");
      assertThat(prod1.price()).isEqualTo(2500000L);
      assertThat(prod1.stock()).isEqualTo(12);

      verify(categoryService).findAll();
      verify(categoryMapper).toTypeList(categories);
    }

    @Test
    @DisplayName("should return empty list when no categories")
    void shouldReturnEmptyList() {
      given(categoryService.findAll()).willReturn(List.of());
      given(categoryMapper.toTypeList(List.of())).willReturn(List.of());

      ResponseEntity<ApiResponse<List<CategoryType>>> response = categoryController.index();

      // Make null-safety explicit
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getData()).isEmpty();

      // Optional: also check status & message for extra confidence
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(response.getBody().getStatusCode()).isEqualTo(200);
      assertThat(response.getBody().getMessage()).isEqualTo("Categories Retrieved Successfully");
    }

    @Test
    @DisplayName("should handle category with empty products list")
    void shouldHandleEmptyProducts() {
      Category emptyCat = Category.builder()
        .id(3L)
        .name("Miscellaneous")
        .products(List.of())
        .build();

      CategoryType emptyType = new CategoryType(3L, "Miscellaneous", List.of());

      given(categoryService.findAll()).willReturn(List.of(emptyCat));
      given(categoryMapper.toTypeList(List.of(emptyCat))).willReturn(List.of(emptyType));

      ResponseEntity<ApiResponse<List<CategoryType>>> response = categoryController.index();

      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getData())
        .hasSize(1)
        .first()
        .satisfies(cat -> assertThat(cat.products()).isEmpty());
    }
  }

  @Nested
  @DisplayName("POST /api/categories")
  class CreateCategory {

    @Test
    @DisplayName("should create and return new Category entity")
    void shouldCreateCategory() {
      Category input = Category.builder()
        .name("Stationery")
        .build();

      Category created = Category.builder()
        .id(300L)
        .name("Stationery")
        .products(List.of())
        .build();

      given(categoryService.create(any(Category.class))).willReturn(created);

      ResponseEntity<ApiResponse<Category>> response = categoryController.create(input);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(response.getBody()).isNotNull();
      assertThat(response.getBody().getStatusCode()).isEqualTo(201);
      assertThat(response.getBody().getMessage()).isEqualTo("Success create category");
      assertThat(response.getBody().getData().getId()).isEqualTo(300L);

      verify(categoryService).create(input);
    }
  }
}