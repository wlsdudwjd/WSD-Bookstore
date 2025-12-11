package com.example.bookstore.user.dto;

import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.Role;

import java.time.LocalDate;

public record UserProfileResponse(
        Long userId,
        String email,
        String name,
        String phoneNumber,
        String address,
        Gender gender,
        LocalDate birthday,
        Role role
) {
}