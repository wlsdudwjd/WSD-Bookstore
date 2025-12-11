package com.example.bookstore.review.entity;

public record Review(Long id, Long bookId, String content, int rating) {
}
