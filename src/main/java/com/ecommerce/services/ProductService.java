package com.ecommerce.services;

import com.ecommerce.models.Product;
import com.ecommerce.repositories.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

  @Autowired
  private ProductRepo productRepo;

  public Product create(Product product) {
    return productRepo.save(product);
  }

  public Product update(Product product) {
    return productRepo.save(product);
  }

  public Product findById(Long id) {
    return productRepo.findById(id).orElse(null);
  }

  public void delete(Long id) {
    productRepo.deleteById(id);
  }

  public Iterable<Product> findAll() {
    return productRepo.findAll();
  }
}
