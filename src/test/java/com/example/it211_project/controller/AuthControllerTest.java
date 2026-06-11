package com.example.it211_project.controller;

import com.example.it211_project.dto.AuthResponse;
import com.example.it211_project.dto.UserResponse;
import com.example.it211_project.service.AuthService;
import com.example.it211_project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();
    }

    @Test
    void login_shouldReturnAuthResponse() throws Exception {
        AuthResponse response = new AuthResponse(
                "access-token-test",
                "refresh-token-test",
                "Bearer"
        );

        when(authService.login(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "username": "admin",
                                          "password": "123456"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token-test"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-test"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void register_shouldReturnUserResponse() throws Exception {
        UserResponse response = new UserResponse(
                1L,
                "Nguyen Van A",
                "student_test",
                "student@test.com",
                true,
                List.of("ROLE_STUDENT")
        );

        when(userService.registerStudent(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "fullName": "Nguyen Van A",
                                          "username": "student_test",
                                          "email": "student@test.com",
                                          "password": "123456"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("student_test"))
                .andExpect(jsonPath("$.email").value("student@test.com"));
    }

    @Test
    void refresh_shouldReturnNewAccessToken() throws Exception {
        AuthResponse response = new AuthResponse(
                "new-access-token",
                "old-refresh-token",
                "Bearer"
        );

        when(authService.refresh(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "refreshToken": "old-refresh-token"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("old-refresh-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void forgotPassword_shouldReturnSuccessMessage() throws Exception {
        when(userService.forgotPassword(any()))
                .thenReturn("Password reset success");

        mockMvc.perform(
                        post("/api/v1/auth/forgot-password")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "username": "student_test",
                                          "newPassword": "newpass123"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset success"));
    }

    @Test
    void changePassword_shouldReturnSuccessMessage() throws Exception {
        Principal principal = () -> "student_test";

        when(userService.changePassword(
                any(),
                eq("student_test")
        )).thenReturn("Password changed");

        mockMvc.perform(
                        post("/api/v1/auth/change-password")
                                .principal(principal)
                                .contentType("application/json")
                                .content("""
                                        {
                                          "oldPassword": "oldpass",
                                          "newPassword": "newpass123"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed"));
    }
}