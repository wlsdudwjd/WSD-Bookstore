package com.example.bookstore.review.dto;

import java.time.LocalDateTime;

public record ReviewUpdateResponse(
        LocalDateTime updatedAt
) {
}
