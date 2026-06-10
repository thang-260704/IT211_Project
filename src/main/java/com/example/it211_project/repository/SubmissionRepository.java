package com.example.it211_project.repository;

import com.example.it211_project.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository
        extends JpaRepository<Submission, Long> {
}