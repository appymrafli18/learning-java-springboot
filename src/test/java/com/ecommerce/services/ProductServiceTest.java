package com.ecommerce.services;

import com.ecommerce.DTOs.requests.ProductPatchRequest;
import com.ecommerce.DTOs.requests.ProductRequest;
import com.ecommerce.exceptions.NotFoundException;
import com.ecommerce.models.Category;
import com.ecommerce.models.Product;
import com.ecommerce.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryService categoryService;

  @InjectMocks
  private ProductService productService;

  /*
   * TODO <find all>: success
   * */
  @Test
  void find_all_success() {

    List<Product> products = new ArrayList<>();

    Category category = Category.builder().id(1L).name("category 1").build();


    for (int i = 1; i <= 5; i++) {
      String name = "Product " + i;
      products.add(
        Product.builder().id((long) i).name(name).price((long) 15000).stock(10).category(category).build()
      );
    }

    when(productRepository.findAll()).thenReturn(products);

    List<Product> hitMethod = productService.findAll();

    assertNotNull(hitMethod);
    assertEquals(5, hitMethod.size());
    assertEquals("Product 5", hitMethod.get(5 - 1).getName());

    verify(productRepository, times(1)).findAll();

    verifyNoMoreInteractions(productRepository);
  }

  /*
   * TODO <find by id>: success + not found
   * */
  @Test
  void findById_success() {

    // Arrange
    Category category = Category.builder().id(1L).name("Category 1").build();

    Product product = Product.builder().id(1L).name("Product 1").price(15000L).stock(10).category(category).build();

    when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));

    Product prd = productService.findById(1L);

    assertEquals("Product 1", prd.getName());
    assertNotNull(prd);
    assertDoesNotThrow(() -> prd);

    verify(productRepository, times(1)).findById(1L);
  }

  @Test
  void findById_notFound() {
    Long id = 99L;

    when(productRepository.findById(id)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.findById(id));

    assertEquals("Product not found with id " + id, exception.getMessage());

    verify(productRepository, times(1)).findById(id);
  }

  /*
   * TODO <create>: success + not found
   * */
  @Test
  void create_success() {
    ProductRequest request = new ProductRequest();
    request.setName("Product 1");
    request.setPrice(15000L);
    request.setStock(10);
    request.setCategoryId(1L);

    Category category = Category.builder().id(1L).name("Category 1").build();

    when(categoryService.findById(1L)).thenReturn(category);

    when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // action
    Product result = productService.create(request);

    // assert
    assertNotNull(result);
    assertEquals("Product 1", result.getName());
    assertEquals(15_000L, result.getPrice());
    assertEquals(10, result.getStock());
    assertEquals(category, result.getCategory());

    // verify
    verify(categoryService, times(1)).findById(1L);
    verify(productRepository, times(1)).save(any(Product.class));

    // tidak ada method yang di panggil selain kedua ini
    verifyNoMoreInteractions(productRepository, categoryService);

  }

  @Test
  void create_notFound() {

  }

  /*
   * TODO <update>: success + not found
   * */
  @Test
  void update_success() {
    Category category = Category.builder().id(1L).name("Category 1").build();

    Product product = new Product();
    product.setId(1L);
    product.setName("Product 1");
    product.setPrice(15000L);
    product.setStock(10);
    product.setCategory(category);

    ProductPatchRequest request = new ProductPatchRequest();
    request.setName("new-data-product");

    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(productRepository.save(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    // action
    Product updated = productService.update(1L, request);

    // assert
    assertNotNull(updated);
    assertEquals("new-data-product", updated.getName());
    assertDoesNotThrow(() -> updated);

    // verify
    verify(productRepository, times(1)).findById(1L);
    verify(productRepository, times(1)).save(any(Product.class));
    verifyNoMoreInteractions(productRepository);

  }

  @Test
  void update_notFound() {
    Long id = 99L;
    ProductPatchRequest request = new ProductPatchRequest();

    when(productRepository.findById(id)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.update(id, request));

    assertEquals("Product not found with id " + id, exception.getMessage());

    verify(productRepository, times(1)).findById(id);
  }

  /*
   * TODO <delete>: success + not found
   * */
  @Test
  void delete_success() {
    // Arrange
    long productId = 1000L;

    assertDoesNotThrow(() -> productService.delete(productId));

    verify(productRepository, times(1)).deleteById(productId);

    verifyNoMoreInteractions(productRepository);
  }

  @Test
  void delete_notFound() {
    // Arrange
    long productId = 1000L;

    doThrow(new EmptyResultDataAccessException(1)).when(productRepository).deleteById(productId);

    NotFoundException exception = assertThrows(NotFoundException.class, () -> productService.delete(productId));

    assertEquals(
      "Product not found with id " + productId,
      exception.getMessage()
    );

    verify(productRepository, times(1)).deleteById(productId);

    verifyNoMoreInteractions(productRepository);
  }

}
