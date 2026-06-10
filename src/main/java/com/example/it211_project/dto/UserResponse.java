package com.example.it211_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String fullName;

    private String username;

    private String email;

    private boolean active;

    private List<String> roles;
}