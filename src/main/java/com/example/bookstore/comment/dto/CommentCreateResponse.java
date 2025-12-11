package com.example.bookstore.comment.dto;

import java.time.LocalDateTime;

public record CommentCreateResponse(
        Long commentId,
        LocalDateTime createdAt
) {
}
