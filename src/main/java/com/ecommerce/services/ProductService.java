package com.ecommerce.services;

import com.ecommerce.DTOs.requests.ProductPatchRequest;
import com.ecommerce.DTOs.requests.ProductRequest;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for managing product-related operations.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryService categoryService;

  /**
   * Retrieves all products.
   *
   * @return list of all products
   */
  public List<Product> findAll() {
    log.debug("Fetching all products");
    return productRepository.findAll();
  }

  /**
   * Creates a new product.
   *
   * @param request the product creation request
   * @return the created product
   * @throws NotFoundException if category not found or request is invalid
   */
  public Product create(ProductRequest request) {
    if (request == null || request.getName() == null) {
      throw new NotFoundException(AppConstants.INVALID_PRODUCT_DATA);
    }

    Category categorySearch = categoryService.findById(request.getCategoryId());

    String imageName = request.getImage() != null ? request.getImage().getOriginalFilename() : null;

    Product product = Product.builder()
        .name(request.getName())
        .price(request.getPrice())
        .stock(request.getStock())
        .image(imageName)
        .category(categorySearch)
        .build();

    productRepository.save(product);
    log.info("Product created successfully: {}", product.getName());

    return product;
  }

  /**
   * Updates an existing product.
   *
   * @param id      the product ID
   * @param request the update request
   * @return the updated product
   * @throws NotFoundException if product not found
   */
  public Product update(Long id, ProductPatchRequest request) {
    if (id == null || id <= 0) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    Product product = this.findById(id);

    if (request.getName() != null && !request.getName().isBlank()) {
      product.setName(request.getName());
    }
    if (request.getPrice() != null && request.getPrice() > 0) {
      product.setPrice(request.getPrice());
    }
    if (request.getStock() != null && request.getStock() > 0) {
      product.setStock(request.getStock());
    }

    productRepository.save(product);
    log.info("Product updated successfully: {}", id);

    return product;
  }

  /**
   * Retrieves a product by ID.
   *
   * @param id the product ID
   * @return the product
   * @throws NotFoundException if product not found
   */
  public Product findById(Long id) {
    if (id == null || id <= 0) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    return productRepository.findById(id)
        .orElseThrow(() -> {
          log.warn("Product not found with id: {}", id);
          return new NotFoundException(AppConstants.PRODUCT_NOT_FOUND + id);
        });
  }

  /**
   * Deletes a product.
   *
   * @param id the product ID
   * @throws NotFoundException if product not found
   */
  public void delete(Long id) {
    if (id == null || id <= 0) {
      throw new NotFoundException(AppConstants.INVALID_REQUEST);
    }

    if (!productRepository.existsById(id)) {
      log.warn("Product not found for deletion with id: {}", id);
      throw new NotFoundException(AppConstants.PRODUCT_NOT_FOUND + id);
    }

    productRepository.deleteById(id);
    log.info("Product deleted successfully: {}", id);
  }

}
