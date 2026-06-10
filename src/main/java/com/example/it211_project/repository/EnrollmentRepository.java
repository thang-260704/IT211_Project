package com.example.it211_project.repository;

import com.example.it211_project.entity.Course;
import com.example.it211_project.entity.Enrollment;
import com.example.it211_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentAndCourse(
            User student,
            Course course
    );
}