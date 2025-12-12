package com.ecommerce.services;

import com.ecommerce.dto.requests.ProductPatchRequest;
import com.ecommerce.dto.requests.ProductRequest;
import com.ecommerce.models.Product;
import com.ecommerce.repositories.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

  private final ProductRepo productRepo;

  public ProductService(ProductRepo productRepo) {
    this.productRepo = productRepo;
  }

  public Product create(ProductRequest req) {
    Product product = new Product();

    product.setName(req.getName());
    product.setPrice(req.getPrice());
    product.setStock(req.getStock());

    return productRepo.save(product);
  }

  public Product update(Long id, ProductPatchRequest req) {
    Product product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("product not found"));

    if (req.getName() != null)  product.setName(req.getName());
    if (req.getPrice() != null) product.setPrice(req.getPrice());
    if (req.getStock() != null) product.setStock(req.getStock());

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
