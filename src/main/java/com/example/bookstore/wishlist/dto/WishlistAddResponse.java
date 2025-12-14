package com.example.bookstore.wishlist.dto;

import java.time.LocalDateTime;

public record WishlistAddResponse(
        Long bookId,
        LocalDateTime createdAt
) {
}
