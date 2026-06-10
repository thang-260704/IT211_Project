package com.example.it211_project.controller;

import com.example.it211_project.dto.EnrollmentResponse;
import com.example.it211_project.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final EnrollmentService enrollmentService;

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
}
//FR-06: Sinh viên đăng ký tham gia khóa học.//