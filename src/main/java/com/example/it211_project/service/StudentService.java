package com.example.it211_project.service;

import com.example.it211_project.dto.SubmissionRequest;
import com.example.it211_project.entity.Course;
import com.example.it211_project.entity.Submission;
import com.example.it211_project.entity.User;
import com.example.it211_project.repository.CourseRepository;
import com.example.it211_project.repository.SubmissionRepository;
import com.example.it211_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    private final SubmissionRepository submissionRepository;

    public String submitProject(
            SubmissionRequest request,
            String username
    ) {
        User student = userRepository
                .findByUsername(username)
                .orElseThrow();

        Course course = courseRepository
                .findById(request.getCourseId())
                .orElseThrow();

        Submission submission =
                Submission.builder()
                        .student(student)
                        .course(course)
                        .githubUrl(request.getGithubUrl())
                        .reportUrl(request.getReportUrl())
                        .status("SUBMITTED")
                        .build();

        submissionRepository.save(submission);

        return "Submit project success";
    }
}