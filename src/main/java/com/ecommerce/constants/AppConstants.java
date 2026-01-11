package com.ecommerce.constants;

/**
 * Application-wide constants for error messages, validation rules, and API
 * configurations.
 */
public final class AppConstants {

    private AppConstants() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    // Error Messages
    public static final String USER_NOT_FOUND = "User not found with id: ";
    public static final String USER_NOT_FOUND_EMAIL = "User not found with email: ";
    public static final String EMAIL_ALREADY_EXISTS = "Email already registered: ";
    public static final String PASSWORD_MISMATCH = "Passwords do not match";
    public static final String INVALID_PASSWORD = "Invalid password";

    public static final String CATEGORY_NOT_FOUND = "Category not found with id: ";
    public static final String CATEGORY_ALREADY_EXISTS = "Category with name already exists: ";

    public static final String PRODUCT_NOT_FOUND = "Product not found with id: ";
    public static final String INVALID_PRODUCT_DATA = "Invalid product data provided";

    public static final String CART_NOT_FOUND = "Cart not found for user";
    public static final String CART_ITEM_NOT_FOUND = "Cart item not found";

    public static final String INVALID_REQUEST = "Invalid request data";
    public static final String TOKEN_INVALID_OR_EXPIRED = "Token is invalid or expired";
    public static final String ROLE_NOT_FOUND = "User role not found in token";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access";

    // Success Messages
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String REGISTER_SUCCESS = "Registration successful";
    public static final String USER_CREATED = "User created successfully";
    public static final String USER_UPDATED = "User updated successfully";
    public static final String USER_DELETED = "User deleted successfully";
    public static final String USER_RETRIEVED = "Users retrieved successfully";

    public static final String CATEGORY_CREATED = "Category created successfully";
    public static final String CATEGORY_RETRIEVED = "Categories retrieved successfully";

    public static final String PRODUCT_CREATED = "Product created successfully";
    public static final String PRODUCT_RETRIEVED = "Products retrieved successfully";
    public static final String PRODUCT_UPDATED = "Product updated successfully";
    public static final String PRODUCT_DELETED = "Product deleted successfully";

    public static final String CART_RETRIEVED = "Cart retrieved successfully";
    public static final String ITEM_ADDED_TO_CART = "Item added to cart successfully";
    public static final String ITEM_REMOVED_FROM_CART = "Item removed from cart successfully";

    // Security
    public static final String JWT_BEARER_PREFIX = "Bearer ";
    public static final String AUTH_ROLE_PREFIX = "ROLE_";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String COOKIE_ACCESS_TOKEN = "accessToken";

    // Validation Constants
    public static final int MIN_NAME_LENGTH = 6;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_EMAIL_LENGTH = 255;
    public static final int MAX_NAME_LENGTH = 100;

    // API Endpoints
    public static final String API_AUTH_PREFIX = "/api/auth/**";
    public static final String API_PRODUCT_PREFIX = "/api/product/**";
    public static final String API_USER_PREFIX = "/api/user/**";
    public static final String API_CATEGORY_PREFIX = "/api/categories/**";
    public static final String API_CART_PREFIX = "/api/cart/**";

    // CORS Configuration
    public static final String CORS_ALLOWED_ORIGIN = "http://localhost:5173";
    public static final String CORS_WILDCARD = "/**";

}
