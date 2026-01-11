package com.ecommerce.exceptions;

import com.ecommerce.constants.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom entry point for authentication exceptions.
 * Returns a JSON response when authentication fails.
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AuthenticationException authException) throws IOException, ServletException {

    log.warn("Authentication failed for path: {} - {}", request.getRequestURI(), authException.getMessage());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    String jsonResponse = String.format(
        "{\"statusCode\": %d, \"message\": \"%s\", \"error\": \"UNAUTHORIZED\"}",
        HttpServletResponse.SC_UNAUTHORIZED,
        AppConstants.TOKEN_INVALID_OR_EXPIRED);

    response.getWriter().write(jsonResponse);
  }

}
