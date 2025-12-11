package com.example.bookstore.comment.dto;

public record CommentDto(Long id, Long reviewId, String content, int likes) {
}
