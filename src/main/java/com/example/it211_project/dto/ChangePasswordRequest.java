package com.example.it211_project.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String oldPassword;

    private String newPassword;
}