package com.example.bookstore.user.dto;

import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.User;

import java.time.LocalDate;

public record UserProfileResponse(
        String email,
        String name,
        String phoneNumber,
        String address,
        Gender gender,
        LocalDate birthday
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getGender(),
                user.getBirthday()
        );
    }
}