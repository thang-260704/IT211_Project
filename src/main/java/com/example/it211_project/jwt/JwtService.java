package com.example.it211_project.jwt;

import com.example.it211_project.entity.Role;
import com.example.it211_project.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final String secret =
            "my-super-secret-key-for-it211-project-jwt-256-bit-long";

    private final SecretKey key =
            Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    private final long accessExpiration = 1000L * 60 * 15;

    private final long refreshExpiration = 1000L * 60 * 60 * 24 * 7;

    public String generateAccessToken(User user) {

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        return generateToken(
                user.getUsername(),
                roles,
                accessExpiration
        );
    }

    public String generateRefreshToken(User user) {

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        return generateToken(
                user.getUsername(),
                roles,
                refreshExpiration
        );
    }

    private String generateToken(
            String username,
            List<String> roles,
            long expiration
    ) {
        Date now = new Date();
        Date expiredAt =
                new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public LocalDateTime extractExpiration(String token) {
        Date expiration =
                extractClaims(token).getExpiration();

        return expiration.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}