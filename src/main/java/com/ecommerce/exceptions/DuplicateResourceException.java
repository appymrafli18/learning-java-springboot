package com.ecommerce.exceptions;

/**
 * Exception thrown when attempting to create a duplicate resource.
 * This exception is used for operations like email already exists or category name already exists.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
