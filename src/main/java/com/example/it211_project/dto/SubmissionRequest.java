package com.example.it211_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmissionRequest {

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotBlank(message = "GitHub URL is required")
    private String githubUrl;

    private String reportUrl;
}