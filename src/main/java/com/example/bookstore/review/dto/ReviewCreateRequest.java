package com.example.bookstore.review.dto;

public record ReviewCreateRequest(Long bookId, String content, int rating) {
}
