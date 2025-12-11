package com.example.bookstore.auth.dto;

import java.time.LocalDateTime;

public record SignupResponse(
        Long userId,
        LocalDateTime createdAt
) {
}
