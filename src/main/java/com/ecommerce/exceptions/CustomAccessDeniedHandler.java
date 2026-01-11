package com.ecommerce.exceptions;

import com.ecommerce.constants.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom handler for access denied exceptions.
 * Returns a JSON response when user lacks required permissions.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AccessDeniedException accessDeniedException) throws IOException, ServletException {

    log.warn("Access denied for user on path: {}", request.getRequestURI());

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    String jsonResponse = String.format(
        "{\"statusCode\": %d, \"message\": \"%s\", \"error\": \"FORBIDDEN\"}",
        HttpServletResponse.SC_FORBIDDEN,
        AppConstants.UNAUTHORIZED_ACCESS);

    response.getWriter().write(jsonResponse);
  }

}
