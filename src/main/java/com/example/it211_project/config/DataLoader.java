package com.example.it211_project.config;

import com.example.it211_project.entity.Role;
import com.example.it211_project.entity.User;
import com.example.it211_project.repository.RoleRepository;
import com.example.it211_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner loadData() {
        return args -> {

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder().name("ROLE_ADMIN").build()
                    ));

            Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder().name("ROLE_STUDENT").build()
                    ));

            Role lecturerRole = roleRepository.findByName("ROLE_LECTURER")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder().name("ROLE_LECTURER").build()
                    ));

            User admin = userRepository.findByUsername("admin")
                    .orElse(User.builder()
                            .fullName("Admin")
                            .username("admin")
                            .email("admin@gmail.com")
                            .password(passwordEncoder.encode("123456"))
                            .active(true)
                            .build());

            admin.setRoles(Set.of(adminRole));
            admin.setActive(true);
            userRepository.save(admin);

            User lecturer = userRepository.findByUsername("lecturer")
                    .orElse(User.builder()
                            .fullName("Lecturer")
                            .username("lecturer")
                            .email("lecturer@gmail.com")
                            .password(passwordEncoder.encode("123456"))
                            .active(true)
                            .build());

            lecturer.setRoles(Set.of(lecturerRole));
            lecturer.setActive(true);
            userRepository.save(lecturer);
        };
    }
}