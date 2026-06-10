package com.example.it211_project.service;

import com.example.it211_project.dto.GradeRequest;
import com.example.it211_project.dto.MaterialRequest;
import com.example.it211_project.entity.Grade;
import com.example.it211_project.entity.LectureMaterial;
import com.example.it211_project.entity.Submission;
import com.example.it211_project.repository.GradeRepository;
import com.example.it211_project.repository.LectureMaterialRepository;
import com.example.it211_project.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LecturerService {

    private final SubmissionRepository submissionRepository;

    private final GradeRepository gradeRepository;

    private final LectureMaterialRepository lectureMaterialRepository;

    public String gradeProject(
            GradeRequest request
    ) {
        Submission submission =
                submissionRepository
                        .findById(
                                request.getSubmissionId()
                        )
                        .orElseThrow();

        Grade grade =
                Grade.builder()
                        .submission(submission)
                        .score(request.getScore())
                        .feedback(request.getFeedback())
                        .build();

        gradeRepository.save(grade);

        submission.setStatus("GRADED");

        submissionRepository.save(submission);

        return "Grade success";
    }

    public String uploadMaterial(
            MaterialRequest request
    ) {
        LectureMaterial material =
                LectureMaterial.builder()
                        .title(request.getTitle())
                        .fileUrl(request.getFileUrl())
                        .build();

        lectureMaterialRepository.save(material);

        return "Upload material success";
    }
}