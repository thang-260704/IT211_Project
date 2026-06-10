package com.example.it211_project.dto;

import lombok.Data;

@Data
public class SubmissionRequest {

    private Long courseId;

    private String githubUrl;

    private String reportUrl;
}