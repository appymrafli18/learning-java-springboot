package com.ecommerce.services;

import com.ecommerce.DTOs.responses.JwtUserPayload;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // Ambil secret key dari application.properties agar tidak hardcoded
    @Value("${application.security.jwt.secret-key}")
    private String SecretKey;

    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;

    // 1. generate token
    public String generateToken(JwtUserPayload payload) {
        Map<String, Object> claims = new HashMap<>();

        // Masukkan data dari payload ke dalam claims
        claims.put("userId", payload.id());
        claims.put("name", payload.name());
        claims.put("role", payload.role());

        return buildToken(claims, payload.email(), jwtExpiration);
    }

    private String buildToken(Map<String, Object> userPayload, String subject, long expiration) {
        return Jwts.builder()
                .claims(userPayload)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    // 2. extract token + validation token
    public Claims extractToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token).getPayload();
    }

    // 4. Helper
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
