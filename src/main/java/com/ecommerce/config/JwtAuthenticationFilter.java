package com.ecommerce.config;

import com.ecommerce.constants.AppConstants;
import com.ecommerce.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT authentication filter to validate JWT tokens in incoming requests.
 * Runs once per request to extract and validate the JWT token.
 */
@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    try {
      final String authHeader = request.getHeader(AppConstants.AUTHORIZATION_HEADER);

      // Check if Authorization header exists and starts with "Bearer "
      if (authHeader == null || !authHeader.startsWith(AppConstants.JWT_BEARER_PREFIX)) {
        log.debug("No valid JWT token found in request: {}", request.getRequestURI());
        filterChain.doFilter(request, response);
        return;
      }

      // Extract token from header
      String token = authHeader.substring(AppConstants.JWT_BEARER_PREFIX.length());

      // Validate token and extract claims
      Claims claims = jwtService.extractToken(token);
      String userSubject = claims.getSubject();
      String role = (String) claims.get("role");

      if (userSubject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UsernamePasswordAuthenticationToken authToken = getUsernamePasswordAuthenticationToken(role, claims);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.debug("JWT token validated for user: {}", userSubject);
      }

      filterChain.doFilter(request, response);

    } catch (Exception e) {
      log.warn("JWT validation failed: {}", e.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write(String.format(
          "{\"statusCode\": 401, \"message\": \"%s\"}", AppConstants.TOKEN_INVALID_OR_EXPIRED));
    }
  }

  /**
   * Creates authentication token with granted authorities based on user role.
   *
   * @param role         the user role from JWT claims
   * @param extractToken the JWT claims
   * @return UsernamePasswordAuthenticationToken with authorities
   */
  private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(
      String role, Claims extractToken) {

    if (role == null) {
      log.error("Role not found in JWT token");
      throw new RuntimeException(AppConstants.ROLE_NOT_FOUND);
    }

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AppConstants.AUTH_ROLE_PREFIX + role));

    return new UsernamePasswordAuthenticationToken(
        extractToken.getSubject(),
        null,
        authorities);
  }

}
