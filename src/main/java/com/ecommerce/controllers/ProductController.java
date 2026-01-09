package com.ecommerce.controllers;

import com.ecommerce.DTOs.ApiResponse;
import com.ecommerce.DTOs.requests.ProductPatchRequest;
import com.ecommerce.DTOs.requests.ProductRequest;
import com.ecommerce.models.Product;
import com.ecommerce.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {

	private final ProductService productService;

	@GetMapping
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<Product>>> index() {
		List<Product> products = productService.findAll();

		return ResponseEntity.ok(
				ApiResponse.<List<Product>>builder()
						.statusCode(HttpStatus.OK.value())
						.message("Products Retrieved Successfully")
						.data(products)
						.build());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> findById(@PathVariable("id") Long id) {
		Product search = productService.findById(id);

		return ResponseEntity.ok(
				ApiResponse.<Product>builder()
						.statusCode(HttpStatus.OK.value())
						.message("Products Retrieved Successfully")
						.data(search)
						.build());
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Product>> create(@Valid @RequestBody ProductRequest request) {
		Product productCreate = productService.create(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(
				ApiResponse.<Product>builder()
						.statusCode(HttpStatus.CREATED.value())
						.message("Success create product")
						.data(productCreate)
						.build());
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse<Product>> update(@PathVariable("id") Long id,
			@Valid @RequestBody ProductPatchRequest request) {
		Product updateProduct = productService.update(id, request);

		return ResponseEntity.ok(
				ApiResponse.<Product>builder()
						.statusCode(HttpStatus.OK.value())
						.message("Products Update Successfully")
						.data(updateProduct)
						.build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") Long id) {
		productService.delete(id);

		return ResponseEntity.ok(
				ApiResponse.<Void>builder()
						.statusCode(HttpStatus.OK.value())
						.message("Products Update Successfully")
						.build());
	}
}
