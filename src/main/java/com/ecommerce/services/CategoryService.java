package com.ecommerce.services;

import com.ecommerce.exceptions.DuplicateCategoryException;
import com.ecommerce.models.Category;
import com.ecommerce.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Transactional
@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  public Category create(Category category) {
    String name = category.getName().trim();
    // Implementation for creating a category
    if (categoryRepository.existsByName(name)) {
      throw new DuplicateCategoryException("Category with name " + name + " already exists");
    }

    category.setName(name);

    try {
      return categoryRepository.save(category);
    } catch (DataIntegrityViolationException e) {
      throw new DuplicateCategoryException("Category with name " + name + " already exists");
    }
  }

}
