package com.example.it211_project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MaterialRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "File URL is required")
    private String fileUrl;
}