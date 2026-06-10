package com.example.it211_project.repository;

import com.example.it211_project.entity.LectureMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureMaterialRepository
        extends JpaRepository<LectureMaterial, Long> {
}