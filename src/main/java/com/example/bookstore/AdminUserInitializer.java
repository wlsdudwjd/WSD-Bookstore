package com.example.bookstore;

import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.Role;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@example.com";

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode("123"))
                .name("관리자")
                .phoneNumber("01000000000")
                .address("어딘가 관리자 집")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1990, 1, 1))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
    }
}