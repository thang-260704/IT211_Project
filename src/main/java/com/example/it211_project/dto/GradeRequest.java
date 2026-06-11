package com.example.it211_project.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GradeRequest {

    @NotNull(message = "Submission ID is required")
    private Long submissionId;

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be greater than or equal to 0")
    @Max(value = 10, message = "Score must be less than or equal to 10")
    private Double score;

    private String feedback;
}