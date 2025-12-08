package com.ecommerce.repositories;

import com.ecommerce.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepo extends CrudRepository<Product, Long> {
}
