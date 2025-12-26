package com.ecommerce.services;

import com.ecommerce.DTOs.requests.ProductPatchRequest;
import com.ecommerce.DTOs.requests.ProductRequest;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.Product;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  public List<Product> findAll() {
    return productRepository.findAll();
  }

  public Product create(ProductRequest request) {
    Product product = new Product();

    product.setName(request.getName());
    product.setPrice(request.getPrice());
    product.setStock(request.getStock());

    return productRepository.save(product);
  }

  public Product update(Long id, ProductPatchRequest request) {
    Product product = this.findById(id);

    if (request.getName() != null) product.setName(request.getName());
    if (request.getPrice() != null) product.setPrice(request.getPrice());
    if (request.getStock() != null) product.setStock(request.getStock());

    return productRepository.save(product);
  }

  public Product findById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found with id " + id));
  }

  public void delete(Long id) {
    try {
      productRepository.deleteById(id);
    } catch (EmptyResultDataAccessException ex) {
      throw new NotFoundException("Product not found with id " + id);
    }
  }

}
