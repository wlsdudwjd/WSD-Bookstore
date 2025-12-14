package com.example.bookstore.user.dto;

import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserProfileResponse(
        Integer userId,
        String email,
        String name,
        String phoneNumber,
        String address,
        Gender gender,
        LocalDate birthday,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getGender(),
                user.getBirthday(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
