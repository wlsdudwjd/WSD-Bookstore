package com.example.bookstore.auth.dto;

import com.example.bookstore.user.entity.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record SignupRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank String name,
        @NotBlank String phoneNumber,
        @NotBlank String address,
        @NotNull Gender gender,
        @NotNull LocalDate birthday
) {
}