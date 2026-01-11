package com.ecommerce.services;

import com.ecommerce.DTOs.responses.JwtUserPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for JWT token generation, validation, and extraction.
 */
@Slf4j
@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Generates a JWT token from user payload.
     *
     * @param payload the user payload containing id, name, email, and role
     * @return JWT token string
     */
    public String generateToken(JwtUserPayload payload) {
        if (payload == null || payload.email() == null) {
            throw new IllegalArgumentException("Invalid JWT payload");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", payload.id());
        claims.put("name", payload.name());
        claims.put("role", payload.role());

        log.debug("Generating token for user: {}", payload.email());
        return buildToken(claims, payload.email(), jwtExpiration);
    }

    /**
     * Builds a JWT token with the given claims and expiration.
     *
     * @param userPayload the claims to include
     * @param subject     the token subject (typically email)
     * @param expiration  the expiration time in milliseconds
     * @return JWT token string
     */
    private String buildToken(Map<String, Object> userPayload, String subject, long expiration) {
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder()
                .claims(userPayload)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Extracts and validates claims from a JWT token.
     *
     * @param token the JWT token
     * @return the token claims
     */
    public Claims extractToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Invalid token");
        }

        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Gets the signing key from the secret key configuration.
     *
     * @return the secret key for signing
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
