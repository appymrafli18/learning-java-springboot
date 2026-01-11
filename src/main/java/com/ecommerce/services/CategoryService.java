package com.ecommerce.services;

import com.ecommerce.constants.AppConstants;
import com.ecommerce.exceptions.DuplicateResourceException;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.entity.Category;
import com.ecommerce.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing category-related operations.
 */
@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  /**
   * Retrieves all categories.
   *
   * @return list of all categories
   */
  public List<Category> findAll() {
    log.debug("Fetching all categories");
    return categoryRepository.findAll();
  }

  /**
   * Retrieves a category by ID.
   *
   * @param id the category ID
   * @return the category
   * @throws NotFoundException if category not found
   */
  public Category findById(Long id) {
    if (id == null || id <= 0) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    return categoryRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("Category not found with id: {}", id);
          return new NotFoundException(AppConstants.CATEGORY_NOT_FOUND + id);
        });
  }

  /**
   * Creates a new category.
   *
   * @param category the category to create
   * @return the created category
   * @throws DuplicateResourceException if category name already exists
   * @throws NotFoundException          if category data is invalid
   */
  public Category create(Category category) {
    if (category == null || category.getName() == null) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    String name = category.getName().trim();

    if (name.isBlank()) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    if (categoryRepository.existsByName(name)) {
      log.warn("Duplicate category name: {}", name);
      throw new DuplicateResourceException(AppConstants.CATEGORY_ALREADY_EXISTS + name);
    }

    category.setName(name);
    categoryRepository.save(category);
    log.info("Category created successfully: {}", name);

    return category;
  }

}
