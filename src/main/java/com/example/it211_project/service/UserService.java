package com.example.it211_project.service;

import com.example.it211_project.dto.ChangePasswordRequest;
import com.example.it211_project.dto.ForgotPasswordRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.it211_project.dto.RegisterStudentRequest;
import com.example.it211_project.dto.UserResponse;
import com.example.it211_project.entity.Role;
import com.example.it211_project.entity.User;
import com.example.it211_project.repository.RoleRepository;
import com.example.it211_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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

    public String changePassword(
            ChangePasswordRequest request,
            String username
    ) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Old password incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        return "Password changed";
    }
    public String forgotPassword(
            ForgotPasswordRequest request
    ) {
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        return "Password reset success";
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
