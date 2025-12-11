package com.example.bookstore.wishlist.dto;

import jakarta.validation.constraints.NotNull;

public record WishlistRequest(
        @NotNull
        Long bookId
) {
}
