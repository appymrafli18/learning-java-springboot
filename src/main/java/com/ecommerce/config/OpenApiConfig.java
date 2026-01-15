package com.ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "Documentation E-Commerce API",
    description = "API for simple system e-commerce",
    version = "1.0.0",
    summary = "summary",
    contact = @Contact(
      name = "contact swagger",
      email = "contact@gmail.com",
      url = "http://localhost:5000"
    )
  ),
  // add list server (Local, Dev, Prod)
  servers = {
    @Server(url = "http://localhost:5000", description = "Development Server"),
    @Server(url = "http://localhost:5051", description = "Production Server")
  }
  // Langkah Tambahan: Menerapkan security secara global ke semua endpoint
  // security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
  name = "Bearer Authentication",
  description = "This for Bearer JWT Authentication",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "Bearer"
)
public class OpenApiConfig {

  @Bean
  public GroupedOpenApi userApi() {
    return GroupedOpenApi.builder().group("users").pathsToMatch("/api/user/**").build();
  }

  @Bean
  public GroupedOpenApi authApi(){
    return GroupedOpenApi.builder().group("authentication").pathsToMatch("/api/auth/**").build();
  }

  @Bean
  public GroupedOpenApi categoryApi(){
    return GroupedOpenApi.builder().group("categories").pathsToMatch("/api/categories/**")
      .addOpenApiCustomizer(openApi -> openApi.addSecurityItem(
        new SecurityRequirement().addList("Bearer Authentication")
      ))
      .build();
  }

  @Bean
  public GroupedOpenApi cartApi(){
    return GroupedOpenApi.builder().group("cart").pathsToMatch("/api/cart/**").build();
  }

  @Bean
  public GroupedOpenApi productApi(){
    return GroupedOpenApi.builder().group("products").pathsToMatch("/api/product/**").build();
  }

}
