package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.ProductPatchRequest;
import com.ecommerce.DTOs.requests.ProductRequest;
import com.ecommerce.constants.AppConstants;
import com.ecommerce.entity.Product;
import com.ecommerce.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing products.
 */
@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	/**
	 * Retrieves all products.
	 *
	 * @return API response with list of products
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<List<Product>>> index() {
		log.info("Fetching all products");
		List<Product> products = productService.findAll();

		return ResponseEntity.ok(ApiResponse.<List<Product>>builder()
				.statusCode(HttpStatus.OK.value())
				.message(AppConstants.PRODUCT_RETRIEVED)
				.data(products)
				.build());
	}

	/**
	 * Retrieves a product by ID.
	 *
	 * @param id the product ID
	 * @return API response with the product
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> findById(@PathVariable Long id) {
		log.info("Fetching product with id: {}", id);
		Product product = productService.findById(id);

		return ResponseEntity.ok(ApiResponse.<Product>builder()
				.statusCode(HttpStatus.OK.value())
				.message(AppConstants.PRODUCT_RETRIEVED)
				.data(product)
				.build());
	}

	/**
	 * Creates a new product.
	 *
	 * @param request the product creation request
	 * @return API response with the created product
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<Product>> create(@Valid @ModelAttribute ProductRequest request) {
		log.info("Creating new product: {}", request.getName());
		Product productCreate = productService.create(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Product>builder()
				.statusCode(HttpStatus.CREATED.value())
				.message(AppConstants.PRODUCT_CREATED)
				.data(productCreate)
				.build());
	}

	/**
	 * Updates an existing product.
	 *
	 * @param id      the product ID
	 * @param request the update request
	 * @return API response with the updated product
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> update(
			@PathVariable Long id,
			@Valid @RequestBody ProductPatchRequest request) {
		log.info("Updating product with id: {}", id);
		Product updateProduct = productService.update(id, request);

		return ResponseEntity.ok(ApiResponse.<Product>builder()
				.statusCode(HttpStatus.OK.value())
				.message(AppConstants.PRODUCT_UPDATED)
				.data(updateProduct)
				.build());
	}

	/**
	 * Deletes a product.
	 *
	 * @param id the product ID
	 * @return API response indicating successful deletion
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
		log.info("Deleting product with id: {}", id);
		productService.delete(id);

		return ResponseEntity.ok(ApiResponse.<Void>builder()
				.statusCode(HttpStatus.OK.value())
				.message(AppConstants.PRODUCT_DELETED)
				.build());
	}

}
