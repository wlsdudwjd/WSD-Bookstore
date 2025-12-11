package com.example.bookstore.comment.entity;

public record Comment(Long id, Long reviewId, String content) {
}
