package com.example.it211_project.service;

import com.example.it211_project.dto.AuthResponse;
import com.example.it211_project.dto.LoginRequest;
import com.example.it211_project.dto.RefreshTokenRequest;
import com.example.it211_project.entity.User;
import com.example.it211_project.jwt.JwtService;
import com.example.it211_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer"
        );
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh Token invalid");
        }

        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(user);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                "Bearer"
        );
    }

    public String logout(String authorizationHeader) {
        String accessToken = authorizationHeader;

        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        Date expirationDate =
                jwtService.extractExpiration(accessToken);

        redisTokenService.blacklistToken(
                accessToken,
                expirationDate
        );

        SecurityContextHolder.clearContext();

        return "Logout success";
    }
}