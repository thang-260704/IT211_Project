package com.example.it211_project.service;

import com.example.it211_project.dto.ChangePasswordRequest;
import com.example.it211_project.dto.ForgotPasswordRequest;
import com.example.it211_project.dto.RegisterStudentRequest;
import com.example.it211_project.entity.Role;
import com.example.it211_project.entity.User;
import com.example.it211_project.repository.RoleRepository;
import com.example.it211_project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Test
    void registerStudent_shouldEncodePasswordBeforeSave() {
        UserService userService = new UserService(
                userRepository,
                roleRepository,
                passwordEncoder
        );

        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setFullName("Nguyen Van A");
        request.setUsername("student_test");
        request.setEmail("student@test.com");
        request.setPassword("123456");

        Role role = Role.builder()
                .id(1L)
                .name("ROLE_STUDENT")
                .build();

        when(userRepository.existsByUsername("student_test"))
                .thenReturn(false);

        when(userRepository.existsByEmail("student@test.com"))
                .thenReturn(false);

        when(roleRepository.findByName("ROLE_STUDENT"))
                .thenReturn(Optional.of(role));

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(1L);
                    return user;
                });

        userService.registerStudent(request);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertNotEquals("123456", savedUser.getPassword());
        assertTrue(
                passwordEncoder.matches(
                        "123456",
                        savedUser.getPassword()
                )
        );
    }

    @Test
    void registerStudent_shouldThrowException_whenUsernameExists() {
        UserService userService = new UserService(
                userRepository,
                roleRepository,
                passwordEncoder
        );

        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setUsername("student_test");
        request.setEmail("student@test.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername("student_test"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.registerStudent(request)
        );

        assertEquals(
                "Username already exists",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerStudent_shouldThrowException_whenEmailExists() {
        UserService userService = new UserService(
                userRepository,
                roleRepository,
                passwordEncoder
        );

        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setUsername("student_test");
        request.setEmail("student@test.com");
        request.setPassword("123456");

        when(userRepository.existsByUsername("student_test"))
                .thenReturn(false);

        when(userRepository.existsByEmail("student@test.com"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.registerStudent(request)
        );

        assertEquals(
                "Email already exists",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_shouldUpdateEncodedPassword() {
        UserService userService = new UserService(
                userRepository,
                roleRepository,
                passwordEncoder
        );

        User user = User.builder()
                .id(1L)
                .fullName("Nguyen Van A")
                .username("student_test")
                .email("student@test.com")
                .password(passwordEncoder.encode("oldpass"))
                .active(true)
                .roles(new HashSet<>())
                .build();

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("oldpass");
        request.setNewPassword("newpass123");

        when(userRepository.findByUsername("student_test"))
                .thenReturn(Optional.of(user));

        userService.changePassword(
                request,
                "student_test"
        );

        assertTrue(
                passwordEncoder.matches(
                        "newpass123",
                        user.getPassword()
                )
        );

        verify(userRepository).save(user);
    }

    @Test
    void changePassword_shouldThrowException_whenOldPasswordIncorrect() {
        UserService userService = new UserService(
                userRepository,
                roleRepository,
                passwordEncoder
        );

        User user = User.builder()
                .id(1L)
                .username("student_test")
                .password(passwordEncoder.encode("correct-old-password"))
                .active(true)
                .roles(new HashSet<>())
                .build();

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("wrong-old-password");
        request.setNewPassword("newpass123");

        when(userRepository.findByUsername("student_test"))
                .thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.changePassword(
                        request,
                        "student_test"
                )
        );

        assertEquals(
                "Old password incorrect",
                exception.getMessage()
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void forgotPassword_shouldResetEncodedPassword() {
        UserService userService = new UserService(
                userRepository,
                roleRepository,
                passwordEncoder
        );

        User user = User.builder()
                .id(1L)
                .username("student_test")
                .password(passwordEncoder.encode("oldpass"))
                .active(true)
                .roles(new HashSet<>())
                .build();

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setUsername("student_test");
        request.setNewPassword("resetpass123");

        when(userRepository.findByUsername("student_test"))
                .thenReturn(Optional.of(user));

        String result = userService.forgotPassword(request);

        assertEquals("Password reset success", result);

        assertTrue(
                passwordEncoder.matches(
                        "resetpass123",
                        user.getPassword()
                )
        );

        verify(userRepository).save(user);
    }
}