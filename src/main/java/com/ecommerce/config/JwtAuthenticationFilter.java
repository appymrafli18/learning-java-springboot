package com.ecommerce.config;

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
import org.springframework.web.ErrorResponseException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
    // Log semua info request
    // log.info("Request URI: {}", request.getRequestURI());
    // log.info("Request Method: {}", request.getMethod());

    log.info("=== JWT Filter Debug ===");
    log.info("Request URI: {}", request.getRequestURI());


    // 1. ambil header bernama "Authorization"
    final String authHeader = request.getHeader("Authorization");
    log.info("Authorization Header present: {}", authHeader != null);

    final String token;
    final Claims extractToken;

    // 2. cek apakah ada nilai yang dimasukkan ke dalamnya atau tidak
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.warn("No valid Authorization header found for URI: {}", request.getRequestURI());
      filterChain.doFilter(request, response);
      return;
    }

    // 3. jika ada, maka kita akan melakukan proses validasi JWT
    try {
      token = authHeader.substring(7);
      extractToken = jwtService.extractToken(token);
      // log.debug("extractToken: {}", extractToken);
      log.debug("JWT subject: {}", extractToken.getSubject());

      log.info("Extracted Role: {}", extractToken.get("role"));  // ← Lihat role di sini
      log.info("Subject: {}", extractToken.getSubject());
      log.info("Token Valid: {}", jwtService.isTokenValid(token));

      // 4. jika tidak ada, maka kita akan melanjutkan proses selanjutnya
      if (extractToken.getSubject() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        if (jwtService.isTokenValid(token)) {
          // 1. Ekstrak role dari payload token
          String role = (String) extractToken.get("role");

          UsernamePasswordAuthenticationToken authToken = getUsernamePasswordAuthenticationToken(role, extractToken);
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (Exception e) {
      // Jika token expired atau rusak, hentikan di sini
      log.error("JWT Validation Error: {}", e.getMessage());
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Invalid atau Expired");
      return; // Berhenti agar tidak lanjut ke filterChain di bawah
    }

    log.info("Proceeding to next filter/controller...");
    filterChain.doFilter(request, response);
  }

  private static @NonNull UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String role, Claims extractToken) {
    if (role == null) throw new RuntimeException("Error Role Not Found");

    // 2. Ubah string role menjadi format yang dikenali Spring Security
    // Biasanya Spring mengharapkan awalan "ROLE_", contoh: "ROLE_USER"
    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

    // banyak role
//        List<String> roles = extractToken.get("roles", List.class);
//        List<SimpleGrantedAuthority> authorities = roles.stream().map(roleValue -> new SimpleGrantedAuthority("ROLE_" + roleValue)).toList();

    return new UsernamePasswordAuthenticationToken(
      extractToken.getSubject(),
      null,
      authorities
    );
  }
}
