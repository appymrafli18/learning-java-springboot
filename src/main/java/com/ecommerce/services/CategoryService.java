package com.ecommerce.services;

import com.ecommerce.exceptions.DuplicateCategoryException;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.Category;
import com.ecommerce.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public List<Category> findAll() {
    return categoryRepository.findAll();
  }

  public Category findById(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found with id " + id));
  }

  public Category create(Category category) {
    String name = category.getName().trim();
    // Implementation for creating a category
    if (categoryRepository.existsByName(name)) {
      throw new DuplicateCategoryException("Category with name " + name + " already exists");
    }

    category.setName(name);
    return categoryRepository.save(category);
  }

}
