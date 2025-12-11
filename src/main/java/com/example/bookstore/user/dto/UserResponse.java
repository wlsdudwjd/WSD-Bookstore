package com.example.bookstore.user.dto;

import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        Long userId,
        String email,
        String name,
        String phoneNumber,
        String address,
        Gender gender,
        LocalDate birthday,
        Role role,
        Boolean active,
        LocalDateTime createdAt
) {
}