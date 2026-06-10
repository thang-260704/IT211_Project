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

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    // FR-06
    public EnrollmentResponse registerCourse(
            Long studentId,
            Long courseId
    ) {

        User student = userRepository.findById(studentId)
                .orElseThrow();

        Course course = courseRepository.findById(courseId)
                .orElseThrow();

        if (enrollmentRepository
                .existsByStudentAndCourse(student, course)) {

            throw new RuntimeException(
                    "Student already enrolled"
            );
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .registeredAt(LocalDateTime.now())
                .build();

        Enrollment saved =
                enrollmentRepository.save(enrollment);

        return new EnrollmentResponse(
                saved.getId(),
                student.getFullName(),
                course.getCourseName(),
                saved.getRegisteredAt()
        );
    }
}