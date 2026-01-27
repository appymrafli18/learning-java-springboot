package com.ecommerce.services;

import com.ecommerce.exceptions.DuplicateCategoryException;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.Category;
import com.ecommerce.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  private Category category;

  @BeforeEach
  void setUp() {
    category = new Category();
    category.setId(1L);
    category.setName("Electronics");
  }

  // =========================
  // findAll()
  // =========================
  @Test
  void findAll_shouldReturnCategoryList() {
    when(categoryRepository.findAll()).thenReturn(List.of(category));

    List<Category> result = categoryService.findAll();

    assertEquals(1, result.size());
    verify(categoryRepository, times(1)).findAll();
  }

  // =========================
  // findById() - SUCCESS
  // =========================
  @Test
  void findById_shouldReturnCategory_whenExists() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

    Category result = categoryService.findById(1L);

    assertNotNull(result);
    assertEquals("Electronics", result.getName());
    verify(categoryRepository, times(1)).findById(1L);
  }

  // =========================
  // findById() - NOT FOUND
  // =========================
  @Test
  void findById_shouldThrowException_whenNotFound() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(
      NotFoundException.class,
      () -> categoryService.findById(1L)
    );

    assertEquals("Category not found with id 1", exception.getMessage());
    verify(categoryRepository, times(1)).findById(1L);
  }

  // =========================
  // create() - SUCCESS
  // =========================
  @Test
  void create_shouldSaveCategory_whenNotDuplicate() {
    when(categoryRepository.existsByName("Electronics")).thenReturn(false);
    when(categoryRepository.save(any(Category.class))).thenReturn(category);

    Category result = categoryService.create(category);

    assertNotNull(result);
    assertEquals("Electronics", result.getName());
    verify(categoryRepository, times(1)).existsByName("Electronics");
    verify(categoryRepository, times(1)).save(category);
  }

  // =========================
  // create() - DUPLICATE
  // =========================
  @Test
  void create_shouldThrowException_whenDuplicateName() {
    when(categoryRepository.existsByName("Electronics")).thenReturn(true);

    DuplicateCategoryException exception = assertThrows(
      DuplicateCategoryException.class,
      () -> categoryService.create(category)
    );

    assertEquals("Category with name Electronics already exists", exception.getMessage());
    verify(categoryRepository, times(1)).existsByName("Electronics");
    verify(categoryRepository, never()).save(any());
  }

  // =========================
  // create() - TRIM NAME
  // =========================
  @Test
  void create_shouldTrimCategoryName() {
    category.setName("  Electronics  ");

    when(categoryRepository.existsByName("Electronics")).thenReturn(false);
    when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Category result = categoryService.create(category);

    assertEquals("Electronics", result.getName());
    verify(categoryRepository, times(1)).existsByName("Electronics");
    verify(categoryRepository, times(1)).save(category);
  }
}
