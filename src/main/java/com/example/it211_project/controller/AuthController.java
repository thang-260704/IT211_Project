package com.example.it211_project.controller;

import com.example.it211_project.dto.*;
import com.example.it211_project.service.AuthService;
import com.example.it211_project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(
            @Valid @RequestBody RegisterStudentRequest request
    ) {
        return userService.registerStudent(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    public String logout(
            @RequestHeader("Authorization") String token
    ) {
        return authService.logout(token);
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal principal
    ) {
        return userService.changePassword(
                request,
                principal.getName()
        );
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        return userService.forgotPassword(request);
    }
}