package com.example.it211_project.controller;

import com.example.it211_project.dto.EnrollmentResponse;
import com.example.it211_project.dto.SubmissionRequest;
import com.example.it211_project.service.EnrollmentService;
import com.example.it211_project.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    @PostMapping("/register-course")
    public EnrollmentResponse registerCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId
    ) {
        return enrollmentService.registerCourse(
                studentId,
                courseId
        );
    }

    @PostMapping("/submit-project")
    public String submitProject(
            @RequestBody SubmissionRequest request,
            Principal principal
    ) {
        return studentService.submitProject(
                request,
                principal.getName()
        );
    }
}