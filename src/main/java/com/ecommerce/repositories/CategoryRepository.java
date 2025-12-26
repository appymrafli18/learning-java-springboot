package com.ecommerce.repositories;

import com.ecommerce.models.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  @EntityGraph(attributePaths = "products")
  List<Category> findAll();

  boolean existsByName(String name);

}
