package com.example.it211_project.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseRequest {

    @NotBlank
    private String courseName;

    private String description;

    @NotNull
    @Min(1)
    private Integer duration;
}