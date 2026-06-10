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

            if(roleRepository.count() == 0){

                Role adminRole = roleRepository.save(
                        Role.builder()
                                .name("ROLE_ADMIN")
                                .build()
                );

                Role studentRole = roleRepository.save(
                        Role.builder()
                                .name("ROLE_STUDENT")
                                .build()
                );

                User admin = User.builder()
                        .fullName("Admin")
                        .username("admin")
                        .email("admin@gmail.com")
                        .password(
                                passwordEncoder.encode("123456")
                        )
                        .active(true)
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(admin);
            }
            Role lecturerRole = roleRepository.save(
                    Role.builder()
                            .name("ROLE_LECTURER")
                            .build()
            );
            User lecturer = User.builder()
                    .fullName("Lecturer")
                    .username("lecturer")
                    .email("lecturer@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .active(true)
                    .roles(Set.of(lecturerRole))
                    .build();

            userRepository.save(lecturer);
        };
    }
}