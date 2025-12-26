package com.ecommerce.exceptions;

import com.ecommerce.DTOs.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
      .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(400, "Validation failed", null, errors));
  }

  @ExceptionHandler(DuplicateCategoryException.class)
  public ResponseEntity<?> handleDuplicateCategory(DuplicateCategoryException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(
      409,
      ex.getMessage(),
      null

    ));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<?> handleNotFound(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
      404,
      ex.getMessage(),
      null
    ));
  }
}
