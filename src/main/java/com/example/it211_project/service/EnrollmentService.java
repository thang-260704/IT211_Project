package com.example.it211_project.service;

import com.example.it211_project.dto.EnrollmentResponse;
import com.example.it211_project.entity.Course;
import com.example.it211_project.entity.Enrollment;
import com.example.it211_project.entity.User;
import com.example.it211_project.repository.CourseRepository;
import com.example.it211_project.repository.EnrollmentRepository;
import com.example.it211_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentResponse registerCourse(
            Long studentId,
            Long courseId
    ) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .registeredAt(LocalDateTime.now())
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);

        return new EnrollmentResponse(
                saved.getId(),
                student.getFullName(),
                course.getCourseName(),
                saved.getRegisteredAt()
        );
    }

    public EnrollmentResponse registerCourseByUsername(
            String username,
            Long courseId
    ) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .registeredAt(LocalDateTime.now())
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);

        return new EnrollmentResponse(
                saved.getId(),
                student.getFullName(),
                course.getCourseName(),
                saved.getRegisteredAt()
        );
    }
}