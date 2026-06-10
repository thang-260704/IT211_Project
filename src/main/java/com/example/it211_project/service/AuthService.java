package com.example.it211_project.service;

import com.example.it211_project.dto.*;
import com.example.it211_project.entity.TokenBlacklist;
import com.example.it211_project.entity.User;
import com.example.it211_project.jwt.JwtService;
import com.example.it211_project.repository.TokenBlacklistRepository;
import com.example.it211_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public AuthResponse login(
            LoginRequest request
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow();

        String accessToken =
                jwtService.generateAccessToken(user);

        String refreshToken =
                jwtService.generateRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer"
        );
    }

    public AuthResponse refresh(
            RefreshTokenRequest request
    ) {
        String refreshToken =
                request.getRefreshToken();

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException(
                    "Refresh Token invalid"
            );
        }

        String username =
                jwtService.extractUsername(refreshToken);

        User user = userRepository
                .findByUsername(username)
                .orElseThrow();

        String newAccessToken =
                jwtService.generateAccessToken(user);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                "Bearer"
        );
    }

    public String logout(
            String authorizationHeader
    ) {
        String accessToken = authorizationHeader;

        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        TokenBlacklist tokenBlacklist =
                TokenBlacklist.builder()
                        .token(accessToken)
                        .expiredAt(
                                jwtService.extractExpiration(
                                        accessToken
                                )
                        )
                        .build();

        tokenBlacklistRepository.save(tokenBlacklist);

        SecurityContextHolder.clearContext();

        return "Logout success";
    }
}