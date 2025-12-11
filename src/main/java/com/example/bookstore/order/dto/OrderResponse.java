package com.example.bookstore.order.dto;

public record OrderResponse(Long id, Long userId, String status, double totalPrice) {
}
