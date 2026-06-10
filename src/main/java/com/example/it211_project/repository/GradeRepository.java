package com.example.it211_project.repository;

import com.example.it211_project.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository
        extends JpaRepository<Grade, Long> {
}