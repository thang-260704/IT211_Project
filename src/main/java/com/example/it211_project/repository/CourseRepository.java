package com.example.it211_project.repository;

import com.example.it211_project.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByCourseNameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );
}