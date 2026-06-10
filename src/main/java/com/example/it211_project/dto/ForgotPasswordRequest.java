package com.example.it211_project.dto;

import lombok.Data;

@Data
public class ForgotPasswordRequest {

    private String username;

    private String newPassword;
}