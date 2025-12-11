package com.example.bookstore.comment.dto;

import java.time.LocalDateTime;

public record CommentUpdateResponse(
        LocalDateTime updatedAt
) {
}
