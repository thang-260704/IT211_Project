package com.example.it211_project.service;

import com.example.it211_project.dto.CourseRequest;
import com.example.it211_project.dto.CourseResponse;
import com.example.it211_project.entity.Course;
import com.example.it211_project.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseResponse create(CourseRequest request) {

        Course course = Course.builder()
                .courseName(request.getCourseName())
                .description(request.getDescription())
                .duration(request.getDuration())
                .active(true)
                .build();

        return mapToResponse(courseRepository.save(course));
    }

    public CourseResponse update(
            Long id,
            CourseRequest request
    ) {

        Course course = courseRepository.findById(id)
                .orElseThrow();

        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setDuration(request.getDuration());

        return mapToResponse(courseRepository.save(course));
    }

    public void delete(Long id) {

        courseRepository.deleteById(id);
    }

    public Page<CourseResponse> search(
            String keyword,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return courseRepository
                .findByCourseNameContainingIgnoreCase(
                        keyword,
                        pageable
                )
                .map(this::mapToResponse);
    }

    private CourseResponse mapToResponse(Course course) {

        return new CourseResponse(
                course.getId(),
                course.getCourseName(),
                course.getDescription(),
                course.getDuration(),
                course.isActive()
        );
    }
}