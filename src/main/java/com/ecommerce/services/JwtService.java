package com.ecommerce.services;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.ecommerce.DTOs.responses.JwtUserPayload;
import com.ecommerce.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // key
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // method to generate token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject("USER AUTHENTICATION")
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(key).compact();

    }

    // method to extract payload from token
    public JwtUserPayload extractPayloadToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new JwtUserPayload(
                claims.get("id", Long.class),
                claims.get("name", String.class),
                claims.get("email", String.class));
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }
}
