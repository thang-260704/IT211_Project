package com.example.it211_project.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}