package com.example.bookstore.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        UserInfo user
) {
    public record UserInfo(
            Integer userId,
            String name,
            String role
    ) {}
}
