package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot application entry point for the E-Commerce system.
 * Initializes the application with JPA auditing and component scanning.
 */
@SpringBootApplication
@EnableJpaAuditing
public class ECommerceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ECommerceApplication.class, args);
  }

}
