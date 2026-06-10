package com.example.it211_project.dto;

import lombok.Data;

@Data
public class GradeRequest {

    private Long submissionId;

    private Double score;

    private String feedback;
}