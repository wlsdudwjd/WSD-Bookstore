package com.example.bookstore.cart.dto;

import java.time.LocalDateTime;

public record CartItemUpsertResponse(
        Long cartItemId,
        Integer quantity,
        LocalDateTime updatedAt
) {
}
