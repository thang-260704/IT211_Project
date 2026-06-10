package com.example.it211_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseResponse {

    private Long id;

    private String courseName;

    private String description;

    private Integer duration;

    private boolean active;
}