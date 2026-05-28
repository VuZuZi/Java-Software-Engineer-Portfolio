package com.lge.portfolio.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // key ký JWT
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // generate token
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // extract userId
    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    // parse claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // validate token
    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return  extractAllClaims(token).get("email").toString();
    }
}