package com.example.it211_project.service;

import com.example.it211_project.dto.RegisterStudentRequest;
import com.example.it211_project.dto.UserResponse;
import com.example.it211_project.entity.Role;
import com.example.it211_project.entity.User;
import com.example.it211_project.repository.RoleRepository;
import com.example.it211_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // FR-04 Register Student
    public UserResponse registerStudent(RegisterStudentRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow();

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .build();

        user.getRoles().add(studentRole);

        User saved = userRepository.save(user);

        return mapToResponse(saved);
    }

    // FR-05 Search User
    public Page<UserResponse> searchUsers(
            String keyword,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository
                .findByFullNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(
                        keyword,
                        keyword,
                        pageable
                )
                .map(this::mapToResponse);
    }

    private UserResponse mapToResponse(User user) {

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.isActive(),
                roles
        );
    }
}