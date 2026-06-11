package com.example.it211_project.controller;

import com.example.it211_project.dto.GradeRequest;
import com.example.it211_project.dto.MaterialRequest;
import com.example.it211_project.service.LecturerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturer")
@RequiredArgsConstructor
@PreAuthorize("hasRole('LECTURER')")
public class LecturerController {

    private final LecturerService lecturerService;

    @PostMapping("/grades")
    public String gradeProject(
            @Valid @RequestBody GradeRequest request
    ) {
        return lecturerService.gradeProject(request);
    }

    @PostMapping("/materials")
    public String uploadMaterial(
            @Valid @RequestBody MaterialRequest request
    ) {
        return lecturerService.uploadMaterial(request);
    }
}