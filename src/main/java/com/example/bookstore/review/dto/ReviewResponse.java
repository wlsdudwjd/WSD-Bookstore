package com.example.bookstore.review.dto;

public record ReviewResponse(Long id, Long bookId, String content, int rating, int likes) {
}
