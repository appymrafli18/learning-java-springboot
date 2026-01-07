package com.ecommerce.config;

import com.ecommerce.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Log semua info request
        log.info("Request URI: {}", request.getRequestURI());
        log.info("Request Method: {}", request.getMethod());

        // 1. ambil header bernama "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String token;
        final Claims extractToken;

        // 2. cek apakah ada nilai yang dimasukkan ke dalamnya atau tidak
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        };

        // 3. jika ada, maka kita akan melakukan proses validasi JWT
        try {
            token = Objects.requireNonNull(authHeader).substring(7);
            extractToken = jwtService.extractToken(token);
            log.info("extractToken: {}", extractToken);

            // 4. jika tidak ada, maka kita akan melanjutkan proses selanjutnya
            if (extractToken.getSubject() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtService.isTokenValid(token)) {
                    // 1. Ekstrak role dari payload token
                    String role = (String) extractToken.get("role");

                    // 2. Ubah string role menjadi format yang dikenali Spring Security
                    // Biasanya Spring mengharapkan awalan "ROLE_", contoh: "ROLE_USER"
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            extractToken.getSubject(),
                            null,
                            authorities
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Jika token expired atau rusak, hentikan di sini
            log.error("JWT Validation Error: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token Invalid atau Expired");
            return; // Berhenti agar tidak lanjut ke filterChain di bawah
        }

        filterChain.doFilter(request, response);

        // Log semua cookie
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("token-jwt".equals(cookie.getName())) {
//                    try {
//                        // TODO: Implementasi validasi JWT di sini
//
//                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(cookie.getValue(), null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
//                        SecurityContextHolder.getContext().setAuthentication(auth);
//                    } catch (Exception e) {
//                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                        return;
//                    }
//                }
//                log.info("Found cookie: {}={}", cookie.getName(), cookie.getValue());
//            }
//        }
//
//        filterChain.doFilter(request, response);
    }
}
