package com.backend.portfolio.security.jwt;

import com.backend.portfolio.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    private static final long EXPIRATION =
            1000 * 60 * 60 * 24; // 24h

    @PostConstruct
    public void init() {

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(
            Long userId,
            String email,
            Role role
    ) {

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + EXPIRATION)
                )
                .signWith(secretKey)
                .compact();
    }

    public Claims parseClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getEmail(String token) {

        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {

        try {

            parseClaims(token);
            return true;

        } catch (Exception ex) {

            log.warn("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }
}