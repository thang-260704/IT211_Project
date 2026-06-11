package com.example.it211_project.controller;

import com.example.it211_project.dto.CourseRequest;
import com.example.it211_project.dto.CourseResponse;
import com.example.it211_project.dto.UserResponse;
import com.example.it211_project.service.CourseService;
import com.example.it211_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;

    @GetMapping("/users")
    public Page<UserResponse> users(
            @RequestParam(defaultValue = "")
            String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {

        return userService.searchUsers(
                keyword,
                page,
                size
        );
    }

    @PostMapping("/courses")
    public CourseResponse createCourse(
            @RequestBody CourseRequest request
    ) {

        return courseService.create(request);
    }

    @PutMapping("/courses/{id}")
    public CourseResponse updateCourse(
            @PathVariable Long id,
            @RequestBody CourseRequest request
    ) {

        return courseService.update(id, request);
    }

    @DeleteMapping("/courses/{id}")
    public String deleteCourse(
            @PathVariable Long id
    ) {

        courseService.delete(id);

        return "Deleted";
    }

    @GetMapping("/courses")
    public Page<CourseResponse> searchCourse(
            @RequestParam(defaultValue = "")
            String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {

        return courseService.search(
                keyword,
                page,
                size
        );
    }
}
//FR-05: Admin quản lý User và Course, có CRUD, tìm kiếm và phân trang.//