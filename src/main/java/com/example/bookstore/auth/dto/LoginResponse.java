package com.example.bookstore.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {
}