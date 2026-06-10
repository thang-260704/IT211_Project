package com.example.it211_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EnrollmentResponse {

    private Long id;

    private String studentName;

    private String courseName;

    private LocalDateTime registeredAt;
}