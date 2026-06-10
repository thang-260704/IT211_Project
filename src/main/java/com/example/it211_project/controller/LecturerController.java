package com.example.it211_project.controller;

import com.example.it211_project.dto.GradeRequest;
import com.example.it211_project.dto.MaterialRequest;
import com.example.it211_project.service.LecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final LecturerService lecturerService;

    @PostMapping("/grades")
    public String gradeProject(
            @RequestBody GradeRequest request
    ) {
        return lecturerService.gradeProject(request);
    }

    @PostMapping("/materials")
    public String uploadMaterial(
            @RequestBody MaterialRequest request
    ) {
        return lecturerService.uploadMaterial(request);
    }
}