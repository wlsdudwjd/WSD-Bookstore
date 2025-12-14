package com.example.bookstore.auth.dto;

import java.time.LocalDateTime;

public record SignupResponse(
        Integer userId,
        LocalDateTime createdAt
) {
}
