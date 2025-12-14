package com.example.bookstore.user.dto;

import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.Role;
import com.example.bookstore.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        Integer userId,
        String email,
        String name,
        String phoneNumber,
        String address,
        Gender gender,
        LocalDate birthday,
        Role role,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getGender(),
                user.getBirthday(),
                user.getRole(),
                user.getActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}